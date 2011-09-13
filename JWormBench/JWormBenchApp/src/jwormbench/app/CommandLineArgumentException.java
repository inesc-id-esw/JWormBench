package jwormbench.app;

public class CommandLineArgumentException extends RuntimeException{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public CommandLineArgumentException(String errorMessage) {
    super(errorMessage);
  }
}
