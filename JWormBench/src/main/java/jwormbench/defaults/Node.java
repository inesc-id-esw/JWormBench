package jwormbench.defaults;

import org.deuce.transform.NoSyncArray;

import jwormbench.core.INode;
import jwormbench.core.IWorm;

/**
 * Abstracts the node object within the BenchWorld.
 * 
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
 */
@NoSyncArray
public class Node implements INode{
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private int value;
  private IWorm worm;

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public Node(int value){
    this.value = value;
    worm = null;
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   PROPERTIES  ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * @see INode.IBenchWorldNode#getValue()
   */
  public int getValue() {
    return value;
  }
  /**
   * @see INode.IBenchWorldNode#setValue(int)
   */
  public void setValue(int value) {
    this.value = value;
  }
  /**
   * @see INode.IBenchWorldNode#getWorm()
   */
  public IWorm getWorm() {
    return worm;
  }
  /**
   * @see INode.IBenchWorldNode#setWorm(IWorm)
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
