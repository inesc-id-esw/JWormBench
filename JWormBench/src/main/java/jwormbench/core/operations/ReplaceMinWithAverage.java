package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMinWithAverage extends AbstractOperation<Integer>{ 
  IOperationFactory opsFac;
  public ReplaceMinWithAverage(IWorld world, IOperationFactory opsFac) {
    super(world, OperationKind.ReplaceMinWithAverage, true);
    this.opsFac = opsFac;
  }
  @Override
  public Integer performOperation(IWorm w) {
    IOperation.Element<Integer, Integer> minElem = opsFac.
      <IOperation.Element<Integer, Integer>>make(OperationKind.Minimum).
      performOperation(w);
    int avg = opsFac.
      <Integer>make(OperationKind.Average).
      performOperation(w);
    //
    // updates world 
    //
    world.getNode(w.getHeadCoordinate(minElem.idx)).setValue(avg);
    //
    // return the difference between the previous state and the new state of the world. 
    //
    return avg - minElem.value;
  }
}
