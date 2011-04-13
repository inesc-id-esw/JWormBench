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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import jwormbench.config.params.NrOfIterations;
import jwormbench.config.params.NrOfThreads;
import jwormbench.config.params.TimeOut;
import jwormbench.factories.IStepFactory;
import jwormbench.factories.IWormFactory;


public class WormBench {
  private static final String NEW_LINE = System.getProperty("line.separator");

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private final Logger logger;
  private final java.util.List<WormThread> wThreads;
  public final IWorld world;
  private int initWorldSum;
  private int timeoutSeconds;
  private long parallelElapsedTime; 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Inject
  public WormBench(  
      IWorld world,
      IWormFactory wormFac,
      IStepFactory stepsFac,
      Logger logger,
      @NrOfThreads int numThreads,
      @NrOfIterations int iterations,
      @TimeOut int timeoutSeconds
      ){
    this.world = world;
    this.wThreads = new LinkedList<WormThread>();
    this.logger = logger;
    this.timeoutSeconds = timeoutSeconds;
    List<IWorm> worms = new ArrayList<IWorm>(wormFac.make());
    if(worms.size() < numThreads) 
      throw new IllegalArgumentException("The number of worms could not be lower than the number of threads.");
    List<IStep> steps = stepsFac.make();
    float nrOfWormsPerThread = ((float)worms.size())/numThreads;
    int operationsChunkSize = steps.size()/numThreads;
    for (int j = 0; j < numThreads; j++) {
      float fromWormIdx = j*nrOfWormsPerThread;
      float toWormIdx = fromWormIdx + nrOfWormsPerThread;
      int fromOpIdx = j*operationsChunkSize;
      int toOpIdx = fromOpIdx + operationsChunkSize;
      wThreads.add(
          new WormThread(
              steps.subList(fromOpIdx, toOpIdx), 
              iterations, 
              worms.subList((int)fromWormIdx, (int)toWormIdx)));
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
    logger.info(String.format("Benchmark started... World values sum = %d (sync strategy = %s)%s", initWorldSum, syncStat, NEW_LINE));
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
    logger.info("Benchmark completed..." + NEW_LINE);
  }
  public void LogExecutionTime(){
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
      "throughPut per sec = %d,\n" +
      "totalWorkLoad = %d,\n" +
      "parallelTime = %f secs,\n",
      throughPut, totalWorkLoad, (double)parallelElapsedTime / 1000000000.0);
    logger.info(logMessage + NEW_LINE);
  }
  public void LogConsistencyVerification(){
    int finalWorldSum = world.getSumOfAllNodes() - this.getAccumulatedDiffOnWorld();
    logger.info(initWorldSum == finalWorldSum? "Verification SUCCEED": "Verification FAILED");
    logger.info(NEW_LINE);
    logger.info(String.format(
        "World values sum= %d = %d - %d\n\n", 
        finalWorldSum,
        world.getSumOfAllNodes(), 
        getAccumulatedDiffOnWorld()));    
  }
}
