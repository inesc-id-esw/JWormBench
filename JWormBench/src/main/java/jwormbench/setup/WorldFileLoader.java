package jwormbench.setup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.inject.Inject;

import jwormbench.config.params.WorldConfigFile;
import jwormbench.core.INode;
import jwormbench.exceptions.BenchWorldLoadingFileException;
import jwormbench.factories.INodeFactory;

public class WorldFileLoader implements IWorlSetup{
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  String configFile;
  INodeFactory nodeFac;
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Inject
  public WorldFileLoader(@WorldConfigFile String configFile, INodeFactory nodeFac) {
    this.configFile = configFile;
    this.nodeFac = nodeFac;
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- METHODS -------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Override
  public INode[][] loadWorld() {
    INode[][] world = null;
    int rowsNum = 0, columnsNum = 0;
    BufferedReader reader = null;
    try {
      InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(configFile);
      if(in == null)
          throw new BenchWorldLoadingFileException(
                  new FileNotFoundException(configFile + " file not found!"));
      reader = new BufferedReader(new InputStreamReader(in));
      String line = reader.readLine();
      line = line.trim();
      if (line != null && line.length() > 0 && line.indexOf('x') >= 0){
        String[] tokens = line.split("x");
        rowsNum = Integer.valueOf(tokens[0]);
        columnsNum = Integer.valueOf(tokens[1]);
        world = new INode[rowsNum][columnsNum];
      } else {
        throw new BenchWorldLoadingFileException(
            String.format("Invalid format in file %s", configFile));
      }
      int index = 0;
      String[] tokens;
      for (index = 0; index < rowsNum; index++){
        line = reader.readLine();
        tokens = line.split(" ");
        int colCounter = 0, colIndex = 0;
        while ((colCounter < columnsNum) && (colIndex < tokens.length)){
          if (tokens[colIndex] != null && tokens[colIndex].length() > 0){
            world[index][colCounter] = nodeFac.make(Integer.valueOf(tokens[colIndex]));
            colCounter++;
          }
          colIndex++;
        }
        if (colCounter < columnsNum){
          throw new BenchWorldLoadingFileException(
              String.format("Not enough matrix elements in row. Config file = %s, Index = %d", 
                  configFile, 
                  index + 2));
        }
      }
    } catch (FileNotFoundException e) {
      throw new BenchWorldLoadingFileException(e);
    } catch (IOException e) {
      throw new BenchWorldLoadingFileException(e);
    }finally{
        try {
          if(reader != null) reader.close();
        } catch (IOException e) {
          throw new BenchWorldLoadingFileException(e);
        }
    }
    return world;
  }

}
