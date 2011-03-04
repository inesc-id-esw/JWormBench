package jwormbench.exceptions;

public class OperationsLoadingFileException extends RuntimeException{

  /**
   * 
   */
  private static final long serialVersionUID = 539350045336795597L;

  public OperationsLoadingFileException(Exception e) {
    super(e);
  }

  public OperationsLoadingFileException(String msg) {
    super(msg);
  }
}
