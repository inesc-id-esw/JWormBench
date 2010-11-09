package jwormbench.sync.artof;

import artof.core.Copyable;
import jwormbench.defaults.BenchWorldNode;

/**
 * A copyable BenchWorldNode, must be public to be instantiated by reflection.
 * And must have a parameter less constructor.
 */
public class SequentialBenchWorldNode extends BenchWorldNode implements Copyable<SequentialBenchWorldNode>{
  /**
   * A parameter less constructor is requires to be instantiated by reflection
   * on atomic object's locator.
   */
  public SequentialBenchWorldNode(){
    super(0);
  }
  public SequentialBenchWorldNode(int value) {
    super(value);
  }
  @Override
  public void copyTo(SequentialBenchWorldNode target) {
    target.setValue(this.getValue());
    target.setWorm(this.getWorm());
  }    
} 
