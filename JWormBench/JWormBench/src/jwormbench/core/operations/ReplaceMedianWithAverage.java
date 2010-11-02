package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IBenchWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMedianWithAverage extends AbstractOperation<Integer>{ 
  IOperationFactory opsFac;
  public ReplaceMedianWithAverage(IBenchWorld world, IOperationFactory opsFac) {
    super(world, OperationKind.ReplaceMedianWithAverage, true);
    this.opsFac = opsFac;
  }
  @Override
  public Integer performOperation(IWorm w) {
    IOperation.Element<ICoordinate, Integer> medianElem = opsFac.
      <IOperation.Element<ICoordinate, Integer>>make(OperationKind.Median).
      performOperation(w);
    int avg = opsFac.
      <Integer>make(OperationKind.Average).
      performOperation(w);
    //
    // updates world
    //
    world.getNode(medianElem.idx).setValue(avg);
    //
    // return the difference between the previous state and the new state of the world. 
    // 
    return avg - medianElem.value;
  }
}
