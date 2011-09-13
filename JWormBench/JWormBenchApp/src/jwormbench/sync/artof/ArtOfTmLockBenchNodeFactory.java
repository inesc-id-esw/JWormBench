package jwormbench.sync.artof;

import artof.locking.LockObject;
import jwormbench.core.IBenchWorldNode;
import jwormbench.factories.IBenchWorldNodeFactory;

public class ArtOfTmLockBenchNodeFactory implements IBenchWorldNodeFactory{
  @Override
  public IBenchWorldNode make(int initValue) {
    return new ArtOfTmNode(
        new LockObject<SequentialBenchWorldNode>(
            new SequentialBenchWorldNode(initValue)));
  }
}
