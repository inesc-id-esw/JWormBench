package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class Transpose extends AbstractOperation<Integer>{ 
  IOperationFactory opsFac;
  public Transpose(IWorld world, IOperationFactory opsFac) {
    super(world, OperationKind.Transpose, false);
    this.opsFac = opsFac;
  }
  @Override
  public Integer performOperation(IWorm w) {
    int [] transposedValues = w.getHeadValues();
    
    for (int i = 0; i < transposedValues.length / 2; i++){
        int temp = transposedValues [i];
        transposedValues [i] = transposedValues [transposedValues.length - 1 -i];
        transposedValues[transposedValues.length - i - 1] = temp;
    }
    
    // WriteNodesUnderHead
    for (int i = 0; i < transposedValues.length; i++) {
     world.getNode(w.getHeadCoordinate(i)).setValue(transposedValues[i]); 
    }
    return transposedValues[0];
  }
}
