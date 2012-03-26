package jwormbench.sync.jvstm.aom;

import jvstm.lockfree.dblayout.NoSync;
import jvstm.lockfree.dblayout.Transactional;
import jwormbench.core.INode;
import jwormbench.core.IWorm;

@Transactional
public class TransactionalNode implements INode{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ---------------------- FIELDS --------------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected int value;
    @NoSync private IWorm worm;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // -------------------   CONSTRUCTOR ----------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public TransactionalNode(int value){
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
