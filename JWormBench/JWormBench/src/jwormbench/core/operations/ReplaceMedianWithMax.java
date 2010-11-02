package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IBenchWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMedianWithMax extends AbstractOperation<Integer>{ 
  IOperationFactory opsFac;
  public ReplaceMedianWithMax(IBenchWorld world, IOperationFactory opsFac) {
    super(world, OperationKind.ReplaceMedianWithMax, true);
    this.opsFac = opsFac;
  }
  @Override
  public Integer performOperation(IWorm w) {
    IOperation.Element<ICoordinate, Integer> medianElem = opsFac.
      <IOperation.Element<ICoordinate, Integer>>make(OperationKind.Median).
      performOperation(w);
    IOperation.Element<Integer, Integer> maxElem = opsFac.
      <IOperation.Element<Integer, Integer>>make(OperationKind.Maximum).
      performOperation(w);
    //
    // updates the world 
    //
    world.getNode(medianElem.idx).setValue(maxElem.value);
    //
    // return the difference between the previous state and the new state of the world. 
    // 
    return maxElem.value - medianElem.value;
  }
}
