package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IBenchWorld;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;

public class Maximum extends AbstractOperation<IOperation.Element<Integer, Integer>>{
  public Maximum(IBenchWorld world) {
    super(world, OperationKind.Maximum, false);
  }

  @Override
  public IOperation.Element<Integer, Integer> performOperation(IWorm w) {
    int max= world.getNode(w.getHeadCoordinate(0)).getValue();;
    int idx = 0;
    for (int i = 1; i < w.getHeadLength(); i++) {
      int val = world.getNode(w.getHeadCoordinate(i)).getValue();
      if(val > max){
        max = val;
        idx = i;
      }
    }
    return new IOperation.Element<Integer, Integer>(idx, max);
  }
}
