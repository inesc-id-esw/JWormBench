package jwormbench.sync.artof;

import artof.obsfree.FreeObject;
import jwormbench.core.IBenchWorldNode;
import jwormbench.factories.IBenchWorldNodeFactory;

public class ArtOfTmFreeBenchNodeFactory implements IBenchWorldNodeFactory{
  @Override
  public IBenchWorldNode make(int initValue) {
    return new ArtOfTmNode(
        new FreeObject<SequentialBenchWorldNode>(
            new SequentialBenchWorldNode(initValue)));
  }
}
