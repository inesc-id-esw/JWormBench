package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMinWithAverage extends AbstractOperation<Integer>{ 
  final IOperation<IOperation.Element<Integer, Integer>> opMin;
  final IOperation<Integer> opAvg;
  
  public ReplaceMinWithAverage(IWorld world, IOperationFactory opsFac) {
    super(world, OperationKind.ReplaceMinWithAverage, true);
    this.opMin = opsFac.<IOperation.Element<Integer, Integer>>make(OperationKind.Minimum);
    this.opAvg = opsFac.<Integer>make(OperationKind.Average);
  }
  @Override
  public Integer performOperation(IWorm w) {
    IOperation.Element<Integer, Integer> minElem = opMin.performOperation(w);
    int avg = opAvg.performOperation(w);
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
