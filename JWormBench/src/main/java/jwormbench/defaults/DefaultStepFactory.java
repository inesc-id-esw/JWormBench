package jwormbench.defaults;

import com.google.inject.Inject;

import jwormbench.core.AbstractStep;
import jwormbench.core.IStep;
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
  protected IStep factoryMethod(IOperation<?> op, Direction direction){
    return new DefaultStep(direction, op);
  }
}