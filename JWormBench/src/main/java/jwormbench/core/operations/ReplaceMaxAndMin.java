package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.ICoordinate;
import jwormbench.core.IWorld;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMaxAndMin extends AbstractOperation<Integer>{ 
    final IOperation<IOperation.Element<Integer, Integer>> opMin;
    final IOperation<IOperation.Element<Integer, Integer>> opMax;

    public ReplaceMaxAndMin(IWorld world, IOperationFactory opsFac) {
	super(world, OperationKind.ReplaceMaxAndMin, true);
	this.opMin = opsFac.<IOperation.Element<Integer, Integer>>make(OperationKind.Minimum);
	this.opMax = opsFac.<IOperation.Element<Integer, Integer>>make(OperationKind.Maximum);
    }
    @Override
    public Integer performOperation(IWorm w) {
	IOperation.Element<Integer, Integer> maxElem = opMax.performOperation(w);
	IOperation.Element<Integer, Integer> minElem = opMin.performOperation(w);

	world.getNode(w.getHeadCoordinate(maxElem.idx)).setValue(minElem.value);
	world.getNode(w.getHeadCoordinate(minElem.idx)).setValue(maxElem.value);

	return 0;
    }
}
