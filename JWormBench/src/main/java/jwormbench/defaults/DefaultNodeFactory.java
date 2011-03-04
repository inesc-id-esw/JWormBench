package jwormbench.defaults;

import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;


public class DefaultNodeFactory implements INodeFactory {
  public INode make(int initValue){
    return new Node(initValue);
  }
}
