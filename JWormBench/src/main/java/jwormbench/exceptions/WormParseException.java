package jwormbench.exceptions;

public class WormParseException extends RuntimeException{

  /**
   * 
   */
  private static final long serialVersionUID = 4795615413243376318L;
  public WormParseException (Exception e) {
    super(e);
  }
  public WormParseException (String msg) {
    super(msg);
  }
}
