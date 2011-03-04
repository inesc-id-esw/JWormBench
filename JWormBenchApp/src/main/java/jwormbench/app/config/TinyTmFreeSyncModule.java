package jwormbench.app.config;

import jwormbench.factories.INodeFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.sync.tinytm.TinyTmFreeBenchNodeFactory;
import jwormbench.sync.tinytm.TinyTmStepFactory;

import TinyTM.TThread;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class TinyTmFreeSyncModule extends AbstractModule{
  @Override
  protected void configure() {
    //
    // Steps and nodes
    //
    bind(IStepFactory.class)
    .to(TinyTmStepFactory.class)
    .in(Singleton.class);
    bind(INodeFactory.class)
    .to(TinyTmFreeBenchNodeFactory.class)
    .in(Singleton.class);
    //
    // Print number of aborted transactions
    // 
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("Nr of aborted trxs: " + TThread.aborts.get());;
      }
    });

  }
}
