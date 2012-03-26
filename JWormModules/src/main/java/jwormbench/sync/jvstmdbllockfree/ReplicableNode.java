package jwormbench.sync.jvstmdbllockfree;

import jvstm.lockfree.dblayout.Replicable;
import jwormbench.core.IWorm;

public class ReplicableNode implements Replicable<ReplicableNode >{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ---------------------- FIELDS --------------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected int value;
    protected IWorm worm;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // -------------------   CONSTRUCTOR ----------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public ReplicableNode(int value){
	this.value = value;
	worm = null;
    }

    // @Override
    public ReplicableNode replicate() {
	return new ReplicableNode(this.value);
    }

}
