package jwormbench.test;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import jwormbench.core.IWorld;
import jwormbench.core.IOperation;
import jwormbench.core.OperationKind;
import jwormbench.core.WormBench;
import jwormbench.core.WormThread;
import jwormbench.defaults.World;
import jwormbench.defaults.DefaultNodeFactory;
import jwormbench.defaults.DefaultCoordinateFactory;
import jwormbench.defaults.DefaultOperationFactory;
import jwormbench.defaults.DefaultWormFactory;
import jwormbench.defaults.StepFactoryRandomDirAndSameOperation;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.setup.WorldFileLoader;
import jwormbench.setup.IWormsSetup;
import jwormbench.setup.WormsFileLoader;

public class BenchOperationsTests {
  static final int NR_OF_STEPS = 1000;
  static final IWorld world;
  static final IOperationFactory opsFac;
  static final IWormFactory wormFac;
  IOperation<Integer> op;
  
  static{
    final String WORLD_CONFIG_FILE = "config/128.txt";
    final String WORM_CONFIG_FILE = "config/W-B[1.8]-H[1.8]-128.txt";   
    ICoordinateFactory cordFac =  new DefaultCoordinateFactory();
    IWormsSetup wormSetup = new WormsFileLoader(WORM_CONFIG_FILE, cordFac);
    world = new World(
        new WorldFileLoader(
            WORLD_CONFIG_FILE, new DefaultNodeFactory()));
    wormFac = new DefaultWormFactory(cordFac, world, wormSetup);
    opsFac = new DefaultOperationFactory(world);
  }
  @Before
  public void clearWorldFromWorms(){
    for (int i = 0; i < world.getColumnsNum(); i++) {
      for (int j = 0; j < world.getRowsNum(); j++) {
        world.getNode(i, j).setWorm(null);
      }
    }
  }
  @Test
  public void benchReplaceMaxWithAverage(){ 
    op = opsFac.make(OperationKind.ReplaceMaxWithAverage);
  }
  @Test
  public void benchReplaceMedianWithAverage(){ 
    op = opsFac.make(OperationKind.ReplaceMedianWithAverage);
  }
  @Test
  public void benchReplaceMedianWithMax(){ 
    op = opsFac.make(OperationKind.ReplaceMedianWithMax);
  }
  @Test
  public void benchReplaceMedianWithMin(){ 
    op = opsFac.make(OperationKind.ReplaceMedianWithMin);
  }
  @Test
  public void benchReplaceMinWithAverage(){ 
    op = opsFac.make(OperationKind.ReplaceMinWithAverage);
  }
  @Test
  public void benchReplaceMaxAndMin(){ 
    op = opsFac.make(OperationKind.ReplaceMaxAndMin);
  }
  @Test
  public void benchSort(){ 
    op = opsFac.make(OperationKind.Sort);
  }
  @Test
  public void benchTranspose(){ 
    op = opsFac.make(OperationKind.Transpose);
  }
  
  @After
  public void assertTestResult()throws InterruptedException{
    IStepFactory stepsFac = new StepFactoryRandomDirAndSameOperation<Integer>(NR_OF_STEPS, op);
    WormBench bench = new WormBench(
        world,
        wormFac, 
        stepsFac, 
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME),
        1, // nr of threads 
        1, // nr of iterations
        0); // time out
    //
    // Get pre conditions
    //
    int initSum =  world.getSumOfAllNodes();
    //
    // Act - Run on main thread
    // Launch and run worker thread
    //
    bench.RunBenchmark("not sync");
    bench.LogExecutionTime();
    bench.LogConsistencyVerification();
    //
    // Assert post conditions
    //    
    WormThread wt = bench.getWormThread(0);    
    Assert.assertEquals("For operation: " + op.getKind(), 
        bench.getAccumulatedDiffOnWorld(), 
        wt.getAccumulatedDiffOnWorld());
    int finalWorldSum = world.getSumOfAllNodes() - bench.getAccumulatedDiffOnWorld();
    Assert.assertEquals("For operation: " + op.getKind(),initSum, finalWorldSum);
  }
}
