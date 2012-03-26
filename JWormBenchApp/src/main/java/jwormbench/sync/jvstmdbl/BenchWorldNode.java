package jwormbench.sync.jvstmdbl;

import jvstm.dblayout.AbstractDoubleLayout;
import jvstm.dblayout.DoubleLayout;
import jvstm.dblcore.VBox;
import jvstm.reflection.UnsafeHolder;
import jwormbench.core.INode;
import jwormbench.core.IWorm;

/**
 * Abstracts the node object within the BenchWorld.
 * 
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
 */
public class BenchWorldNode extends AbstractDoubleLayout implements INode, DoubleLayout{
  
  private static final int value__INDEX__ = 0;
  private static final long value__ADDRESS__;
  static{
    try {
      value__ADDRESS__ = UnsafeHolder.getUnsafe().objectFieldOffset(BenchWorldNode.class.getDeclaredField("value"));
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object[] toExtendedLayout(){
    return new Object[]{this.value};
  }
  @Override
  public void toCompactLayout(Object[] from){
    this.value = (Integer) from[value__INDEX__ ];
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
    // The explicit cast from int to Object it is not required, 
    // but I made it to avoid reordering mistakes between the 
    // fieldIndex and filedValue arguments.
    //
    return VBox.getInt(this, value__INDEX__ , value__ADDRESS__);
  }
  /**
   * @see wormbench.INode#setValue(int)
   */
  public void setValue(int newValue) {
    // The explicit cast from int to Object it is not required, 
    // but I made it to avoid reordering mistakes between the 
    // fieldIndex and filedValue arguments.
    //
    VBox.put(newValue, this, value__INDEX__, 1);
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
