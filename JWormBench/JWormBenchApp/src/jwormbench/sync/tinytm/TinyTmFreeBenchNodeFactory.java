package jwormbench.sync.tinytm;

import TinyTM.ofree.FreeObject;
import jwormbench.core.IBenchWorldNode;
import jwormbench.factories.IBenchWorldNodeFactory;

public class TinyTmFreeBenchNodeFactory implements IBenchWorldNodeFactory{
  @Override
  public IBenchWorldNode make(int initValue) {
    return new TinyTmNode(
        new FreeObject<SequentialBenchWorldNode>(
            new SequentialBenchWorldNode(initValue)));
  }
}
