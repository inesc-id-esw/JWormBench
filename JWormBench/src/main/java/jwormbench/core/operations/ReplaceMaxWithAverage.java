package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class ReplaceMaxWithAverage extends AbstractOperation<Integer>{ 
    final IOperation<IOperation.Element<Integer, Integer>> opMax;
    final IOperation<Integer> opAvg;

    public ReplaceMaxWithAverage(IWorld world, IOperationFactory opsFac) {
	super(world, OperationKind.ReplaceMaxWithAverage, true);
	this.opMax = opsFac.<IOperation.Element<Integer, Integer>>make(OperationKind.Maximum);
	this.opAvg = opsFac.<Integer>make(OperationKind.Average);
    }
    @Override
    public Integer performOperation(IWorm w) {
	IOperation.Element<Integer, Integer> maxElem = opMax.performOperation(w);
	int avg = opAvg.performOperation(w);
	world.getNode(w.getHeadCoordinate(maxElem.idx)).setValue(avg);

	return avg - maxElem.value;
    }
}
