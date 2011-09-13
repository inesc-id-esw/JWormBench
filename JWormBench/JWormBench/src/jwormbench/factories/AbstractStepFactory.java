package jwormbench.factories;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jwormbench.core.AbstractStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.setup.IDisposable;
import jwormbench.setup.IStepSetup;
import jwormbench.setup.IStepSetup.OperationProperties;

public abstract class AbstractStepFactory implements IStepFactory {
  private IStepSetup opsSetup;
  private IOperationFactory opFac;
  
  public AbstractStepFactory(IStepSetup opsSetup, IOperationFactory opFac) {
    this.opsSetup = opsSetup;
    this.opFac = opFac;
  }
  /**
   * @see jwormbench.defaults.IStepFactory#make()
   */
  @Override
  final public List<AbstractStep> make(){
    List<AbstractStep> steps = new LinkedList<AbstractStep>();
    Iterator<OperationProperties> iterator = null;
    try {
      iterator = opsSetup.iterator();
      while(iterator.hasNext()) {
        OperationProperties op = iterator.next();
        steps.add(
            factoryMethod(
                opFac.make(op.operationKind),
                op.direction));
      }
    }finally{
        if(iterator instanceof IDisposable)
          ((IDisposable) iterator).dispose();

    }
    return steps;
  }
  protected abstract AbstractStep factoryMethod(IOperation<?> op, Direction direction);
}
