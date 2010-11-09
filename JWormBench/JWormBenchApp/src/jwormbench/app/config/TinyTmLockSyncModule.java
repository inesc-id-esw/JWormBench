package jwormbench.app.config;

import jwormbench.factories.IBenchWorldNodeFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.sync.tinytm.TinyTmLockBenchNodeFactory;
import jwormbench.sync.tinytm.TinyTmStepFactory;

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
    bind(IBenchWorldNodeFactory.class)
    .to(TinyTmLockBenchNodeFactory.class)
    .in(Singleton.class);
  }
}
