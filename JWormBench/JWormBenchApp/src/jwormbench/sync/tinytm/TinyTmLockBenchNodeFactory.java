package jwormbench.sync.tinytm;

import TinyTM.locking.LockObject;
import jwormbench.core.IBenchWorldNode;
import jwormbench.factories.IBenchWorldNodeFactory;

public class TinyTmLockBenchNodeFactory implements IBenchWorldNodeFactory{
  @Override
  public IBenchWorldNode make(int initValue) {
    return new TinyTmNode(
        new LockObject<SequentialBenchWorldNode>(
            new SequentialBenchWorldNode(initValue)));
  }
}
