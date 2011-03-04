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
package jwormbench.app;

import java.util.logging.Logger;

import org.deuce.transaction.ContextDelegator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import jwormbench.app.config.BenchWithoutSync;
import jwormbench.app.config.BoostSyncModule;
import jwormbench.app.config.DeuceSyncModule;
import jwormbench.app.config.FinelockSyncModule;
import jwormbench.app.config.JvstmSyncModule;
import jwormbench.app.config.LockSyncModule;
import jwormbench.app.config.ArtOfTmContentionManagerModule;
import jwormbench.app.config.ArtOfTmFreeSyncModule;
import jwormbench.app.config.ArtOfTmLockSyncModule;
import jwormbench.app.config.TinyTmFreeSyncModule;
import jwormbench.app.config.TinyTmLockSyncModule;
import jwormbench.core.WormBench;

public class ConsoleApp {
  private static final String OPERATIONS_FILENAME_PATTERN = "config/%d_ops_%d%%writes.txt";
  private static final String WORLD_FILENAME_PATTERN = "config/%d.txt";
  private static final String WORMS_FILENAME_PATTERN = "config/W-B[1.1]-H[%s]-%d.txt";
  private static final String NEW_LINE = System.getProperty("line.separator");
  
  private static void printArguments(
      Logger logger, 
      int nrOfIterations, 
      int nrOfThreads, 
      int wRate, 
      int nrOfOperations,
      String syncStat, 
      int worldSize, 
      String headSize)
  {
    logger.info("------------------------ ARGS ----------------" + NEW_LINE);
    String logMessage = String.format(
        "sync strategy= %s,\n" +
        "threadsNum = %d,\n" +
        "iterations = %d,\n" + 
        "world size = %d,\n" +
        "head size = %s\n" + 
        "rw trx rate = %d\n" +
        "nr of operations = %d" ,
        syncStat, nrOfThreads, nrOfIterations, worldSize, headSize, wRate, nrOfOperations);
    logger.info(logMessage + NEW_LINE);    
    logger.info("----------------------------------------------" + NEW_LINE);
  }
  
  public static void main(String[] args) throws InterruptedException {
    String[] optionalArguments = {
        "-iterations = 1",
        "-threads = 2",
        "-timeout = 0", 
        "-head = 2.16",
        "-world = 512",
        "-wRate = 51",
        "-nrOperations = 1920",
        "-sync = deuce" //none | jvstm | lock | artof-free | artof-lock | tiny-free | tiny-lock | deuce
        };
    CommandLineArgumentParser.DefineOptionalParameter(optionalArguments);
    try{
      CommandLineArgumentParser.ParseArguments(args);
    }catch(CommandLineArgumentException e){
      e.printStackTrace();
    }
    String syncStat = CommandLineArgumentParser.GetParamValue("-sync");
    final int nrOfIterations = Integer.parseInt(CommandLineArgumentParser.GetParamValue("-iterations"));
    final int nrOfThreads = Integer.parseInt(CommandLineArgumentParser.GetParamValue("-threads"));
    final int timeOut = Integer.parseInt(CommandLineArgumentParser.GetParamValue("-timeout"));
    final int wRate = Integer.parseInt(CommandLineArgumentParser.GetParamValue("-wRate"));
    final int nrOperations = Integer.parseInt(CommandLineArgumentParser.GetParamValue("-nrOperations"));
    final int worldSize = Integer.parseInt(CommandLineArgumentParser.GetParamValue("-world"));
    final String headSize = CommandLineArgumentParser.GetParamValue("-head");
    final String configWorms = String.format(WORMS_FILENAME_PATTERN, headSize, worldSize);
    final String configWorld= String.format(WORLD_FILENAME_PATTERN, worldSize);
    final String configOperations = String.format(OPERATIONS_FILENAME_PATTERN, nrOperations, wRate);
    //
    // Choose synchronization strategy
    //
    WormBench benchWarmUp, benchRollout = null;
    Logger logger = null;
    if(syncStat.equals("deuce")){
      syncStat += ": " + ContextDelegator.getInstance().getClass().getName();
      //
      // DeuceSTM corrupts all constructors and damage Guice functionality,
      // then we can not use a regular Guice module. :-p
      //
      benchWarmUp = DeuceSyncModule.configure(1,2,0,configWorms,configWorld,configOperations);
      benchRollout = DeuceSyncModule.configure(nrOfIterations,nrOfThreads,timeOut,configWorms,configWorld,configOperations);
      logger = DeuceSyncModule.getLogger();
    }else{
      //
      // Configure via Guice
      //
      Module configModule = new BenchWithoutSync(
          nrOfIterations,
          nrOfThreads,
          timeOut,        
          configWorms,
          configWorld,
          configOperations
      );
      Module warmConfigModule = new BenchWithoutSync(1,2,0,configWorms,configWorld,configOperations);
      if(syncStat.equals("lock")){
        configModule = Modules.override(configModule).with(new LockSyncModule());
        warmConfigModule = Modules.override(warmConfigModule ).with(new LockSyncModule());
      }else if(syncStat.equals("jvstm")){
        configModule = Modules.override(configModule).with(new JvstmSyncModule());
        warmConfigModule = Modules.override(warmConfigModule ).with(new JvstmSyncModule());
      }else if(syncStat.equals("artof-free")){
        artof.core.Defaults.setModule(new ArtOfTmContentionManagerModule(1, 10));
        configModule = Modules.override(configModule).with(new ArtOfTmFreeSyncModule());
        warmConfigModule = Modules.override(warmConfigModule).with(new ArtOfTmFreeSyncModule());
      }else if(syncStat.equals("artof-lock")){
        // não usa o ContentionManager
        configModule = Modules.override(configModule).with(new ArtOfTmLockSyncModule());
        warmConfigModule = Modules.override(warmConfigModule ).with(new ArtOfTmLockSyncModule());
      }else if(syncStat.equals("tiny-free")){
        configModule = Modules.override(configModule).with(new TinyTmFreeSyncModule());
        warmConfigModule = Modules.override(warmConfigModule ).with(new TinyTmFreeSyncModule());
      }else if(syncStat.equals("tiny-lock")){
        configModule = Modules.override(configModule).with(new TinyTmLockSyncModule());
        warmConfigModule = Modules.override(warmConfigModule ).with(new TinyTmLockSyncModule());
      }else if(syncStat.equals("boost")){
        configModule = Modules.override(configModule).with(new BoostSyncModule());
        warmConfigModule = Modules.override(warmConfigModule).with(new BoostSyncModule());
      }if(syncStat.equals("finelock")){
        configModule = Modules.override(configModule).with(new FinelockSyncModule());
        warmConfigModule = Modules.override(warmConfigModule).with(new FinelockSyncModule());
      }       
      Injector injector = Guice.createInjector(configModule );
      Injector warmInjector = Guice.createInjector(warmConfigModule);
      benchRollout = injector.getInstance(WormBench.class);
      benchWarmUp = warmInjector.getInstance(WormBench.class);
      logger = injector.getInstance(Logger.class);
    }
    //
    // WarmUp 
    //
    printArguments(logger, nrOfIterations, nrOfThreads, wRate, nrOperations, syncStat, worldSize, headSize);
    logger.info("Warming up..." + NEW_LINE);
    benchWarmUp.RunBenchmark(syncStat );
    logger.info("Warm Up Finish!" + NEW_LINE);
    //
    // Run 
    // 
    benchRollout.RunBenchmark(syncStat);
    benchRollout.LogExecutionTime();
    benchRollout.LogConsistencyVerification();    
  }
}
