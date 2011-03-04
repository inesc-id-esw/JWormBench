/*
 * JWormBench: a Java benchmark based on WormBench - a synthetic 
 * workload for Transactinal Memory Systems Center www.bscmsrc.eu.
 * Copyright (C) 2010 INESC-ID Software Engineering Group
 * http://www.esw.inesc-id.pt
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author's contact:
 * INESC-ID Software Engineering Group
 * Rua Alves Redol 9
 * 1000 - 029 Lisboa
 * Portugal
 */
package jwormbench.setup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import com.google.inject.Inject;

import jwormbench.config.params.WormsConfigFile;
import jwormbench.core.ICoordinate;
import jwormbench.exceptions.WormParseException;
import jwormbench.exceptions.WormsLoadingFileException;
import jwormbench.factories.ICoordinateFactory;

/**
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt
 */
public class WormsFileLoader implements IWormsSetup{
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private String wormsConfigFile;
  private ICoordinateFactory coordFac;
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Inject
  public WormsFileLoader(@WormsConfigFile String wormsConfigFile, ICoordinateFactory coordFac) {
    super();
    this.wormsConfigFile = wormsConfigFile;
    this.coordFac = coordFac;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ----------------------- METHODS ------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /* (non-Javadoc)
   * @see jwormbench.setup.IWormsSetup#iterator()
   */
  @Override
  public Iterator<IWormsSetup.WormProperties> iterator() {
    try {
      return new WormFileIterator(wormsConfigFile);
    } catch (FileNotFoundException e) {
      throw new WormsLoadingFileException(e);
    } catch (IOException e) {
      throw new WormsLoadingFileException(e);
    }
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------- AUXILIAR FUNCTIONS ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * The worm is defined as
   * WormID = 1; Name = Worm1; GroupID = 1; HeadSize = 2; Speed = 1; Body = [23,1][23,2][23,3][24,3]
   * Where ";" is a delimiter. 
   */
  private WormProperties parseWormConfigstr(String configStr){
    int id, headSize, speed, groupId, bodyLength;
    String name;
    ICoordinate[] body, head;
    String [] configStrTokens = configStr.split(";");
    String strId = getConfigValue(configStrTokens, "WormID");

    if (strId != null && strId.length() > 0){
      id = Integer.parseInt(strId);
    } else{
      throw new WormParseException(String.format("Worm ID not defined", configStr)); 
    }

    String strGroupId = getConfigValue(configStrTokens, "GroupID");
    if (strGroupId != null && strGroupId.length() > 0){
      groupId = Integer.parseInt(strGroupId);
    }else{
      throw new WormParseException(String.format("Worm GroupID not defined", configStr));
    }

    name = getConfigValue(configStrTokens, "Name");

    String strHeadSize = getConfigValue(configStrTokens, "HeadSize");
    if (strHeadSize != null && strHeadSize.length() > 0){
      headSize = Integer.parseInt(strHeadSize);
      head = new ICoordinate[headSize*headSize];
      for (int i = 0; i < head.length; i++) {
        head[i] = coordFac.make(0, 0);
      }
    }
    else{
      throw new WormParseException(String.format("Worm HeadSize not defined", configStr));
    }

    String strSpeed = getConfigValue(configStrTokens, "Speed");
    if (strSpeed != null && strSpeed.length() > 0){
      speed = Integer.parseInt(strSpeed);
    }
    else{
      throw new WormParseException(String.format("Worm Speed not defined", configStr));
    }

    String bodyLengthStr = getConfigValue(configStrTokens, "BodyLength");
    if (bodyLengthStr != null && bodyLengthStr.length() > 0){
      bodyLength = Integer.parseInt(bodyLengthStr);
    }
    else{
      throw new WormParseException(String.format("Worm BodyLength not defined", configStr));
    }
    String strBody = getConfigValue(configStrTokens, "Body");
    if (strBody != null && strBody.length() > 0){
      String[] tokens = strBody.split("]");
      if (bodyLength == 1){
        //
        // If the body length is 1 we yet create one more cell
        // that we later use to compute the orientation of the worm.
        //
        body = new ICoordinate[2];
      }
      else{
        body = new ICoordinate[bodyLength];
      }

      if ((tokens.length) != body.length){
        throw new WormParseException(
            String.format("Worm BodyLength not given body coordinates does not match", 
                configStr));
      }

      for (int i = 0; i < body.length; i++){
        tokens[i] = tokens[i].replace("[", "");
        String[] strCoordinate = tokens[i].split(",");

        body[i] = coordFac.make(
            Integer.parseInt(strCoordinate[0]),
            Integer.parseInt(strCoordinate[1]));
      }
    }
    else{
      throw new WormParseException(String.format("Worm Body not defined", configStr));
    }
    return new WormProperties(id, headSize, speed, groupId, bodyLength, name, body, head);
  }
  /**
   * Auxiliary function.
   * @param configStrTokens
   * @param configParamName
   * @return A string value for parameter configParamName in configStrTokens. 
   */
  private static String getConfigValue(String [] configStrTokens, String configParamName){
    configParamName = configParamName.toLowerCase();
    String value = null;
    boolean found = false;
    int lineNo = 0;
    while ((!found) && (lineNo < configStrTokens.length)){
      if (configStrTokens[lineNo].indexOf(configParamName) >= 0){
        String[] tokens = configStrTokens[lineNo].split("=");
        if (tokens.length == 2 && tokens[0].trim().equals(configParamName)){
          found = true;
          value = tokens[1].trim();
        }
      }
      lineNo++;
    }
    return value;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------- NESTED TYPES  ---------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  public class WormFileIterator implements Iterator<WormsFileLoader.WormProperties>, IDisposable{
    BufferedReader reader;
    String line = null;
    public WormFileIterator(String wormsConfigFile) throws IOException{
      reader = new BufferedReader(new FileReader(wormsConfigFile));
      do{ 
        line = reader.readLine();
      }while(line != null && line.charAt(0) == '#');
    }
    @Override
    public boolean hasNext() {
      return line != null;
    }
    @Override
    public WormProperties next() {
      line = line.trim().toLowerCase();
      WormProperties worm = parseWormConfigstr(line);
      do{ 
        try {
          line = reader.readLine();
        } catch (IOException e) {
          throw new WormsLoadingFileException(e);
        }
      }while(line != null && line.charAt(0) == '#');
      return worm;
    }
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
    @Override
    public void dispose() {
      try {
        reader.close();
      } catch (IOException e) {
        throw new WormsLoadingFileException(e);
      }
    }
  }
}
