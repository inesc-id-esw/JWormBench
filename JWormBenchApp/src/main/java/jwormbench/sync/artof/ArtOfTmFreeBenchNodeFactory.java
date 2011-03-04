package jwormbench.sync.artof;

import artof.obsfree.FreeObject;
import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;

public class ArtOfTmFreeBenchNodeFactory implements INodeFactory{
  @Override
  public INode make(int initValue) {
    return new ArtOfTmNode(
        new FreeObject<SequentialBenchWorldNode>(
            new SequentialBenchWorldNode(initValue)));
  }
}
