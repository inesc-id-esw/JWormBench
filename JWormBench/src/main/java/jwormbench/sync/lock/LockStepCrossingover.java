package jwormbench.sync.lock;

import jwormbench.core.AbstractStep;
import jwormbench.core.IStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;

public class LockStepCrossingover extends AbstractStep{
  private static final Object operationLock = new Object();
  private static final Object moveLock = new Object();

  public LockStepCrossingover(Direction direction, IOperation<?> op) {
    super(direction, op);
  }
  @Override
  public Object performStep(IWorm worm) {
    Object res = null;
    //
    // Perform operation
    //
    synchronized(operationLock){
      res = op.performOperation(worm);
    }
    //
    // Move worm
    //
    worm.move(direction);
    worm.updateWorldUnderWorm();
    //
    // returns the result of operation
    //
    return res;
  }
}
