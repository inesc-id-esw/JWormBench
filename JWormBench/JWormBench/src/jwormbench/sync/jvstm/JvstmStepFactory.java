package jwormbench.sync.jvstm;

import com.google.inject.Inject;

import jwormbench.core.AbstractStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.factories.AbstractStepFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.setup.IStepSetup;

public class JvstmStepFactory extends AbstractStepFactory{
  @Inject
  public JvstmStepFactory(IStepSetup opsSetup, IOperationFactory opFac) {
    super(opsSetup, opFac);
  }

  @Override
  protected AbstractStep factoryMethod(IOperation<?> op, Direction direction) {
    return new JvstmStep(direction, op);
  }

}
