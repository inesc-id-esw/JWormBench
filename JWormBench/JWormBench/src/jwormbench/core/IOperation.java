package jwormbench.core;


public interface IOperation<T>{
  /**
   * The kind of Operation.
   */
  OperationKind getKind();
  /**
   * If this operation changes the world state (the sum of all nodes values), 
   * then it will returns the difference of world's state, to its previous 
   * state before change => In that case T is an Integer.
   * Otherwise it will return the result of the performed operation => An instance of T.
   */
  T performOperation(IWorm w);
  /**
   * Returns true if the world state is modified by this operation.
   * In that case the result of peformOperation method is an Integer. 
   */
  boolean isWorldModified();
  /**
   * Auxiliary type returned by some operations. 
   */
  public class Element<K, V>{
    public final K idx;
    public final V value;
    public Element(K idx, V value) {
      super();
      this.idx = idx;
      this.value = value;
    } 
  }
}
