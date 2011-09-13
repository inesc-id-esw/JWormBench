package jwormbench.app.config;

import artof.cm.BackofMaxDelay;
import artof.cm.BackofMinDelay;
import artof.cm.BackoffManager;
import artof.core.ContentionManager;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ArtOfTmContentionManagerModule extends AbstractModule{
  private final int minDelay, maxDelay;
  public ArtOfTmContentionManagerModule(int minDelay, int maxDelay) {
    super();
    this.minDelay = minDelay;
    this.maxDelay = maxDelay;
  }
  @Override
  protected void configure() {
    //
    // Parameters and ContentionManager
    //
    bind(int.class).annotatedWith(BackofMinDelay.class).toInstance(minDelay);
    bind(int.class).annotatedWith(BackofMaxDelay.class).toInstance(maxDelay);
    bind(ContentionManager.class)
    .to(BackoffManager.class)
    .in(Singleton.class);
  }
}
