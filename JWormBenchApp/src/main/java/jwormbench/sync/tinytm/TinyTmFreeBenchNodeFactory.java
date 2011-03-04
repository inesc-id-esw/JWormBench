package jwormbench.sync.tinytm;

import TinyTM.ofree.FreeObject;
import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;

public class TinyTmFreeBenchNodeFactory implements INodeFactory{
  @Override
  public INode make(int initValue) {
    return new TinyTmNode(
        new FreeObject<SequentialBenchWorldNode>(
            new SequentialBenchWorldNode(initValue)));
  }
}
