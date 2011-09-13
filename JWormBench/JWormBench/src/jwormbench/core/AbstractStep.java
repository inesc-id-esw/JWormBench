package jwormbench.core;

public abstract class AbstractStep {
  /**
   * Direction taken by the worm moved by this step.
   */
  public final Direction direction;
  /**
   * Operation performed on the worm by this step.
   */
  public final IOperation<?> op;
  /**
   * Takes by parameter the direction and operation 
   * that will be performed by this step.
   */
  public AbstractStep(Direction direction, IOperation<?> op) {
    this.direction = direction;
    this.op = op;
  }
  /**
   * If this step change the world state (the sum of all nodes values), 
   * then it will returns the difference of that value, to the previous 
   * state before change. In this case the result is an Integer.
   * Otherwise it will return the result of the performed operation.   
   */
  public abstract Object performStep(IWorm worm);
}
