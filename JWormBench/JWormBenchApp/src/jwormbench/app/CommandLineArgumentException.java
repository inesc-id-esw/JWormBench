package jwormbench.app;

public class CommandLineArgumentException extends RuntimeException{

  public CommandLineArgumentException(String errorMessage) {
    super(errorMessage);
  }
}
