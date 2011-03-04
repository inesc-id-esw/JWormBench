package jwormbench.core;

public interface IStep {
  /**
   * Direction taken by the worm moved by this step.
   */
  Direction getDirection();
  /**
   * The kind of Operation.
   */
  OperationKind getOpKind();
  /**
   * Returns true if the world state is modified by this step.
   * In that case the result of performStep method is an Integer. 
   */
  boolean isWorldModified();
  /**
   * If this step change the world state (the sum of all nodes values), 
   * then it will returns the difference of that value, to the previous 
   * state before change. In this case the result is an Integer.
   * Otherwise it will return the result of the performed operation.   
   */
  Object performStep(IWorm worm);
}
