package jwormbench.core;

public abstract class AbstractStep implements IStep{
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
   * Returns true if the world state is modified by this step.
   * In that case the result of performStep method is an Integer. 
   */
  public final boolean isWorldModified(){
    return op.isWorldModified();
  }
  /**
   * The kind of Operation.
   */
  public final OperationKind getOpKind(){
    return op.getKind();
  }
  /**
   * Direction taken by the worm moved by this step.
   */
  public Direction getDirection(){
    return direction;
  }

}
