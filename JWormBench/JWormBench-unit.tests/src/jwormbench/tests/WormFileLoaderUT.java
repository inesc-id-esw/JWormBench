package jwormbench.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;
import jwormbench.core.AbstractWorm;
import jwormbench.core.IBenchWorld;
import jwormbench.core.IBenchWorldNode;
import jwormbench.core.ICoordinate;
import jwormbench.core.IWorm;
import jwormbench.defaults.DefaultCoordinateFactory;
import jwormbench.defaults.DefaultWormFactory;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.setup.IWormsSetup;
import jwormbench.setup.WormsFileLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WormFileLoaderUT {
  static final  String CONFIG_FILE_NAME = "WormFileLoaderUT_config.txt"; 
  File configFile;
  
  @Before
  public void setupTest() throws IOException{
    configFile = new File(CONFIG_FILE_NAME);
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
  public void testValidConfigFile() throws IOException{
    int NR_OF_WORMS = 3;
    String[] lines = new String[]{
        "WormID = 1; Name = Worm1; GroupID = 1; HeadSize = 4; Speed = 1; BodyLength = 4; Body = [200,244][200,243][199,243][198,243]",
        "WormID = 13; Name = Worm13; GroupID = 13; HeadSize = 1; Speed = 1; BodyLength = 1; Body = [26,69][27,69]",
        "WormID = 6; Name = Worm6; GroupID = 6; HeadSize = 8; Speed = 1; BodyLength = 3; Body = [158,111][158,112][158,111]"
        };
    writeInConfigFile(lines);
    ICoordinateFactory cordFac =  new DefaultCoordinateFactory();
    IWormsSetup wormsSetup = new WormsFileLoader(CONFIG_FILE_NAME, cordFac);
    AbstractWorm[] worms = new AbstractWorm[NR_OF_WORMS];
    IBenchWorld world = new IBenchWorld() {
      private final IBenchWorldNode node = new IBenchWorldNode() {
        public void setWorm(IWorm worm) {}
        public void setValue(int value) {}
        public IWorm getWorm() {return null;}
        public int getValue() {return 0;}
      };
      public int getRowsNum() {return 0;}
      public IBenchWorldNode getNode(ICoordinate c) {return node;}
      public IBenchWorldNode getNode(int x, int y) {return node;}
      public int getColumnsNum() {return 0;}
      @SuppressWarnings("unused") public void updateWorldUnderWorm(IWorm w) {throw new UnsupportedOperationException();}
      public int getSumOfAllNodes() {return 0;}
    };
    IWormFactory wormFac = new DefaultWormFactory(cordFac, world, wormsSetup);
    int i = 0;
    for (IWorm w : wormFac.make(NR_OF_WORMS)) {
      worms[i++] = (AbstractWorm) w;
    }
    //
    // Worm1
    //
    Assert.assertEquals(1, worms[0].id);
    Assert.assertEquals(4, worms[0].headSize);
    Assert.assertEquals(1, worms[0].speed);
    Assert.assertEquals(1, worms[0].groupId);
    Assert.assertEquals(4, worms[0].bodyLength);
    Assert.assertEquals("worm1", worms[0].name); // The parser converts name to lower case
    Assert.assertEquals(200, worms[0].getBodyCoordinate(0).getX());
    Assert.assertEquals(244, worms[0].getBodyCoordinate(0).getY());
    Assert.assertEquals(200, worms[0].getBodyCoordinate(1).getX());
    Assert.assertEquals(243 , worms[0].getBodyCoordinate(1).getY());
    Assert.assertEquals(199, worms[0].getBodyCoordinate(2).getX());
    Assert.assertEquals(243, worms[0].getBodyCoordinate(2).getY());
    Assert.assertEquals(198, worms[0].getBodyCoordinate(3).getX());
    Assert.assertEquals(243, worms[0].getBodyCoordinate(3).getY());

    //
    // Worm13
    //
    Assert.assertEquals(13, worms[1].id);
    Assert.assertEquals(1, worms[1].headSize);
    Assert.assertEquals(1, worms[1].speed);
    Assert.assertEquals(13, worms[1].groupId);
    Assert.assertEquals(1, worms[1].bodyLength);
    Assert.assertEquals("worm13", worms[1].name); // The parser converts name to lower case
    Assert.assertEquals(26, worms[1].getBodyCoordinate(0).getX());
    Assert.assertEquals(69, worms[1].getBodyCoordinate(0).getY());
    Assert.assertEquals(27, worms[1].getBodyCoordinate(1).getX());
    Assert.assertEquals(69, worms[1].getBodyCoordinate(1).getY());
    //
    // Worm6
    //
    Assert.assertEquals(6, worms[2].id);
    Assert.assertEquals(8, worms[2].headSize);
    Assert.assertEquals(1, worms[2].speed);
    Assert.assertEquals(6, worms[2].groupId);
    Assert.assertEquals(3, worms[2].bodyLength);
    Assert.assertEquals("worm6", worms[2].name); // The parser converts name to lower case
    Assert.assertEquals(158, worms[2].getBodyCoordinate(0).getX());
    Assert.assertEquals(111, worms[2].getBodyCoordinate(0).getY());
    Assert.assertEquals(158, worms[2].getBodyCoordinate(1).getX());
    Assert.assertEquals(112, worms[2].getBodyCoordinate(1).getY());
    Assert.assertEquals(158, worms[2].getBodyCoordinate(2).getX());
    Assert.assertEquals(111, worms[2].getBodyCoordinate(2).getY());
    
  }
}
