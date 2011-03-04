package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;

public class Minimum extends AbstractOperation<IOperation.Element<Integer, Integer>>{
  public Minimum(IWorld world) {
    super(world, OperationKind.Minimum, false);
  }
  @Override
  public IOperation.Element<Integer, Integer> performOperation(IWorm w) {
    int min= world.getNode(w.getHeadCoordinate(0)).getValue();;
    int minIdx = 0;
    for (int i = 1; i < w.getHeadLength(); i++) {
      int val = world.getNode(w.getHeadCoordinate(i)).getValue();
      if(val < min) {
        min = val;
        minIdx = i;
      }
    }
    return new IOperation.Element<Integer, Integer>(minIdx,min);
  }
}
