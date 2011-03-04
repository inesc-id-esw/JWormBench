package jwormbench.sync.artof;

import artof.locking.LockObject;
import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;

public class ArtOfTmLockBenchNodeFactory implements INodeFactory{
  @Override
  public INode make(int initValue) {
    return new ArtOfTmNode(
        new LockObject<SequentialBenchWorldNode>(
            new SequentialBenchWorldNode(initValue)));
  }
}
