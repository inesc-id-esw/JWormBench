package jwormbench.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.Assert;
import jwormbench.core.IWorld;
import jwormbench.core.INode;
import jwormbench.core.ICoordinate;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.defaults.World;
import jwormbench.defaults.DefaultNodeFactory;
import jwormbench.defaults.DefaultCoordinateFactory;
import jwormbench.defaults.DefaultOperationFactory;
import jwormbench.defaults.DefaultWormFactory;
import jwormbench.factories.INodeFactory;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.setup.IWorlSetup;
import jwormbench.setup.IWormsSetup;
import jwormbench.setup.WormsFileLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OperationsTests {
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ------------ AUXILIAR FUNCTIONS ------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  // 5 rows x 10 columns
  private final static int [][] INIT_WORLD_VALUES = {
    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, // Col 1 - x = 0
    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, // Col 2 - x = 1
    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, // Col 3 - x = 2
    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, // Col 4 - x = 3
    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9} // Col 5 - x = 4
  };
  
  private static class ConstBenchWorldSetup implements IWorlSetup{
    public INode[][] loadWorld() {
      INodeFactory nodeFac = new DefaultNodeFactory();
      INode[][] world = new INode[INIT_WORLD_VALUES.length][INIT_WORLD_VALUES[0].length];
      for (int i = 0; i < world.length; i++) {
        for (int j = 0; j < world[i].length; j++) {
          world[i][j] = nodeFac.make(INIT_WORLD_VALUES[i][j]);
        }
      }
      return world;
    }
  }  
  
  static final  String CONFIG_FILE_NAME = "config/WormFileLoaderUT_config.txt"; 
  File configFile;

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // --------------------  FIELDS  --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  IWorld world;
  IWorm[] worms;
  IOperation<Integer> sum, avg, repMaxAvg, repMinAvg, repMedianAvg, repMaxMin, repMedianMin, repMedianMax;
  IOperation<IOperation.Element<Integer, Integer>> min, max;
  IOperation<IOperation.Element<ICoordinate, Integer>> med;
  private IOperation<Integer> sort, trans;

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // --------------- SETUP & TEARDOWN ------------------ 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private void writeInConfigFile(String[] lines) throws IOException{
    FileWriter writer = new FileWriter(configFile);
    for (String line : lines) {
      writer.write(line);
      writer.write(System.getProperty("line.separator"));
    }
    writer.close();
  }
  @Before
  public void setupTest() throws IOException{
    configFile = new File("target/classes/" + CONFIG_FILE_NAME);
    configFile.createNewFile();
    //
    // BenchWorld (Arrange)
    // 
    world = new World(new ConstBenchWorldSetup());
    //
    // Worms Setup and Factory (Arrange)
    // 
    String[] lines = new String[]{"WormID = 1; Name = Worm1; GroupID = 1; HeadSize = 3; Speed = 1; BodyLength = 4; Body = [1,5][2,5][2,4][2,3]"};
    worms = new IWorm[lines.length];
    writeInConfigFile(lines);
    ICoordinateFactory coordFac = new DefaultCoordinateFactory();
    IWormsSetup wormsSetup = new WormsFileLoader(CONFIG_FILE_NAME, coordFac);
    IWormFactory wormFac = new DefaultWormFactory(coordFac, world, wormsSetup);
    //
    // Verify correct initialization
    //
    for(int x = 0; x < INIT_WORLD_VALUES.length; x++){
      for(int y = 0; y < INIT_WORLD_VALUES[x].length; y++){ 
         Assert.assertEquals(null, world.getNode(x, y).getWorm());
      }
    }
    //
    // Create Worms (Act)
    //
    int i = 0;
    for (IWorm w : wormFac.make()) {
      worms[i++] = w; 
    }
    //
    //  Worm's head - Verify correct initialization
    //
    for(int x = 0; x < INIT_WORLD_VALUES.length; x++){
      for(int y = 0; y < INIT_WORLD_VALUES[x].length; y++){
        // body coordinates
        if(((x == 1) && (y==5)) || ((x == 2) && (y==5)) || ((x == 2) && (y==4)) || ((x == 2) && (y==3))
            //head coordinates
            || ((x == 1) && (y==3)) 
            || ((x == 0) && (y==4))|| ((x == 1) && (y==4)) 
            || ((x == 0) && (y==5))|| ((x == 1) && (y==5)) || ((x == 4) && (y==5))
            || ((x == 0) && (y==6))|| ((x == 1) && (y==6))
            || ((x == 1) && (y==7)) )
          Assert.assertNotSame(String.format("(%d, %d)", x, y), null, world.getNode(x, y).getWorm());
        else
          Assert.assertEquals(String.format("(%d, %d)", x, y), null, world.getNode(x, y).getWorm());
      }
    }
    //
    // Create Operations
    //
    IOperationFactory opFac = new DefaultOperationFactory(world);
    sum = opFac.<Integer>make(OperationKind.Sum);
    avg = opFac.<Integer>make(OperationKind.Average);
    med = opFac.make(OperationKind.Median);
    min = opFac.make(OperationKind.Minimum);
    max = opFac.make(OperationKind.Maximum);
    repMaxAvg = opFac.<Integer>make(OperationKind.ReplaceMaxWithAverage);
    repMinAvg = opFac.<Integer>make(OperationKind.ReplaceMinWithAverage);
    repMedianAvg = opFac.<Integer>make(OperationKind.ReplaceMedianWithAverage);
    repMaxMin = opFac.<Integer>make(OperationKind.ReplaceMaxAndMin);
    repMedianMin = opFac.<Integer>make(OperationKind.ReplaceMedianWithMin);
    repMedianMax = opFac.<Integer>make(OperationKind.ReplaceMedianWithMax);
    sort = opFac.<Integer>make(OperationKind.Sort);
    trans = opFac.<Integer>make(OperationKind.Transpose);
  }
  @After
  public void tearDownTest(){
    configFile.delete();
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------    UNIT TESTS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * This unit test verifies the conformity of 
   * immutable operations as: sum, average, medium, maximum and minimum. 
   */
  @Test
  public void testImmutableOperations() throws IOException{
    //
    // Act and Assert
    // The value of each head's node is equals to its y coordinate.
    // 
    Assert.assertEquals(45, (int) sum.performOperation(worms[0])); // = 3 + 4 + 4 + 5 + 5 + 5 + 6 + 6 + 7
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));
    Assert.assertEquals(5, (int) med.performOperation(worms[0]).value);
    Assert.assertEquals(3, (int) min.performOperation(worms[0]).value);
    Assert.assertEquals(7, (int) max.performOperation(worms[0]).value);
  }
  /**
   * These operations will change the world state.
   */
  @Test
  public void testReplaceMaxWithAverage(){
    //
    // Assert precondition
    //
    Assert.assertEquals(7, world.getNode(1, 7).getValue());
    Assert.assertEquals(45, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0])); 
    Assert.assertEquals(7, (int) max.performOperation(worms[0]).value);
    //
    // Act and Assert
    //
    Assert.assertEquals(-2, (int) repMaxAvg.performOperation(worms[0])); // Replace 7 by 5
    Assert.assertEquals(5, world.getNode(1, 7).getValue()); 
    Assert.assertEquals(43, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(4, (int) avg.performOperation(worms[0])); 
    Assert.assertEquals(6, (int) max.performOperation(worms[0]).value);
    Assert.assertEquals(-2, (int) repMaxAvg.performOperation(worms[0]));
    Assert.assertEquals(41, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(4, (int) avg.performOperation(worms[0]));
    Assert.assertEquals(6, (int) max.performOperation(worms[0]).value);
  }
  /**
   * These operations will change the world state.
   */
  @Test
  public void testReplaceMinWithAverage(){
    //
    // Assert precondition
    //
    Assert.assertEquals(3, world.getNode(1, 3).getValue());
    //
    // Act and Assert
    //
    Assert.assertEquals(2, (int) repMinAvg.performOperation(worms[0]));// Replace 3 by 5
    Assert.assertEquals(5, world.getNode(1, 3).getValue()); 
    Assert.assertEquals(47, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));    
    Assert.assertEquals(4, (int) min.performOperation(worms[0]).value);
    //
    // Act and Assert again
    //
    Assert.assertEquals(1, (int) repMinAvg.performOperation(worms[0])); // Replace 4 by 5
    Assert.assertEquals(48, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));    
    Assert.assertEquals(4, (int) min.performOperation(worms[0]).value); // There were 2 values 4
  }
  /**
   * These operations will change the world state.
   */
  @Test
  public void testReplaceMedianWithAverage(){
    //
    // Assert precondition
    //
    Assert.assertEquals(5, (int) med.performOperation(worms[0]).value);    
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));
    //
    // Act and Assert
    //
    Assert.assertEquals(0, (int) repMedianAvg.performOperation(worms[0])); // Replace 5 by 5
    Assert.assertEquals(5, (int) med.performOperation(worms[0]).value);
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));            
  }
  @Test
  public void testReplaceMaxAndMin(){
    //
    // Assert precondition
    //
    Assert.assertEquals(3, world.getNode(1, 3).getValue());
    Assert.assertEquals(7, world.getNode(1, 7).getValue());
    Assert.assertEquals(45, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));
    // 
    // Act
    //
    repMaxMin.performOperation(worms[0]); // Change the values between coordinates (1,3) and (1,7)
    // 
    // Assert
    //
    Assert.assertEquals(7, world.getNode(1, 3).getValue());
    Assert.assertEquals(3, world.getNode(1, 7).getValue());
    Assert.assertEquals(45, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));
  }
  /**
   * These operations will change the world state.
   */
  @Test
  public void testReplaceMedianWithMax(){
    //
    // Assert precondition
    //
    Assert.assertEquals(45, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));
    Assert.assertEquals(7, (int) max.performOperation(worms[0]).value);
    //
    // Act and Assert
    //
    Assert.assertEquals(2,(int) repMedianMax.performOperation(worms[0])); // Replace median 5 value with 7 
    Assert.assertEquals(47, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));        
  }
  /**
   * These operations will change the world state.
   */
  @Test
  public void testReplaceMedianWithMin(){
    //
    // Assert precondition
    //
    Assert.assertEquals(45, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));
    //
    // Act and Assert precondition
    //
    Assert.assertEquals(-2,(int) repMedianMin.performOperation(worms[0])); // Replace median 5 value with 3 
    Assert.assertEquals(43, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(4, (int) avg.performOperation(worms[0]));    
  }
  @Test
  public void testSort(){
    final int [] initHeadValues = {3,4,5,6,7,4,5,6,5}; 
    //
    // Assert precondition
    //
    for (int i = 0; i < initHeadValues.length; i++) {
      Assert.assertEquals(initHeadValues[i], world.getNode(worms[0].getHeadCoordinate(i)).getValue());
      Assert.assertEquals(initHeadValues[i], worms[0].getHeadValues()[i]);
    }
    Assert.assertEquals(45, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));
    // 
    // Act
    //
    sort.performOperation(worms[0]);
    //
    // Assert
    //
    Arrays.sort(initHeadValues);
    for (int i = 0; i < initHeadValues.length; i++) {
      Assert.assertEquals(initHeadValues[i], world.getNode(worms[0].getHeadCoordinate(i)).getValue());
      Assert.assertEquals(initHeadValues[i], worms[0].getHeadValues()[i]);      
    }
    Assert.assertEquals(45, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));    
  }
  @Test
  public void testTranspose(){
    final int [] initHeadValues = {3,4,5,6,7,4,5,6,5}; 
    final int [] transposedHeadValues = {5,6,5,4,7,6,5,4,3};
    //
    // Assert precondition
    //
    for (int i = 0; i < initHeadValues.length; i++) {
      Assert.assertEquals(initHeadValues[i], world.getNode(worms[0].getHeadCoordinate(i)).getValue());
      Assert.assertEquals(initHeadValues[i], worms[0].getHeadValues()[i]);
    }
    Assert.assertEquals(45, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));
    // 
    // Act
    //
    trans.performOperation(worms[0]);
    //
    // Assert
    //
    for (int i = 0; i < initHeadValues.length; i++) {
      Assert.assertEquals(transposedHeadValues [i], world.getNode(worms[0].getHeadCoordinate(i)).getValue());
      Assert.assertEquals(transposedHeadValues [i], worms[0].getHeadValues()[i]);      
    }
    Assert.assertEquals(45, (int) sum.performOperation(worms[0]));
    Assert.assertEquals(5, (int) avg.performOperation(worms[0]));    
  }

}
