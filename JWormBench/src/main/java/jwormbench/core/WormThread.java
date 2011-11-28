package jwormbench.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WormThread implements Runnable{
  public static final int RESULTS_QUEUE_LIMIT = 200;
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private final IWorm [] worms;
  private final Iterable<IStep> operations;
  private int iterations;
  private volatile boolean timedOut;
  private final int [] stepsCounter;
  private int accumulatedDiffStateOnWorld;
  /**
   * According to WormBench's implementation we keep a buffer storing some operations results.
   * Checking and offering results into this buffer will affect all operations performance.
   * However we keep it like the original.
   * We follow a FIFO approach.
   * ??! Maybe is there some intention in delaying the duration of each operation!?
   * !!! Maybe to avoid Jitter optimizations !!! 
   */
  protected final Queue<Object> resultsBuffer = new LinkedList<Object>();

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public WormThread(Iterable<IStep> operations, int iterations, List<IWorm> worms){
    super();
    this.worms = worms.toArray(new IWorm[worms.size()]);
    this.operations = operations;
    this.iterations = iterations;
    this.timedOut = false;
    this.stepsCounter = new int[OperationKind.values().length];
  }
  public int getStepsCounter(){
    int sum = 0;
    for (int i = 0; i < stepsCounter.length; i++) {
      sum += stepsCounter[i];
    }
    return sum;
  } 
  public int getAccumulatedDiffOnWorld(){
    return accumulatedDiffStateOnWorld;
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   METHODS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public void setTimedOut(){
    timedOut = true; 
  }
  @Override
  public void run() {
    accumulatedDiffStateOnWorld = 0;
    for (int i = 0; i < stepsCounter.length; i++) {
     stepsCounter[i] = 0;
    }
    for (int j = 0; (j < iterations) && !timedOut; j++) {
      for (IStep step : operations) {
        if(timedOut) break;
        if(step.isWorldModified()){
          //
          // If this step change the world state (the sum of all nodes values), 
          // then it will returns the difference of that value, to the previous 
          // state before change. In this case the result is an Integer.
          // 
          Object res = step.performStep(worms[j%worms.length]);
          accumulatedDiffStateOnWorld += (Integer) res;  
        }else{
          //
          // This branch is as original in JWormBEnch
          //
          Object res = step.performStep(worms[j%worms.length]);
          if(resultsBuffer.size() >= RESULTS_QUEUE_LIMIT)
            resultsBuffer.poll();
          resultsBuffer.offer(res);
        }
        stepsCounter[step.getOpKind().ordinal()]++;
      }
    }
  }
}
