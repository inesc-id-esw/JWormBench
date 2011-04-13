package jwormbench.sync.jvstmdbl;

import jvstm.VBox;
import jvstm.dblayout.AbstractAtomic;
import jwormbench.core.INode;
import jwormbench.core.IWorm;

/**
 * Abstracts the node object within the BenchWorld.
 * 
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
 */
public class BenchWorldNode implements INode {
  private static final int value__ADDRESS__;
  private static final int value_vbox__ADDRESS__;
  static{
    try {
      value__ADDRESS__ = (int) UnsafeHolder.getUnsafe().objectFieldOffset(BenchWorldNode.class.getDeclaredField("value"));
      value_vbox__ADDRESS__ = (int) UnsafeHolder.getUnsafe().objectFieldOffset(BenchWorldNode.class.getDeclaredField("value_vbox"));
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public VBox<?> value_vbox;
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
   * For debug purpose.
   */
  public VBox<?> getVBox(){
    return value_vbox;
  }
  /**
   * @see wormbench.INode#getValue()
   */
  public int getValue() {
    return AbstractAtomic.onReadAccess(this, value, value_vbox__ADDRESS__);
  }
  /**
   * @see wormbench.INode#setValue(int)
   */
  public void setValue(int newValue) {
    AbstractAtomic.onWriteAccess(this, newValue, value__ADDRESS__, value_vbox__ADDRESS__);
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
