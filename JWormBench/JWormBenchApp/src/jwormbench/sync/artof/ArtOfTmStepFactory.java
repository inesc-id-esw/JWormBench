package jwormbench.sync.artof;

import com.google.inject.Inject;

import jwormbench.core.AbstractStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.factories.AbstractStepFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.setup.IStepSetup;

public class ArtOfTmStepFactory extends AbstractStepFactory{
  @Inject
  public ArtOfTmStepFactory(IStepSetup opsSetup, IOperationFactory opFac) {
    super(opsSetup, opFac);
  }

  @Override
  protected AbstractStep factoryMethod(IOperation<?> op, Direction direction) {
    return new ArtOfTmStep(direction, op);
  }

}
