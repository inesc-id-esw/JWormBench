package jwormbench.defaults;

import jwormbench.core.IBenchWorldNode;
import jwormbench.core.IWorm;

/**
 * Abstracts the node object within the BenchWorld.
 * 
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
 */
public class BenchWorldNode implements IBenchWorldNode{
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
   * @see wormbench.IBenchWorldNode#getValue()
   */
  public int getValue() {
    return value;
  }
  /**
   * @see wormbench.IBenchWorldNode#setValue(int)
   */
  public void setValue(int value) {
    this.value = value;
  }
  /**
   * @see wormbench.IBenchWorldNode#getWorm()
   */
  public IWorm getWorm() {
    return worm;
  }
  /**
   * @see wormbench.IBenchWorldNode#setWorm(IWorm)
   */
  public void setWorm(IWorm w) {
    /*
    if(worm != w && worm != null && w!= null)
      throw new NodeAlreadyOccupiedException(
          String.format("Worm %s can not move to node with worm %s", w.getName(), worm.getName()));
    */
    worm = w;
  }
}
