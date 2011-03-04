package jwormbench.sync.artof.boost;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import artof.core.Transaction;
import artof.core.Transaction.Status;
import artof.core.TThread;

import jwormbench.core.INode;
import jwormbench.core.IWorm;

/**
 * We do not want to synchronize the manipulation of worm property.
 * We just have to worry with value property.
 * The invocation of setValue does not commute with any other invocation, 
 * neither setValue, nor getValue.
 * The invocation of getValue just commute with itself.
 * Version 1 => use a ReentrantLock
 * Version 2 => use a ReadWriteLock allow parallelism between concurrent getValue invocations. 
 */
public class BoostNode implements INode{
  static ThreadLocal<Set<Runnable>> localOnAbort = new ThreadLocal<Set<Runnable>>(){
    protected Set<Runnable> initialValue() {
     return new HashSet<Runnable>(); 
    }
  };
  static ThreadLocal<Set<Runnable>> localOnCommit = new ThreadLocal<Set<Runnable>>(){
    protected Set<Runnable> initialValue() {
     return new HashSet<Runnable>(); 
    }
  };
  static{
    TThread.onValidate = new Callable<Boolean>() {
      public Boolean call() throws Exception {
        return Transaction.getLocal().getStatus() != Status.ABORTED; 
      }
    };
    TThread.addOnAbortHandler(new Runnable() {
      public void run() {
        Set<Runnable> handlers = localOnAbort.get();
        for (Runnable h : handlers) {
          h.run();
        }
        handlers.clear();
        localOnCommit.get().clear();
        LockSet.cleanup(); // unlock all acquired locks
      }
    });
    TThread.addOnCommitHandler(new Runnable() {
      public void run() {
        Set<Runnable> handlers = localOnCommit.get();
        for (Runnable h : handlers) {
          h.run();
        }
        handlers.clear();
        localOnAbort.get().clear();
        LockSet.cleanup(); // unlock all acquired locks
      }
    });
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private final LinearNode linNode;
  private final LockNode myLock;

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public BoostNode(LinearNode linNode) {
    super();
    this.linNode = linNode;
    this.myLock = new LockNode();
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   PROPERTIES  ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Override
  public int getValue() {
    if(Transaction.getLocal().getStatus() == Status.ACTIVE)
      myLock.readLock();
    return linNode.getValue();
  }
  @Override
  public void setValue(int value) {
    if(Transaction.getLocal().getStatus() == Status.ACTIVE){
      myLock.writeLock();
      final int prevValue = linNode.getValue();
      localOnAbort.get().add(new Runnable() {
        public void run() {
          linNode.setValue(prevValue);    
        }
      });
    }
    linNode.setValue(value);
  }
  @Override
  public IWorm getWorm() {
    return linNode.getWorm();
  }
  @Override
  public void setWorm(IWorm worm) {
    linNode.setWorm(worm);    
  }
}
