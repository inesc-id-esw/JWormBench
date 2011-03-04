package jwormbench.test;

import org.junit.Test;

import junit.framework.Assert;
import jwormbench.core.IWorld;
import jwormbench.core.INode;
import jwormbench.defaults.World;
import jwormbench.defaults.DefaultNodeFactory;
import jwormbench.defaults.DefaultCoordinateFactory;
import jwormbench.factories.INodeFactory;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.setup.IWorlSetup;

public class BenchWorldTests {
	private final static int [][] INIT_VALUES = {
		{1, 2, 3, 4, 5},
		{1, 2, 3, 4, 5},
		{1, 2, 3, 4, 5}};
	
	private static class ConstBenchWorldSetup implements IWorlSetup{
		public INode[][] loadWorld() {
		  INodeFactory nodeFac = new DefaultNodeFactory();
			INode[][] world = new INode[INIT_VALUES.length][INIT_VALUES[0].length];
			for (int i = 0; i < world.length; i++) {
				for (int j = 0; j < world[i].length; j++) {
					world[i][j] = nodeFac.make(INIT_VALUES[i][j]);
				}
			}
			return world;
		}
		
	} 	
	@Test
	public void testBenchWorld(){
	  ICoordinateFactory coordFac = new DefaultCoordinateFactory();
		IWorld world = new World(new ConstBenchWorldSetup());
		for (int i = 0; i < world.getRowsNum(); i++) {
			for (int j = 0; j < world.getColumnsNum(); j++) {
				int expected = INIT_VALUES[i][j];
				int actual = world.getNode(i, j).getValue();
				Assert.assertEquals(expected , actual);
				Assert.assertEquals(expected , world.getNode(coordFac.make(i, j)).getValue());
			}
			
		}
	}
}
