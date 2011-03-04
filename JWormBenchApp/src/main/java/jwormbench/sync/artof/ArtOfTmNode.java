package jwormbench.sync.artof;

import jwormbench.core.INode;
import jwormbench.core.IWorm;
import artof.core.AbortedException;
import artof.core.AtomicObject;

/**
 * Atomic version of BenchWorldNode for Tinytm obstruction free.
 */
public class ArtOfTmNode implements INode {
  final AtomicObject<SequentialBenchWorldNode> atomicNode;
  public ArtOfTmNode(AtomicObject<SequentialBenchWorldNode> init){
    atomicNode = init;
  }
  @Override
  public int getValue() {
    int val = atomicNode.openRead().getValue();
    // To ensure the value recorded is consistent with all other values 
    // observed by this transaction. 
    if(!atomicNode.isValid()) 
      throw new AbortedException(); 
    return val;
  }
  @Override
  public IWorm getWorm() {
    IWorm worm = atomicNode.openRead().getWorm();
    // To ensure the value recorded is consistent with all other values 
    // observed by this transaction. 
    if(!atomicNode.isValid())
      throw new AbortedException(); 
    return worm;
  }
  @Override
  public void setValue(int value) {
    atomicNode.openWrite().setValue(value);
  }
  @Override
  public void setWorm(IWorm worm) {
    atomicNode.openWrite().setWorm(worm);
  }
}
