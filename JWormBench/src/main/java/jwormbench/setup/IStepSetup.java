package jwormbench.setup;

import java.util.Iterator;

import jwormbench.core.Direction;
import jwormbench.core.OperationKind;

public interface IStepSetup extends Iterable<IStepSetup.OperationProperties>{
  Iterator<IStepSetup.OperationProperties> iterator();

  public static class OperationProperties{
    public final OperationKind operationKind;
    public final Direction direction;
    public OperationProperties(OperationKind operationKind, Direction direction) {
      super();
      this.operationKind = operationKind;
      this.direction = direction;
    }
  }
}