package jwormbench.app.config;

import jvstm.dblcore.Transaction;
import jwormbench.factories.INodeFactory;
import jwormbench.factories.IStepFactory;
 

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class JvstmDblLayoutSyncModule extends AbstractModule{
  @Override
  protected void configure() {
    bind(IStepFactory.class)
    .to(jwormbench.sync.jvstmdbl.JvstmStepFactory.class)
    .in(Singleton.class);
    bind(INodeFactory.class)
    .to(jwormbench.sync.jvstmdbl.NodeDoubleLayoutFactory.class)
    .in(Singleton.class);
  }
}
