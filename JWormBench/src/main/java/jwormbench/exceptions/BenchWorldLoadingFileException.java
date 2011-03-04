package jwormbench.exceptions;


public class BenchWorldLoadingFileException extends RuntimeException{

	/**
   * 
   */
  private static final long serialVersionUID = 4758064862163162443L;
  public BenchWorldLoadingFileException(Exception e) {
		super(e);
	}
	public BenchWorldLoadingFileException(String msg) {
		super(msg);
	}

}
