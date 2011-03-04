package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMaxWithAverage extends AbstractOperation<Integer>{ 
  IOperationFactory opsFac;
  public ReplaceMaxWithAverage(IWorld world, IOperationFactory opsFac) {
    super(world, OperationKind.ReplaceMaxWithAverage, true);
    this.opsFac = opsFac;
  }
  @Override
  public Integer performOperation(IWorm w) {
    IOperation.Element<Integer, Integer> maxElem = opsFac.
      <IOperation.Element<Integer, Integer>>make(OperationKind.Maximum).
      performOperation(w);
    int avg = opsFac.
      <Integer>make(OperationKind.Average).
      performOperation(w);
    world.getNode(w.getHeadCoordinate(maxElem.idx)).setValue(avg);
     
    return avg - maxElem.value;
  }
}
