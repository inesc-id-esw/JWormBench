package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IBenchWorld;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.IOperationFactory;

public class Sort extends AbstractOperation<Integer>{ 
  IOperationFactory opsFac;
  public Sort(IBenchWorld world, IOperationFactory opsFac) {
    super(world, OperationKind.Sort, false);
    this.opsFac = opsFac;
  }
  @Override
  public Integer performOperation(IWorm w) {
    int [] sortedValues = w.getHeadValues();
    //
    //Original WormBench comment:
    //"Selection sort. Intentionally chosen assuming that one would use less
    //efficient sorting algorithm."
    //
    for (int i = 0; i < sortedValues.length; i++) {
      int min = sortedValues[i];
      int minIndex = i;
      for (int j = i+1; j < sortedValues.length; j++) {
        if (sortedValues[j] < min){
            min = sortedValues[j];
            minIndex = j;
        }
      }
      sortedValues[minIndex] = sortedValues[i];
      sortedValues[i] = min;
    }
    // WriteNodesUnderHead
    for (int i = 0; i < sortedValues.length; i++) {
     world.getNode(w.getHeadCoordinate(i)).setValue(sortedValues[i]); 
    }
    return sortedValues[0];
  }
}
