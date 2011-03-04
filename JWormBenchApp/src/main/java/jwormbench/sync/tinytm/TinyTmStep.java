package jwormbench.sync.tinytm;

import java.util.concurrent.Callable;

import TinyTM.TThread;

import jwormbench.core.AbstractStep;
import jwormbench.core.IStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;

public class TinyTmStep extends AbstractStep{

  public TinyTmStep(Direction direction, IOperation<?> op) {
    super(direction, op);
  }

  @Override
  public Object performStep(final IWorm worm) {
    Object res = null;
    //
    // Perform operation
    //
    try {
      Callable<Object> task = new Callable<Object>() {
        public Object call() throws Exception {
          return op.performOperation(worm);
        }
      };
      res = TThread.doIt(task);
    } catch (Exception e) {
      throw new RuntimeException("For operation: " + op.getKind(), e);
    }
    //
    // Move worm
    //
    worm.move(direction);
    worm.updateWorldUnderWorm();
    return res;
  }
}
