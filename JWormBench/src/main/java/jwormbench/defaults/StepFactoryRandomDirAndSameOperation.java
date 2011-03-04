package jwormbench.defaults;

import java.util.LinkedList;
import java.util.List;

import jwormbench.core.AbstractStep;
import jwormbench.core.IStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.factories.IStepFactory;

public class StepFactoryRandomDirAndSameOperation<T> implements IStepFactory{
  private final int nrOfSteps;
  private final IOperation<T> op;
  public StepFactoryRandomDirAndSameOperation(int nrOfSteps, IOperation<T> op) {
    super();
    this.nrOfSteps = nrOfSteps;
    this.op = op;
  }
  @Override
  public List<IStep> make() {
    List<IStep> steps = new LinkedList<IStep>();
    for (int i = 0; i < nrOfSteps; i++) {
      Direction dir = Direction.values()[(int)(Math.random()*3)];
      steps.add(new AbstractStep(dir, op) {
        public Object  performStep(IWorm worm) {
          Object res = op.performOperation(worm);
          worm.move(direction);
          worm.updateWorldUnderWorm();
          return res;
        }
      });
    }
    return steps;
  }
}

