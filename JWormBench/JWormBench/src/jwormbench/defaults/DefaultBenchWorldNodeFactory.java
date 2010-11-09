package jwormbench.defaults;

import jwormbench.core.IBenchWorldNode;
import jwormbench.factories.IBenchWorldNodeFactory;


public class DefaultBenchWorldNodeFactory implements IBenchWorldNodeFactory {
  public IBenchWorldNode make(int initValue){
    return new BenchWorldNode(initValue);
  }
}
