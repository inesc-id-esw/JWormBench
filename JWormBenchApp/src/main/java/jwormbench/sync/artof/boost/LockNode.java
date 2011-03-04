package jwormbench.sync.artof.boost;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import artof.core.AbortedException;

public class LockNode {
  private static final long LOCK_TIMEOUT = 2;

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  
  private final ReadWriteLock rwLock;
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public LockNode() {
    super();
    this.rwLock = new ReentrantReadWriteLock();
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------  METHODS -------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  public void readLock(){
    LockSet lockSet = LockSet.getLocal();
    if(lockSet.contains(rwLock.readLock()) || lockSet.contains(rwLock.writeLock()) )
      return;
    lock(rwLock.readLock());
  }
  public void writeLock(){
    LockSet lockSet = LockSet.getLocal();
    if(lockSet.contains(rwLock.writeLock()) )
      return;
    if(lockSet.contains(rwLock.readLock())){
      lockSet.remove(rwLock.readLock());
      rwLock.readLock().unlock();
    }
    lock(rwLock.writeLock());
  }
  private void lock(Lock myLock){
    LockSet lockSet = LockSet.getLocal();
    if(lockSet.add(myLock)){
      try {
        if(!myLock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS)){
          lockSet.remove(myLock);
          throw new AbortedException();
        }
      } catch (InterruptedException e) {
        lockSet.remove(myLock);
        throw new AbortedException(e);
      }
    }
  }
  public void readUnlock(){
    rwLock.readLock().unlock();
  }
  public void writeUnlock(){
    rwLock.writeLock().unlock();
  }
}
