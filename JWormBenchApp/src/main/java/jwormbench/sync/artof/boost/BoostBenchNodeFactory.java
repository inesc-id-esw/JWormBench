package jwormbench.sync.artof.boost;

import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;

public class BoostBenchNodeFactory implements INodeFactory{

  @Override
  public INode make(int initValue) {
    return new BoostNode(new LinearNode(initValue));
  }

}
