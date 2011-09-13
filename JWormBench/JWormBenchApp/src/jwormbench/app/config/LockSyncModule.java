package jwormbench.app.config;

import jwormbench.factories.IStepFactory;
import jwormbench.sync.lock.LockStepCrossingoverFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class LockSyncModule extends AbstractModule{
  @Override
  protected void configure() {
    bind(IStepFactory.class)
    .to(LockStepCrossingoverFactory.class)
    .in(Singleton.class); 
  }
}
