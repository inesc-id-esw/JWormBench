package jwormbench.defaults;

import org.deuce.transform.NoSync;

import jwormbench.core.ICoordinate;
import jwormbench.factories.ICoordinateFactory;

public class DefaultCoordinateFactory implements ICoordinateFactory {
  /**
   * @see jwormbench.factories.ICoordinateFactory#make(int, int)
   */
  public ICoordinate make(int x, int y){
    return new Coordinate(x, y);
  }
  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // --------------- NESTED TYPES  --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private static class Coordinate implements ICoordinate {
    @NoSync private int x;
    @NoSync private int y;
    /**
     * @see jwormbench.core.ICoordinate#getX()
     */
    public int getX() {
      return x;
    }
    /**
     * @see jwormbench.core.ICoordinate#setX(int)
     */
    public void setX(int x) {
      this.x = x;
    }
    /**
     * @see jwormbench.core.ICoordinate#getY()
     */
    public int getY() {
      return y;
    }
    /**
     * @see jwormbench.core.ICoordinate#setY(int)
     */
    public void setY(int y) {
      this.y = y;
    }
    public Coordinate(int x, int y) {
      super();
      this.x = x;
      this.y = y;
    }
  }
}
