package jwormbench.factories;

import jwormbench.core.IOperation;
import jwormbench.core.OperationKind;

public interface IOperationFactory {

  <T> IOperation<T> make(OperationKind opKind);
  
}