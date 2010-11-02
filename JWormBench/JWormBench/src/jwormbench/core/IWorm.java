package jwormbench.core;

public interface IWorm {
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------  PROPERTIES   ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * Name of the Worm
   */
  String getName();
  /**
   * Coordinate 0 is the head and BodyLength-1 is the tail
   */
  ICoordinate getBodyCoordinate(int i);
  /**
   * Head coordinates layout like a triangle.
   * The length of the head is equals to HeadSize*HeadSize 
   */
  ICoordinate getHeadCoordinate(int i);
  /**
   * Retrieves the previous body's coordinates.
   */
  ICoordinate getOldBodyCoordinate(int i);
  /**
   * Retrieves the previous head's coordinates.
   */
  ICoordinate getOldHeadCoordinate(int i);
  /**
   * If the BodyLength is 1 then the worm will store 2 coordinates instead of 1,
   * to calculate the Worm's direction.
   * The private calcNewCoordinate method requires a minimum of two coordinates to 
   * calculate the direction.
   * However the second coordinate will never appears in BenchWorld. 
   */
  int getBodyLength();
  /**
   * The length of head is equals to HeadSize*HeadSize   
   **/
  int getHeadLength();
  /**
   * Returns an array of int values of the world under the worm's head.  
   **/
  int [] getHeadValues();

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------    METHODS    ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Move all coordinates of the body and head to the direction given by
   * argument. 
   * @param direction Direction to move the worm's body and head.
   */
  void move(Direction direction);
  /**
   * Update node's under worm 'w' with its reference, just if those 
   * nodes already have that worm or if they are empty (no worm).
   * If a node already has a different worm then it throws 
   * NodeAlreadyOccupiedException.  
   */
  void updateWorldUnderWorm();
}
