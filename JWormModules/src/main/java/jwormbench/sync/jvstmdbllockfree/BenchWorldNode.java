package jwormbench.sync.jvstmdbllockfree;

import jvstm.lockfree.OwnershipRecord;
import jvstm.lockfree.UtilUnsafe;
import jvstm.lockfree.VBoxBody;
import jvstm.lockfree.dblayout.DoubleLayout;
import jvstm.lockfree.dblayout.StmBarriers;
import jvstm.util.Pair;
import jwormbench.core.INode;
import jwormbench.core.IWorm;
import jwormbench.defaults.Node;

/**
 * Abstracts the node object within the BenchWorld.
 * 
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
 */
public class BenchWorldNode extends ReplicableNode implements INode, DoubleLayout<ReplicableNode>{
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*
     *------------ DoubleLayout INFRA-STRUCTURE -----------------*
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private static final long header__ADDRESS__;
    private static final long currentOwner__ADDRESS__;
    private static final long value__ADDRESS__;
    static{
        try {
    	header__ADDRESS__= UtilUnsafe.UNSAFE.objectFieldOffset(BenchWorldNode.class.getDeclaredField("header"));
    	currentOwner__ADDRESS__ = UtilUnsafe.UNSAFE.objectFieldOffset(Pair.class.getDeclaredField("first"));
    	value__ADDRESS__ = UtilUnsafe.UNSAFE.objectFieldOffset(Node.class.getDeclaredField("value"));
        } catch (SecurityException e) {
    	throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
    	throw new RuntimeException(e);
        }
    }

    @Override
    public VBoxBody<ReplicableNode> readHeader() {
	return header;
    }
    @Override
    public boolean casHeader(VBoxBody<ReplicableNode> expected, VBoxBody<ReplicableNode> newBody) {
	return UtilUnsafe.UNSAFE.compareAndSwapObject(this, header__ADDRESS__, expected, newBody);
    }
    public void toStandardLayout(ReplicableNode from){
	this.value = from.value;
    }
    public boolean casHeaderWithNull(VBoxBody<ReplicableNode> expected){
	return UtilUnsafe.UNSAFE.compareAndSwapObject(this,header__ADDRESS__, expected, null);
    }
    //
    // Methods from the OwnershipRecord approach
    //
    @Override
    public Pair<OwnershipRecord, ReplicableNode> getOwner() {
	return currentOwner;
    }
    @Override
    public boolean CASsetOwner(OwnershipRecord previousOwner, OwnershipRecord newOwner) {
	return UtilUnsafe.UNSAFE.compareAndSwapObject(this.currentOwner, currentOwner__ADDRESS__, previousOwner, newOwner);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ---------------------- FIELDS --------------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private VBoxBody<ReplicableNode> header;
    private Pair<OwnershipRecord, ReplicableNode> currentOwner = new Pair<OwnershipRecord, ReplicableNode>(OwnershipRecord.DEFAULT_COMMITTED_OWNER, null);


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // -------------------   CONSTRUCTOR ----------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public BenchWorldNode(int value){
	super(value);
        worm = null;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // -------------------   PROPERTIES  ----------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * @see wormbench.INode#getValue()
     */
    public int getValue() {
        return StmBarriers.get(this).value;
    }
    /**
     * @see wormbench.INode#setValue(int)
     */
    public void setValue(int value) {
        StmBarriers.put(this, value, value__ADDRESS__);
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
