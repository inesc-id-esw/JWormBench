package jwormbench.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;
import jwormbench.core.IBenchWorldNode;
import jwormbench.defaults.DefaultBenchWorldNodeFactory;
import jwormbench.exceptions.BenchWorldLoadingFileException;
import jwormbench.setup.BenchWorldFileLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BenchWorldFileLoaderUT {
	static final  String CONFIG_FILE_NAME = "BenchWorldFileLoaderUT_config.txt"; 
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
	public void testFileNotFoundExcpetion(){
		BenchWorldFileLoader loader = new BenchWorldFileLoader("oliolanaoestaca.txt", new DefaultBenchWorldNodeFactory());
		try{
			loader.loadWorld();
		}catch(BenchWorldLoadingFileException e){
			if(e.getCause().getClass() == FileNotFoundException.class)
				Assert.assertTrue(true);
				return;
		}
		Assert.assertFalse(true);
	}
	@Test(expected = BenchWorldLoadingFileException.class)
	public void testInvalidFormatInConfigFile() throws IOException{
		String[] lines = new String[]{"hgjkgkg", "kkhhkh"}; // does not contains an 'x'
		writeInConfigFile(lines);
		BenchWorldFileLoader loader = new BenchWorldFileLoader(CONFIG_FILE_NAME, new DefaultBenchWorldNodeFactory());
		loader.loadWorld();
	}
	@Test
	public void testValidConfigFile() throws IOException{
		String[] lines = new String[]{
				"4x8", 
				"1 2 3 4 5 6 7 8", // Sum = 36
				"1 2 3 4 5 6 7 8",
				"1 2 3 4 5 6 7 8",
				"1 2 3 4 5 6 7 8"};
		writeInConfigFile(lines); // Total = 144;
		BenchWorldFileLoader loader = new BenchWorldFileLoader(CONFIG_FILE_NAME, new DefaultBenchWorldNodeFactory());
		IBenchWorldNode[][] world = loader.loadWorld();
		Assert.assertEquals(4, world.length);
		Assert.assertEquals(8, world[0].length);
		int sum = 0;
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[i].length; j++) {
				sum += world[i][j].getValue(); 
			}
		}
		Assert.assertEquals(144, sum);
	}
	@Test(expected = BenchWorldLoadingFileException.class)
	public void testNotEnoughElementsInRow() throws IOException{
		String[] lines = new String[]{
				"4x8", 
				"1 2 3 4 5 6", 
				"1 2 3 4 5 ",
				"1 2 3 4 5 6 7 8",
				"1 2 3 4 5 6 7 8"};
		writeInConfigFile(lines); 
		BenchWorldFileLoader loader = new BenchWorldFileLoader(CONFIG_FILE_NAME, new DefaultBenchWorldNodeFactory());
		loader.loadWorld();
	}
}
