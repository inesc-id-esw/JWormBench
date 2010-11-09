package jwormbench.app.config;

import artof.core.Transaction;
import jwormbench.factories.IBenchWorldNodeFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.sync.artof.ArtOfTmLockBenchNodeFactory;
import jwormbench.sync.artof.ArtOfTmStepFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ArtOfTmLockSyncModule extends AbstractModule{
  @Override
  protected void configure() {
    //
    // Steps and nodes
    //
    bind(IBenchWorldNodeFactory.class)
    .to(ArtOfTmLockBenchNodeFactory.class)
    .in(Singleton.class);
    bind(IStepFactory.class)
    .to(ArtOfTmStepFactory.class)
    .in(Singleton.class);
    //
    // Print number of aborted transactions
    // 
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("Nr of aborted trxs: " + Transaction.nrOfAborts);;
      }
    });
  }
}
