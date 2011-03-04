package jwormbench.exceptions;

public class NodeAlreadyOccupiedException extends RuntimeException{

  /**
   * 
   */
  private static final long serialVersionUID = -2994814622448515835L;
  public NodeAlreadyOccupiedException(String msg) {
    super(msg);
  }
  public NodeAlreadyOccupiedException() {
  }

}
