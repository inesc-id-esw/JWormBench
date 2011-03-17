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

import com.google.inject.AbstractModule;
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

  
  private static void printUsage() {
    System.out.println("USAGE:");
    System.out.println("java jwormbench.app.ConsoleApp");
    System.out.print(" -threads <int>");
    System.out.print(" -iterations <int>");
    System.out.print(" -world <int>");
    System.out.print(" -head <string>");
    System.out.print(" -wRate <string>");
    System.out.print(" -nrOperations <int>");
    System.out.print(" -timeout <>"); 
    System.out.println(" -sync <string>)");
    System.out.println();
    System.out.println("OPTIONS");
    System.out.println(" - threads:  the number of worker threads (1 by default)");
    System.out.println(" - iterations: the size of the workload is equals to the ‘number of iterations’ times the ‘number of operations’ (1 by default)");
    System.out.println(" - world: the world’s size (512 by default)");
    System.out.println(" - head: the size of worms’ head. The number of nodes under the head of the worm is equal to the square of his head’s size.('2.16' by default, meaning the head’s size is between 2 and 16, corresponding to a number of nodes between 4 and 256 nodes)");
    System.out.println(" - wRate: label for the name of the worm operations configuration file. This file’s name has the following form: < nrOperations>_ops_<wRate>%writes.txt (21 by default, corresponding to an updates rate of 20% and configuration #1)");
    System.out.println(" - nrOperations: number of operations performed per iteration. This number also determines the name of the worm operations configuration file (1920 by default)");
    System.out.println(" - timeout: if zero the benchmark just finishes when it completes the total workload (0 by default);");
    System.out.println(" - sync: the name of a class that defines a Guice module or one of the built-in synchronization strategies: jvstm | lock | finelock | deuce | artof-free | artof-lock | tiny-free | tiny-lock (none by default)");
  }  
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
  
  public static void main(String[] args) throws InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    String[] optionalArguments = {
        "-iterations = 1",
        "-threads = 2",
        "-timeout = 0", 
        "-head = 2.16",
        "-world = 512",
        "-wRate = 21",
        "-nrOperations = 1920",
        "-sync = none" //none | jvstm | lock | fine-lock | deuce | artof-free | artof-lock | tiny-free | tiny-lock
        };
    CommandLineArgumentParser.DefineOptionalParameter(optionalArguments);
    try{
      CommandLineArgumentParser.ParseArguments(args);
    }catch(CommandLineArgumentException e){
      System.out.println("ERROR parsing arguments: " + e.getMessage());
      printUsage();
      System.exit(1);
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
      }else if(syncStat.equals("finelock")){
        configModule = Modules.override(configModule).with(new FinelockSyncModule());
        warmConfigModule = Modules.override(warmConfigModule).with(new FinelockSyncModule());
      }else{
        System.err.println("Unrecognized sync strategy. Will try to load a Java Class with that name as a Guice module.");
        AbstractModule module = (AbstractModule) Class.forName(syncStat).newInstance();
        configModule = Modules.override(configModule).with(module);
        warmConfigModule = Modules.override(warmConfigModule).with(module);
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
