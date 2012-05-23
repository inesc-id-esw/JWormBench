package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMedianWithAverage extends AbstractOperation<Integer>{ 
    final IOperation<IOperation.Element<ICoordinate, Integer>> opMedian;
    final IOperation<Integer> opAvg;

    public ReplaceMedianWithAverage(IWorld world, IOperationFactory opsFac) {
	super(world, OperationKind.ReplaceMedianWithAverage, true);
	this.opMedian = opsFac.<IOperation.Element<ICoordinate, Integer>>make(OperationKind.Median);
	this.opAvg = opsFac.<Integer>make(OperationKind.Average);
    }

  @Override
  public Integer performOperation(IWorm w) {
    IOperation.Element<ICoordinate, Integer> medianElem = opMedian.performOperation(w);
    int avg = opAvg.performOperation(w);
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
