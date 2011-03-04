package jwormbench.sync.artof.boost;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;

/**
 * Local lock set.
 * @author Maurice Herlihy
 */
public class LockSet {

  static ThreadLocal<Set<Lock>> local = new ThreadLocal<Set<Lock>>() {
    protected synchronized Set<Lock> initialValue() {
      return new HashSet<Lock>();
    }
  };
  public static LockSet getLocal() {
    return new LockSet();
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  Set<Lock> set;
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  protected LockSet() {
    set = local.get();
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------  METHODS -------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * Add a lock to local set
   * @param lock 
   * @return <i>true</i> if lock not already in set
   */
  public boolean add(Lock lock) {
    return set.add(lock);
  }

  /**
   * Remove a lock from local set
   * @param e lock to remove
   * @return <i>true</i> iff lock was in set
   */
  public boolean remove(Lock e) {
    return set.remove(e);
  }

  /**
   * Is lock present in local set?
   * @param e lock to test
   * @return <i>true</i> iff lock in set
   */
  public boolean contains(Lock e) {
    return set.contains(e);
  }

  public static void cleanup() {
    Set<Lock> set = local.get();
    for (Lock lock : set) {
      lock.unlock();
    }
    set.clear();
  }

}
