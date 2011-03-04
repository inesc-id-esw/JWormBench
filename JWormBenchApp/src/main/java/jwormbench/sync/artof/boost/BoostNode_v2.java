package jwormbench.sync.artof.boost;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import artof.core.AbortedException;
import artof.core.Transaction;
import artof.core.Transaction.Status;
import artof.core.TThread;

import jwormbench.core.INode;
import jwormbench.core.IWorm;

public class BoostNode_v2 implements INode{
  private static final long LOCK_TIMEOUT = 2;
  static ThreadLocal<List<Runnable>> localOnAbort = new ThreadLocal<List<Runnable>>(){
    protected java.util.List<Runnable> initialValue() {
     return new ArrayList<Runnable>(); 
    }
  };
  static ThreadLocal<List<Runnable>> localOnCommit = new ThreadLocal<List<Runnable>>(){
    protected java.util.List<Runnable> initialValue() {
     return new ArrayList<Runnable>(); 
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
        List<Runnable> handlers = localOnAbort.get();
        for (int i = handlers.size()-1; i >= 0; i--) {
          handlers.get(i).run();
        }
        handlers.clear();
        localOnCommit.get().clear();
      }
    });
    TThread.addOnCommitHandler(new Runnable() {
      public void run() {
        List<Runnable> handlers = localOnCommit.get();
        for (int i = handlers.size()-1; i >= 0; i--) {
          handlers.get(i).run();
        }
        handlers.clear();
        localOnAbort.get().clear();
      }
    });
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private final LinearNode linNode;
  private final ReentrantLock myLock;

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public BoostNode_v2(LinearNode linNode) {
    super();
    this.linNode = linNode;
    this.myLock = new ReentrantLock();
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   PROPERTIES  ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Override
  public int getValue() {
    if(Transaction.getLocal().getStatus() == Status.ACTIVE){
      if(!myLock.isHeldByCurrentThread()){
        try {
          if(myLock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS)){
            // Then we must register an handler to release the lock.
            localOnAbort.get().add(new Runnable() {
              public void run() {
                myLock.unlock();
              }
            });
            localOnCommit.get().add(new Runnable() {
              public void run() {
                myLock.unlock();
              }
            });
          }else{ // lock already acquired by a concurrent transaction
            throw new AbortedException();    
          }
        } catch (InterruptedException e) {
          throw new AbortedException(e);
        }
      }
    }
    return linNode.getValue();
  }
  @Override
  public void setValue(int value) {
    if(Transaction.getLocal().getStatus() == Status.ACTIVE){
      if(myLock.isHeldByCurrentThread()){
        // Then the handler just have to rollback the operation
        // and another handler will release the lock.
        final int prevValue = linNode.getValue();
        localOnAbort.get().add(new Runnable() {
          public void run() {
            linNode.setValue(prevValue);    
          }
        });
      }else{
        try {
          if(myLock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS)){
            final int prevValue = linNode.getValue();
            localOnAbort.get().add(new Runnable() {
              public void run() {
                linNode.setValue(prevValue);
                myLock.unlock();
              }
            });
            localOnCommit.get().add(new Runnable() {
              public void run() {
                myLock.unlock();
              }
            });
          }else{ // lock already acquired by a concurrent transaction
            throw new AbortedException();    
          }
        } catch (InterruptedException e) {
          throw new AbortedException(e);
        }
      }
    }
    // If we reach here that means no other transaction 
    // is concurring for this abstract lock.   
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
