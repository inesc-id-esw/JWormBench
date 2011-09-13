package jwormbench.tests;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import jwormbench.core.AbstractStep;
import jwormbench.core.Direction;
import jwormbench.core.IBenchWorld;
import jwormbench.core.IWorm;
import jwormbench.core.WormBench;
import jwormbench.core.WormThread;
import jwormbench.defaults.BenchWorld;
import jwormbench.defaults.DefaultBenchWorldNodeFactory;
import jwormbench.defaults.DefaultCoordinateFactory;
import jwormbench.defaults.DefaultOperationFactory;
import jwormbench.defaults.DefaultWormFactory;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.setup.BenchWorldFileLoader;
import jwormbench.setup.IStepSetup;
import jwormbench.setup.IWormsSetup;
import jwormbench.setup.StepsFileLoader;
import jwormbench.setup.WormsFileLoader;
import jwormbench.utils.ILogger;

public class BenchmarkUT {
  
  @Test
  public void testBenchWorm() throws InterruptedException{
    final String WORLD_CONFIG_FILE = "config/128.txt";
    final String WORM_CONFIG_FILE = "config/W-B[1.8]-H[1.8]-128.txt";
    final String OPERATIONS_CONFIG_FILE = "config/1000_10.txt";
   
    ICoordinateFactory cordFac =  new DefaultCoordinateFactory();
    IWormsSetup wormSetup = new WormsFileLoader(WORM_CONFIG_FILE, cordFac);
    IBenchWorld world = new BenchWorld(
        new BenchWorldFileLoader(
            WORLD_CONFIG_FILE, new DefaultBenchWorldNodeFactory()));
    IWormFactory wormFac = new DefaultWormFactory(cordFac, world, wormSetup);
    
    final IOperationFactory opsFac = new DefaultOperationFactory(world);
    IStepFactory stepsFac = new IStepFactory() {      
      @Override
      public List<AbstractStep> make() {
        List<AbstractStep> steps = new LinkedList<AbstractStep>();
        for (IStepSetup.OperationProperties opProps: new StepsFileLoader(OPERATIONS_CONFIG_FILE)) {
          Direction dir = Direction.values()[(int)(Math.random()*3)];
          // ignore direction given from file to make
          // the worm navigate on different directions for each test. 
          // steps.add(new AbstractStep(opProps.direction, opsFac.make(opProps.operationKind)) {
          steps.add(new AbstractStep(dir, opsFac.make(opProps.operationKind)) {
            public Object performStep(IWorm worm) {
              Object res = op.performOperation(worm);
              worm.move(direction);
              worm.updateWorldUnderWorm();
              return res;
            }
          });
        }
        return steps;
      }
    };
    WormBench bench = new WormBench(
        world,
        wormFac, 
        stepsFac, 
        new ILogger() {public void log(String msg) {System.out.println(msg);}},
        1, // nr of threads 
        100, // nr of iterations
        0); // time out
    try{
     bench.getWormThread(1); 
    }catch(Exception e){
      Assert.assertEquals(e.getClass(), IndexOutOfBoundsException.class);
    }
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
    Assert.assertEquals(bench.getAccumulatedDiffOnWorld(),wt.getAccumulatedDiffOnWorld());
    Assert.assertEquals(initSum, world.getSumOfAllNodes() - bench.getAccumulatedDiffOnWorld());
  }
}
