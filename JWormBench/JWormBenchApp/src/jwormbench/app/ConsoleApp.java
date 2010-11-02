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
import jwormbench.core.WormBench;

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
        "-sync = jvstm" //none | jvstm || lock
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
    //
    // Run 
    // 
    Injector injector = Guice.createInjector(configModule );
    WormBench bench = injector.getInstance(WormBench.class);
    bench.RunBenchmark();
    bench.LogExecutionTime();
    bench.LogConsistencyVerification();
    
    // CONTABILIZAR ABORTS
    // 
    // Colocar menos operações de RW e mais R.
    // 
    // TESTAR OUTRA VERSAO DA JVSTM
    // ---> Estou sem as expeculativas a funcionar.
    // ---> Não estou a dar informação de ReadOnly ou Não. => Posso aproveitar isso.
  }
}
