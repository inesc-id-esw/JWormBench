package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IBenchWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMedianWithMin extends AbstractOperation<Integer>{ 
  IOperationFactory opsFac;
  public ReplaceMedianWithMin(IBenchWorld world, IOperationFactory opsFac) {
    super(world, OperationKind.ReplaceMedianWithMin, true);
    this.opsFac = opsFac;
  }
    @Override
  public Integer performOperation(IWorm w) {
    IOperation.Element<ICoordinate, Integer> medianElem = opsFac.
      <IOperation.Element<ICoordinate, Integer>>make(OperationKind.Median).
      performOperation(w);
    IOperation.Element<Integer, Integer> minElem = opsFac.
      <IOperation.Element<Integer, Integer>>make(OperationKind.Minimum).
      performOperation(w);
    //
    // updates world
    //
    world.getNode(medianElem.idx).setValue(minElem.value);
    //
    // return the difference between the previous state and the new state of the world. 
    //
    return minElem.value - medianElem.value;
  }
}
