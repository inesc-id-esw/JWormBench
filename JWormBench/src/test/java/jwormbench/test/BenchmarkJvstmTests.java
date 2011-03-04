package jwormbench.test;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Test;

import junit.framework.Assert;
import jwormbench.core.IWorld;
import jwormbench.core.WormBench;
import jwormbench.core.WormThread;
import jwormbench.defaults.World;
import jwormbench.defaults.DefaultCoordinateFactory;
import jwormbench.defaults.DefaultOperationFactory;
import jwormbench.defaults.DefaultWormFactory;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.setup.WorldFileLoader;
import jwormbench.setup.IWormsSetup;
import jwormbench.setup.StepsFileLoader;
import jwormbench.setup.WormsFileLoader;
import jwormbench.sync.jvstm.JvstmBenchNodeFactory;
import jwormbench.sync.jvstm.JvstmStepFactory;

public class BenchmarkJvstmTests {

  @Test
  public void testBenchWorm() throws InterruptedException{
    final String WORLD_CONFIG_FILE = "config/128.txt";
    final String WORM_CONFIG_FILE = "config/W-B[1.8]-H[1.8]-128.txt";
    final String OPERATIONS_CONFIG_FILE = "config/1000_10.txt";

    ICoordinateFactory cordFac =  new DefaultCoordinateFactory();
    IWormsSetup wormSetup = new WormsFileLoader(WORM_CONFIG_FILE, cordFac);
    IWorld world = new World(
        new WorldFileLoader(
            WORLD_CONFIG_FILE, new JvstmBenchNodeFactory()));
    IWormFactory wormFac = new DefaultWormFactory(cordFac, world, wormSetup);

    IOperationFactory opsFac = new DefaultOperationFactory(world);
    IStepFactory stepsFac = new JvstmStepFactory(new StepsFileLoader(OPERATIONS_CONFIG_FILE), opsFac);
    Logger logger = Logger.getLogger("");
    logger.getHandlers()[0].setFormatter(new Formatter() {
      public String format(LogRecord record) {
        return record.getMessage();
      }
    });
    WormBench bench = new WormBench(
        world,
        wormFac, 
        stepsFac, 
        logger,
        1, // nr of threads 
        1, // nr of iterations
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
