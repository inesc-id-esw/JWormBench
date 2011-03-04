package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMaxAndMin extends AbstractOperation<Integer>{ 
  IOperationFactory opsFac;
  public ReplaceMaxAndMin(IWorld world, IOperationFactory opsFac) {
    super(world, OperationKind.ReplaceMaxAndMin, false);
    this.opsFac = opsFac;
  }
  @Override
  public Integer performOperation(IWorm w) {
    IOperation.Element<Integer, Integer> maxElem = opsFac.
      <IOperation.Element<Integer, Integer>>make(OperationKind.Maximum).
      performOperation(w);
    IOperation.Element<Integer, Integer> minElem = opsFac.
      <IOperation.Element<Integer, Integer>>make(OperationKind.Minimum).
      performOperation(w);
      
    world.getNode(w.getHeadCoordinate(maxElem.idx)).setValue(minElem.value);
    world.getNode(w.getHeadCoordinate(minElem.idx)).setValue(maxElem.value);
     
    return maxElem.value - minElem.value;
  }
}
