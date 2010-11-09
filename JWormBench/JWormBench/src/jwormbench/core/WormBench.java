/*
 * JWormBench: a Java benchmark based on WormBench - a synthetic 
 * workload for Transactinal Memory Systems Center www.bscmsrc.eu.
 * Copyright (C) 2010 INESC-ID Software Engineering Group
 * http://www.esw.inesc-id.pt
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author's contact:
 * INESC-ID Software Engineering Group
 * Rua Alves Redol 9
 * 1000 - 029 Lisboa
 * Portugal
 */
package jwormbench.core;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import jwormbench.config.params.NrOfIterations;
import jwormbench.config.params.NrOfThreads;
import jwormbench.config.params.TimeOut;
import jwormbench.factories.IStepFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.utils.ILogger;


public class WormBench {
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private final ILogger logger;
  private final java.util.List<WormThread> wThreads;
  private final IBenchWorld world;
  private int initWorldSum;
  private int timeoutSeconds;
  private long parallelElapsedTime; 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Inject
  public WormBench(  
      IBenchWorld world,
      IWormFactory wormFac,
      IStepFactory stepsFac,
      ILogger logger,
      @NrOfThreads int numThreads,
      @NrOfIterations int iterations,
      @TimeOut int timeoutSeconds
      ){
    this.world = world;
    this.wThreads = new LinkedList<WormThread>();
    this.logger = logger;
    this.timeoutSeconds = timeoutSeconds;
    Iterable<IWorm> worms = wormFac.make(numThreads);
    List<AbstractStep> steps = stepsFac.make();
    int operationsChunkSize = steps.size()/numThreads;
    int i = 0;
    for (IWorm w : worms) {
      int from = i*operationsChunkSize;
      int to = from + operationsChunkSize;
      wThreads.add(new WormThread(w, steps.subList(from, to), iterations));
    }
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ------------------ PROPERTIES --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public WormThread getWormThread(int i){
    return wThreads.get(i);
  }
  public int getAccumulatedDiffOnWorld(){
    int total = 0;
    for (WormThread wt : wThreads) {
      total += wt.getAccumulatedDiffOnWorld();
    }
    return total;
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   METHODS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public void RunBenchmark(String syncStat) throws InterruptedException{
    initWorldSum  = world.getSumOfAllNodes();
    logger.log(String.format("Benchmark started... World values sum = %d (sync strategy = %s)", initWorldSum, syncStat));
    //
    // Spawn a thread for every worm and run one worm on the main thread.
    //
    for (WormThread wt : wThreads) {
      wt.start();
    }
    long parallelTimeStart = System.nanoTime();
    if (timeoutSeconds > 0){
      //
      // Set the timeout. So that the threads exit normally and not by enforcing.
      //
      for (WormThread wt : wThreads) {
        wt.join(timeoutSeconds*1000);
        long elapsedTime = System.nanoTime() - parallelTimeStart;
        if(elapsedTime > timeoutSeconds) break;
      }
      for (WormThread wt : wThreads) {
        wt.setTimedOut();
      }
    }
    else{
      for (WormThread wt : wThreads) {
        wt.join();
      }      
    }
    parallelElapsedTime = System.nanoTime() - parallelTimeStart;
    logger.log("Benchmark completed...");
  }
  public void LogExecutionTime(){
    int threadsNum = wThreads.size();
    //
    // The total number of operations executed by all worms.
    // This may differ because of the timeout.
    //
    long totalWorkLoad = 0;
    for (WormThread wt : wThreads) {
        totalWorkLoad += wt.getStepsCounter();
    }          
    //
    // Throughput per second
    //
    int throughPut = (int)(totalWorkLoad * 1000000000 / parallelElapsedTime);
    //
    // [Worms#] [Throughut] [TotalWorkLoad] [ParallelTimeCycles] [ParallelTime] [TotalTimeCycles] [TotalTime] [SynchType] [NxM] [W-Body] [W-Head] [W-BW-Init]
    //
    String logMessage = String.format(
      "threadsNum = %d,\n" +
      "throughPut per sec = %d,\n" +
      "totalWorkLoad = %d,\n" +
      "parallelTime = %f secs,\n",
      threadsNum, throughPut, totalWorkLoad, (double)parallelElapsedTime / 1000000000.0);
    logger.log(logMessage);
  }
  public void LogConsistencyVerification(){
    int finalWorldSum = world.getSumOfAllNodes() - this.getAccumulatedDiffOnWorld();
    System.out.println(initWorldSum == finalWorldSum? "Verification SUCCEED": "Verification FAILED");
    System.out.println(String.format(
        "World values sum= %d = %d - %d\n", 
        finalWorldSum,
        world.getSumOfAllNodes(), 
        getAccumulatedDiffOnWorld()));    
  }
}
