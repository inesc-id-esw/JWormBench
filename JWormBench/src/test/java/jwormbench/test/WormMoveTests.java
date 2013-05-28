package jwormbench.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;
import jwormbench.core.Direction;
import jwormbench.core.IWorld;
import jwormbench.core.INode;
import jwormbench.core.IWorm;
import jwormbench.defaults.World;
import jwormbench.defaults.DefaultNodeFactory;
import jwormbench.defaults.DefaultCoordinateFactory;
import jwormbench.defaults.DefaultWormFactory;
import jwormbench.factories.INodeFactory;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.setup.IWorlSetup;
import jwormbench.setup.IWormsSetup;
import jwormbench.setup.WormsFileLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WormMoveTests {
  // 5 rows x 10 columns
  private final static int [][] INIT_WORLD_VALUES = {
    {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, // Col 1 - x = 0
    {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, // Col 2 - x = 1
    {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, // Col 3 - x = 2
    {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, // Col 4 - x = 3
    {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}}; // Col 5 - x = 4
    
  
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
  
  @Before
  public void setupTest() throws IOException{
    configFile = new File("target/classes/" + CONFIG_FILE_NAME);
    configFile.createNewFile();
  }
  @After
  public void tearDownTest(){
    configFile.delete();
  }
  private void writeInConfigFile(String[] lines) throws IOException{
    FileWriter writer = new FileWriter(configFile);
    for (String line : lines) {
      writer.write(line);
      writer.write(System.getProperty("line.separator"));
    }
    writer.close();
  }
  @Test
  public void createWormAndMove() throws IOException{
    //
    // BenchWorld (Arrange)
    // 
    ICoordinateFactory coordFac = new DefaultCoordinateFactory();
    IWorld world = new World(new ConstBenchWorldSetup());
    //
    // Worms Setup and Factory (Arrange)
    // 
    String[] lines = new String[]{"WormID = 1; Name = Worm1; GroupID = 1; HeadSize = 3; Speed = 1; BodyLength = 4; Body = [1,5][2,5][2,4][2,3]"};
    IWorm[] worms = new IWorm[lines.length];
    writeInConfigFile(lines);
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
    // Assert
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
    // Move Worm
    // 
    for (int j = 0; j < worms.length; j++) {
      worms[j].move(Direction.Ahead);
      worms[j].updateWorldUnderWorm();
    }
    //
    // Assert
    //
    for(int x = 0; x < INIT_WORLD_VALUES.length; x++){
      for(int y = 0; y < INIT_WORLD_VALUES[x].length; y++){
        // body coordinates
        if(((x == 0) && (y==5)) || ((x == 1) && (y==5)) || ((x == 2) && (y==5)) || ((x == 2) && (y==4))
            //head coordinates
            || ((x == 0) && (y==3)) 
            || ((x == 0) && (y==4))|| ((x == 4) && (y==4)) 
            || ((x == 0) && (y==5))|| ((x == 4) && (y==5)) || ((x == 3) && (y==5))
            || ((x == 0) && (y==6))|| ((x == 4) && (y==6))
            || ((x == 0) && (y==7)) )
          Assert.assertNotSame(String.format("(%d, %d)", x, y), null, world.getNode(x, y).getWorm());
        else
          Assert.assertEquals(String.format("(%d, %d)", x, y), null, world.getNode(x, y).getWorm());
      }
    }

  }
}
