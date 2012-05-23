package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMedianWithMax extends AbstractOperation<Integer>{ 
    final IOperation<IOperation.Element<ICoordinate, Integer>> opMedian;
    final IOperation<IOperation.Element<Integer, Integer>> opMax;

    public ReplaceMedianWithMax(IWorld world, IOperationFactory opsFac) {
	super(world, OperationKind.ReplaceMedianWithMax, true);
	this.opMedian = opsFac.<IOperation.Element<ICoordinate, Integer>>make(OperationKind.Median);
	this.opMax = opsFac.<IOperation.Element<Integer, Integer>>make(OperationKind.Maximum);
    }
    @Override
    public Integer performOperation(IWorm w) {
	IOperation.Element<ICoordinate, Integer> medianElem = opMedian.performOperation(w);
	IOperation.Element<Integer, Integer> maxElem = opMax.performOperation(w);
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
