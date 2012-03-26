package jwormbench.app.config;

import jwormbench.factories.INodeFactory;
import jwormbench.factories.IStepFactory;
 

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Depends on project jvstm-doublelayout-v3 - version of Multiprog12 - replicate via array. 
 * @author mcarvalho
 *
 */
public class JvstmAomSyncModule extends AbstractModule{
  @Override
  protected void configure() {
    bind(IStepFactory.class)
    .to(jwormbench.sync.jvstm.aom.JvstmStepFactory.class)
    .in(Singleton.class);
    bind(INodeFactory.class)
    .to(jwormbench.sync.jvstm.aom.TransactionalNodeFactory.class)
    .in(Singleton.class);
  }
}
