package jwormbench.sync.lock;

import com.google.inject.Inject;

import jwormbench.core.AbstractStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.factories.AbstractStepFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.setup.IStepSetup;

public class LockStepCrossingoverFactory extends AbstractStepFactory{
  @Inject
  public LockStepCrossingoverFactory(IStepSetup opsSetup, IOperationFactory opsFac) {
    super(opsSetup, opsFac);
  }
  @Override
  protected AbstractStep factoryMethod(IOperation<?> op, Direction direction){
    return new LockStepCrossingover(direction, op);
  }
}