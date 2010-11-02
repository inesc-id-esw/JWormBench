package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IBenchWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;

public class Sum extends AbstractOperation<Integer>{ 
  public Sum(IBenchWorld world) {
    super(world, OperationKind.Sum, false);
  }
  @Override
  public Integer performOperation(IWorm w) {
    int sum = 0;
    for (int i = 0; i < w.getHeadLength(); i++) {
      ICoordinate c = w.getHeadCoordinate(i);
      sum += world.getNode(c).getValue();
    }
    return sum;
  }
}
