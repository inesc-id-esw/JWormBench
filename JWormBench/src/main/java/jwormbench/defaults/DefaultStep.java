package jwormbench.defaults;

import jwormbench.core.AbstractStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;

public class DefaultStep extends AbstractStep{

  public DefaultStep(Direction direction, IOperation<?> op) {
    super(direction, op);
  }
  public Object performStep(IWorm worm){
    Object res = op.performOperation(worm);
    worm.move(direction);
    worm.updateWorldUnderWorm();   
    return res;
  }
}
