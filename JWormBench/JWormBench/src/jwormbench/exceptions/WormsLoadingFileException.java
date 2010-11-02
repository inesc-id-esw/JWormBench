package jwormbench.exceptions;


public class WormsLoadingFileException extends RuntimeException{

	/**
   * 
   */
  private static final long serialVersionUID = -3436545884472850198L;
  public WormsLoadingFileException(Exception e) {
		super(e);
	}
	public WormsLoadingFileException(String msg) {
		super(msg);
	}

}
