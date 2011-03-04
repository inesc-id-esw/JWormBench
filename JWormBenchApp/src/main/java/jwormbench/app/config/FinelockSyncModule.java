package jwormbench.app.config;

import jwormbench.factories.IStepFactory;
import jwormbench.sync.finelock.FinelockStepFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class FinelockSyncModule extends AbstractModule{
  @Override
  protected void configure() {
    bind(IStepFactory.class)
    .to(FinelockStepFactory.class)
    .in(Singleton.class);
  }
}
