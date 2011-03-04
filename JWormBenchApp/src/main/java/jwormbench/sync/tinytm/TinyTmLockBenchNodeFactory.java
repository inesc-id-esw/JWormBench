package jwormbench.sync.tinytm;

import TinyTM.locking.LockObject;
import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;

public class TinyTmLockBenchNodeFactory implements INodeFactory{
  @Override
  public INode make(int initValue) {
    return new TinyTmNode(
        new LockObject<SequentialBenchWorldNode>(
            new SequentialBenchWorldNode(initValue)));
  }
}
