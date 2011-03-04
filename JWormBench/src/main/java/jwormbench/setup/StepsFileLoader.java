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

import jwormbench.config.params.StepsConfigFile;
import jwormbench.core.Direction;
import jwormbench.core.OperationKind;
import jwormbench.exceptions.OperationsLoadingFileException;

/**
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt
 */
public class StepsFileLoader implements IStepSetup{
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private String configFile;
 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Inject
  public StepsFileLoader(@StepsConfigFile String configFile) {
    super();
    this.configFile = configFile;
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ----------------------- METHODS ------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /* (non-Javadoc)
   * @see jwormbench.setup.IOperationsSetup#iterator()
   */
  public Iterator<OperationProperties> iterator() {
    try {
      return new OperationsFileIterator(configFile);
    } catch (FileNotFoundException e) {
      throw new OperationsLoadingFileException(e);
    } catch (IOException e) {
      throw new OperationsLoadingFileException(e);
    }
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------- NESTED TYPES  ---------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  public class OperationsFileIterator implements Iterator<StepsFileLoader.OperationProperties>, IDisposable{
    BufferedReader reader;
    String line = null;
    public OperationsFileIterator(String wormsConfigFile) throws IOException{
      reader = new BufferedReader(new FileReader(wormsConfigFile));
      do{ 
        line = reader.readLine();
      }while(line != null && !line.isEmpty() && line.charAt(0) == '#');
    }
    @Override
    public boolean hasNext() {
      return line != null && !line.isEmpty();
    }
    @Override
    public OperationProperties next() {
      line = line.trim().toLowerCase();
      String[] tokens = line.split("-");
      if (tokens == null || tokens.length != 2){
          throw new NumberFormatException(
              String.format("Parsing operations. Expected OperationType - Cidrection. Filename: %s", 
              configFile));
      }
      tokens[0] = tokens[0].trim();
      tokens[1] = tokens[1].trim();
      do{ 
        try {
          line = reader.readLine();
        } catch (IOException e) {
          throw new OperationsLoadingFileException(e);
        }
      }while(line != null && !line.isEmpty() && line.charAt(0) == '#');
      return new OperationProperties(
          OperationKind.values()[Integer.parseInt(tokens[0])],
          Direction.values()[Integer.parseInt(tokens[1])]);
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
        throw new OperationsLoadingFileException(e);
      }
    }
  }
}
