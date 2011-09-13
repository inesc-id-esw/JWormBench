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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import jwormbench.app.config.BenchWithoutSync;
import jwormbench.app.config.JvstmSyncModule;
import jwormbench.app.config.LockSyncModule;
import jwormbench.app.config.ArtOfTmContentionManagerModule;
import jwormbench.app.config.ArtOfTmFreeSyncModule;
import jwormbench.app.config.ArtOfTmLockSyncModule;
import jwormbench.app.config.TinyTmFreeSyncModule;
import jwormbench.app.config.TinyTmLockSyncModule;
import jwormbench.core.WormBench;
import jwormbench.factories.IBenchWorldNodeFactory;

public class ConsoleApp {
  public static void main(String[] args) throws InterruptedException {
    String[] optionalArguments = {
        "-iterations = 32",
        "-threads = 2",
        "-timeout = 0", 
        "-worms = config/genome_W-B[1.1]-H[16.16]-52.txt",
        //"-worms = config/genome_52.txt",
        "-world = config/1024.txt",
        "-operations = config/1000_ops_RO_dominated.txt",
        //"-operations = config/genome_1000_ops.txt");
        "-sync = jvstm" //none | jvstm | lock | artof-free | artof-lock | tiny-free | tiny-lock
        };
    CommandLineArgumentParser.DefineOptionalParameter(optionalArguments);
    try{
      CommandLineArgumentParser.ParseArguments(args);
    }catch(CommandLineArgumentException e){
      e.printStackTrace();
    }
    Module configModule = new BenchWithoutSync(
        Integer.parseInt(CommandLineArgumentParser.GetParamValue("-iterations")),
        Integer.parseInt(CommandLineArgumentParser.GetParamValue("-threads")),
        Integer.parseInt(CommandLineArgumentParser.GetParamValue("-timeout")),        
        CommandLineArgumentParser.GetParamValue("-worms"),
        CommandLineArgumentParser.GetParamValue("-world"),
        CommandLineArgumentParser.GetParamValue("-operations")
    );
    //
    // Choose synchronization strategy
    //
    if(CommandLineArgumentParser.GetParamValue("-sync").equals("lock"))
      configModule = Modules.override(configModule).with(new LockSyncModule());
    else if(CommandLineArgumentParser.GetParamValue("-sync").equals("jvstm"))
      configModule = Modules.override(configModule).with(new JvstmSyncModule());
    else if(CommandLineArgumentParser.GetParamValue("-sync").equals("artof-free")){
      artof.core.Defaults.setModule(new ArtOfTmContentionManagerModule(1, 10));
      configModule = Modules.override(configModule).with(new ArtOfTmFreeSyncModule());
    }else if(CommandLineArgumentParser.GetParamValue("-sync").equals("artof-lock")){
      // não usa o ContentionManager
      configModule = Modules.override(configModule).with(new ArtOfTmLockSyncModule());
    }else if(CommandLineArgumentParser.GetParamValue("-sync").equals("tiny-free")){
      configModule = Modules.override(configModule).with(new TinyTmFreeSyncModule());
    }else if(CommandLineArgumentParser.GetParamValue("-sync").equals("tiny-lock")){
      configModule = Modules.override(configModule).with(new TinyTmLockSyncModule());
    }
    //
    // Run 
    // 
    Injector injector = Guice.createInjector(configModule );
    WormBench bench = injector.getInstance(WormBench.class);
    String syncStat = injector.getInstance(IBenchWorldNodeFactory.class).getClass().getSimpleName();
    bench.RunBenchmark(syncStat );
    bench.LogExecutionTime();
    bench.LogConsistencyVerification();    
  }
}
