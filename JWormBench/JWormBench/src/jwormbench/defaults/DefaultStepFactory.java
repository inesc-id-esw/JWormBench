package jwormbench.defaults;

import com.google.inject.Inject;

import jwormbench.core.AbstractStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.factories.AbstractStepFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.setup.IStepSetup;

public class DefaultStepFactory extends AbstractStepFactory{

  @Inject
  public DefaultStepFactory(IStepSetup opsSetup, IOperationFactory opsFac) {
    super(opsSetup, opsFac);
  }
  protected AbstractStep factoryMethod(IOperation<?> op, Direction direction){
    return new AbstractStep(direction, op){
      public Object performStep(IWorm worm){
        Object res = op.performOperation(worm);
        worm.move(direction);
        worm.updateWorldUnderWorm();   
        return res;
      }
    };
  }
}