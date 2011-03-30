package jwormbench.sync.jvstmdbl;

import jvstm.VBox;
import jvstm.dblayout.AbstractAtomic;
import jvstm.reflection.UnsafeHolder;
import jwormbench.core.INode;
import jwormbench.core.IWorm;

/**
 * Abstracts the node object within the BenchWorld.
 * 
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
 */
class BenchWorldNode implements INode {
  private static final long value__ADDRESS__;
  private static final long vbox_value__ADDRESS__;
  static{
    try {
      value__ADDRESS__ = UnsafeHolder.getUnsafe().objectFieldOffset(BenchWorldNode.class.getDeclaredField("value"));
      vbox_value__ADDRESS__ = UnsafeHolder.getUnsafe().objectFieldOffset(BenchWorldNode.class.getDeclaredField("vbox_value"));
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private VBox<Integer> vbox_value;
  private int value;
  private IWorm worm;

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public BenchWorldNode(int value){
    this.value = value;
    worm = null;
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   PROPERTIES  ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * @see wormbench.INode#getValue()
   */
  public int getValue() {
    return AbstractAtomic.onReadAccess(this, value, vbox_value__ADDRESS__);
  }
  /**
   * @see wormbench.INode#setValue(int)
   */
  public void setValue(int newValue) {
    AbstractAtomic.onWriteAccess(this, newValue, value__ADDRESS__, vbox_value__ADDRESS__);
  }
  /**
   * @see wormbench.INode#getWorm()
   */
  public IWorm getWorm() {
    return worm;
  }
  /**
   * @see wormbench.INode#setWorm(IWorm)
   */
  public void setWorm(IWorm w) {
    this.worm = w;
    /*
    if(worm.get() != w && worm.get() != null && w!= null)
      throw new NodeAlreadyOccupiedException(
          String.format("Worm %s can not move to node with worm %s", w.getName(), worm.get().getName()));
    worm.put(w);
    */
  }
}
