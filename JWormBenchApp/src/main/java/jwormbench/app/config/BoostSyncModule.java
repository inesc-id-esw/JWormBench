package jwormbench.app.config;

import artof.core.Transaction;
import jwormbench.factories.INodeFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.sync.artof.ArtOfTmStepFactory;
import jwormbench.sync.artof.boost.BoostBenchNodeFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class BoostSyncModule extends AbstractModule{
  @Override
  protected void configure() {
    //
    // Steps and nodes
    //
    bind(IStepFactory.class)
      .to(ArtOfTmStepFactory.class)
      .in(Singleton.class);
    bind(INodeFactory.class)
      .to(BoostBenchNodeFactory.class)
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
