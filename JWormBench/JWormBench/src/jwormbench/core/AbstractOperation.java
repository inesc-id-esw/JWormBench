package jwormbench.core;


public abstract class AbstractOperation<T> implements IOperation<T>{
  protected final IBenchWorld world;
  protected final OperationKind kind;
  protected final boolean isWorldUpdater;
  
  public AbstractOperation(IBenchWorld world, OperationKind kind, boolean isWorldUpdater) {
    this.world = world;
    this.kind = kind;
    this.isWorldUpdater = isWorldUpdater;
  }
  @Override
  public boolean isWorldModified(){
    return isWorldUpdater;
  }
  @Override
  public final OperationKind getKind(){
    return kind;
  }
}
