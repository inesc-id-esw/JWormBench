package jwormbench.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;
import jwormbench.core.Direction;
import jwormbench.core.AbstractStep;
import jwormbench.core.IStep;
import jwormbench.defaults.DefaultOperationFactory;
import jwormbench.defaults.DefaultStepFactory;
import jwormbench.exceptions.OperationsLoadingFileException;
import jwormbench.factories.IStepFactory;
import jwormbench.setup.StepsFileLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class OperationsdFileLoaderTests {
	static final  String CONFIG_FILE_NAME = "config/OperationsFileLoaderUT_config.txt"; 
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
	public void testFileNotFoundExcpetion(){
	  IStepFactory opFac= new DefaultStepFactory(new StepsFileLoader("oliolanaoestaca.txt"), new DefaultOperationFactory(null));
		try{
		  opFac.make();
		}catch(OperationsLoadingFileException e){
			if(e.getCause().getClass() == FileNotFoundException.class)
				Assert.assertTrue(true);
				return;
		}
		Assert.assertFalse(true);
	}
	@Test(expected = NumberFormatException.class)
	public void testInvalidFormatInConfigFile() throws IOException{
		String[] lines = new String[]{"hgjkgkg", "kkhhkh"}; // does not contains an '-' 
		writeInConfigFile(lines);
		IStepFactory opFac= new DefaultStepFactory(new StepsFileLoader(CONFIG_FILE_NAME), new DefaultOperationFactory(null));
		opFac.make();
	}
	@Test
	public void testValidConfigFile() throws IOException{
		String[] lines = new String[]{
				"1 - 0",
				"2 - 1",
				"3 - 2",
        "4 - 0",
        "5 - 1",
        "6 - 2",
        ""};
		writeInConfigFile(lines);
		IStepFactory opFac= new DefaultStepFactory(new StepsFileLoader(CONFIG_FILE_NAME), new DefaultOperationFactory(null));
    List<IStep> ops = opFac.make();
		Assert.assertEquals(6, ops.size());
		Assert.assertEquals(Direction.Ahead, ops.get(0).getDirection());
		Assert.assertEquals(Direction.Right, ops.get(1).getDirection());
		Assert.assertEquals(Direction.Left, ops.get(2).getDirection());
		Assert.assertEquals(Direction.Ahead, ops.get(3).getDirection());
		Assert.assertEquals(Direction.Right, ops.get(4).getDirection());
		Assert.assertEquals(Direction.Left, ops.get(5).getDirection());
	}
}
