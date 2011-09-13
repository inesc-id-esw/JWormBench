package jwormbench.sync.jvstm;

import jvstm.VBox;
import jwormbench.core.IBenchWorldNode;
import jwormbench.core.IWorm;
import jwormbench.factories.IBenchWorldNodeFactory;


public class JvstmBenchNodeFactory implements IBenchWorldNodeFactory{
  public IBenchWorldNode make(int initValue){
    return new BenchWorldNode(initValue);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // --------------- NESTED TYPES  --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Abstracts the node object within the BenchWorld.
   * 
   * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
   */
  class BenchWorldNode implements IBenchWorldNode {
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ---------------------- FIELDS --------------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private VBox<Integer> value = new VBox<Integer>();
    private IWorm worm;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // -------------------   CONSTRUCTOR ----------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public BenchWorldNode(int value){
      this.value.put(value);
      worm = null;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // -------------------   PROPERTIES  ----------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * @see wormbench.IBenchWorldNode#getValue()
     */
    public int getValue() {
      return value.get();
    }
    /**
     * @see wormbench.IBenchWorldNode#setValue(int)
     */
    public void setValue(int value) {
      this.value.put(value);
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
      this.worm = w;
      /*
      if(worm.get() != w && worm.get() != null && w!= null)
        throw new NodeAlreadyOccupiedException(
            String.format("Worm %s can not move to node with worm %s", w.getName(), worm.get().getName()));
      worm.put(w);
      */
    }
  }
}
