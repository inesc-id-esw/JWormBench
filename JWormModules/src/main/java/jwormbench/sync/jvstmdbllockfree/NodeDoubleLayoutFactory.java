package jwormbench.sync.jvstmdbllockfree;

import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;
import jwormbench.sync.jvstmdbllockfree.BenchWorldNode;

public class NodeDoubleLayoutFactory implements INodeFactory{

  @Override
  public INode make(int initValue) {
    // TODO Auto-generated method stub
    return new BenchWorldNode(initValue);
  }
}
