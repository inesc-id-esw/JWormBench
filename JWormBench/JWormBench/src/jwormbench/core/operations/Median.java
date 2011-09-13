package jwormbench.core.operations;

import jwormbench.core.AbstractOperation;
import jwormbench.core.IBenchWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IOperation;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;

public class Median extends AbstractOperation<IOperation.Element<ICoordinate, Integer>>{
  public Median(IBenchWorld world) {
    super(world,OperationKind.Median, false);
  }
  /**
   * Original WormBench comment:
   * "Selection sort. Intentionally chosen assuming that one would use less
   * efficient sorting algorithm.
   */
  @Override
  public IOperation.Element<ICoordinate, Integer> performOperation(IWorm w) {
    ICoordinate [] sortedValues = new ICoordinate [w.getHeadLength()];
    for (int i = 0; i < sortedValues.length; i++) {
      sortedValues[i] = w.getHeadCoordinate(i);
    }
    for (int i = 0; i < sortedValues.length; i++) {  
      int min = world.getNode(sortedValues[i]).getValue();
      int minIndex = i;
      for (int j = i+1; j < sortedValues.length; j++) {
        if(world.getNode(sortedValues[j]).getValue() < min){
          min = world.getNode(sortedValues[j]).getValue();
          minIndex = j;
        }
      }
      ICoordinate minCoord = sortedValues[minIndex];
      sortedValues[minIndex] = sortedValues[i];
      sortedValues[i] = minCoord;
    }
    int median = world.getNode(sortedValues[sortedValues.length/2]).getValue();
    return new Element<ICoordinate, Integer>(sortedValues[sortedValues.length/2], median);
  }
}
