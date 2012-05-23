package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMedianWithMin extends AbstractOperation<Integer>{ 
    final IOperation<IOperation.Element<ICoordinate, Integer>> opMedian;
    final IOperation<IOperation.Element<Integer, Integer>> opMin;

    public ReplaceMedianWithMin(IWorld world, IOperationFactory opsFac) {
	super(world, OperationKind.ReplaceMedianWithMin, true);
	this.opMedian = opsFac.<IOperation.Element<ICoordinate, Integer>>make(OperationKind.Median);
	this.opMin = opsFac.<IOperation.Element<Integer, Integer>>make(OperationKind.Minimum);
    }
    @Override
    public Integer performOperation(IWorm w) {
	IOperation.Element<ICoordinate, Integer> medianElem = opMedian.performOperation(w);
	IOperation.Element<Integer, Integer> minElem = opMin.performOperation(w);
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
