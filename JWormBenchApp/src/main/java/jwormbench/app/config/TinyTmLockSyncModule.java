package jwormbench.app.config;

import jwormbench.factories.INodeFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.sync.tinytm.TinyTmLockBenchNodeFactory;
import jwormbench.sync.tinytm.TinyTmStepFactory;

import TinyTM.TThread;
import TinyTM.locking.OnAbort;
import TinyTM.locking.OnCommit;
import TinyTM.locking.OnStart;
import TinyTM.locking.OnValidate;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class TinyTmLockSyncModule extends AbstractModule{
  @Override
  protected void configure() {
    //
    // Steps and nodes
    //
    bind(IStepFactory.class)
    .to(TinyTmStepFactory.class)
    .in(Singleton.class);
    bind(INodeFactory.class)
    .to(TinyTmLockBenchNodeFactory.class)
    .in(Singleton.class);
    //
    // Register handlers on TThread
    //
    TThread.onAbort(new OnAbort());
    TThread.onCommit(new OnCommit());
    TThread.onStart(new OnStart());
    TThread.onValidate(new OnValidate());
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
