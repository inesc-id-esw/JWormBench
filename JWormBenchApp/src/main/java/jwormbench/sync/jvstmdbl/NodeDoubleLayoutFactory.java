package jwormbench.sync.jvstmdbl;

import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;

/**
 * Depends on project jvstm-doublelayout-v3 - version of Multiprog12 - replicate via array. 
 * @author mcarvalho
 *
 */
public class NodeDoubleLayoutFactory implements INodeFactory{

  @Override
  public INode make(int initValue) {
    // TODO Auto-generated method stub
    return new BenchWorldNode(initValue);
  }
}
