package jwormbench.sync.jvstmdbl;

import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;

public class NodeDoubleLayoutFactory implements INodeFactory{

  @Override
  public INode make(int initValue) {
    // TODO Auto-generated method stub
    return new BenchWorldNode(initValue);
  }
}
