package jwormbench.app.config;

import jvstm.Transaction;
import jwormbench.factories.INodeFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.sync.jvstm.JvstmStepFactory;
import jwormbench.sync.jvstmdbl.NodeDoubleLayoutFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class JvstmDblLayoutSyncModule extends AbstractModule{
  static{
    //
    // Print number of aborted transactions
    // 
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("Nr of aborted trxs: " + Transaction.nrOfAborts);;
      }
    });
  }
  @Override
  protected void configure() {
    bind(IStepFactory.class)
    .to(JvstmStepFactory.class)
    .in(Singleton.class);
    bind(INodeFactory.class)
    .to(NodeDoubleLayoutFactory.class)
    .in(Singleton.class);
  }
}
