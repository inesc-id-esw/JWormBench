package jwormbench.sync.tinytm;

import com.google.inject.Inject;

import jwormbench.core.AbstractStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.factories.AbstractStepFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.setup.IStepSetup;

public class TinyTmStepFactory extends AbstractStepFactory{
  @Inject
  public TinyTmStepFactory(IStepSetup opsSetup, IOperationFactory opFac) {
    super(opsSetup, opFac);
  }

  @Override
  protected AbstractStep factoryMethod(IOperation<?> op, Direction direction) {
    return new TinyTmStep(direction, op);
  }

}
