package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;

public class Median extends AbstractOperation<IOperation.Element<ICoordinate, Integer>>{
  public Median(IWorld world) {
    super(world,OperationKind.Median, false);
  }
  /**
   * Original WormBench comment:
   * "Selection sort. Intentionally chosen assuming that one would use less
   * efficient sorting algorithm.
   */
  @Override
  public IOperation.Element<ICoordinate, Integer> performOperation(IWorm w) {
    int [] headValues = w.getHeadValues();
    Element<ICoordinate, Integer> [] sortedValues = new Element[headValues.length];
    for (int i = 0; i < sortedValues.length; i++) {
      sortedValues[i] = new Element<ICoordinate, Integer>(
          w.getHeadCoordinate(i), 
          headValues[i]);
    }
    for (int i = 0; i < sortedValues.length; i++) {  
      int min = sortedValues[i].value;
      int minIndex = i;
      for (int j = i+1; j < sortedValues.length; j++) {
        if(sortedValues[j].value < min){
          min = sortedValues[j].value;
          minIndex = j;
        }
      }
      Element<ICoordinate, Integer> minCoord = sortedValues[minIndex];
      sortedValues[minIndex] = sortedValues[i];
      sortedValues[i] = minCoord;
    }
    return sortedValues[sortedValues.length/2];
  }
}
