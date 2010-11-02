/*
 * JWormBench: a Java benchmark based on WormBench - a synthetic 
 * workload for Transactinal Memory Systems Center www.bscmsrc.eu.
 * Copyright (C) 2010 INESC-ID Software Engineering Group
 * http://www.esw.inesc-id.pt
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author's contact:
 * INESC-ID Software Engineering Group
 * Rua Alves Redol 9
 * 1000 - 029 Lisboa
 * Portugal
 */
package jwormbench.core;

import jwormbench.exceptions.NodeAlreadyOccupiedException;
import jwormbench.factories.ICoordinateFactory;


public abstract class AbstractWorm implements IWorm{
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public final int id;
  public final int groupId;
  public final String name;
  public final int headSize;
  public final int speed;
  //
  // The case when bodyLength != body.Length is when we want a worm with
  // body of length 1. In this case we again have body[0] and body[1] cells
  // but use body[1] for finding the orientation of the worm. This does not
  // have any influence except writing the cells under the body - then
  // we don't update the cell corresponding to body[1].
  //
  public final int bodyLength;
  protected final IBenchWorld world;
  private ICoordinateFactory coordFac;
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  public AbstractWorm(int id, int headSize, int speed, int groupId,
      int bodyLength, String name, IBenchWorld world, ICoordinateFactory coordFac){
    super();
    this.id = id;
    this.groupId = groupId;
    this.name = name;
    this.headSize = headSize;
    this.speed = speed;
    this.bodyLength = bodyLength;
    this.world = world;
    this.coordFac = coordFac;
  }
  @Override
  public int getBodyLength() {
    return bodyLength;
  }

  @Override
  public int getHeadLength() {
    return headSize*headSize;
  }
  @Override
  public int [] getHeadValues(){
    int [] values = new int[getHeadLength()];
    for (int i = 0; i < values.length; i++) {
      values[i] = world.getNode(getHeadCoordinate(i)).getValue();
    }
    return values;
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ----------------- PUBLIC METHODS   ---------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * Move the body of the worm. 
   * The original move method in WormBench has two atomic parts: 
   * - 1st - Changing the coordinates of the Worm's body (<=> moveBody).
   * - 2nd - Write on the world nodes under the body (<=> updateWorldUnderWorm()).
   * 
   *  However, the 1st operation isolated does not change any global state, 
   *  so there is no way to abort this transaction.
   *  And the 2nd operation should be atomic together with the 1st one. 
   *  It does not make sense to interleave the 1st and 2nd operation with 
   *  the same operations on a different worm.
   *  
   *  So we leave the decision to the caller to make move() operation atomic, or not.   
   */
  public void move(Direction direction){
    ICoordinate newCoordinate = calcNewCoordinate(direction);
    moveBody(newCoordinate);
    updateHeadCoordinates();
  }
  /**
   * If a node has already a different worm then it throws 
   * NodeAlreadyOccupiedException => THIS VERIFICATION IS COMMMENTED!!! 
   */
  @Override
  public void updateWorldUnderWorm() {
    //
    // Check if the nodes are all free or occupied by the worm 'w'.
    // For the body we just have to check the first node coordinates, 
    // because just that one will move to a new node; all the others will replace 
    // the next node in front.
    //
    /*
    IWorm wormOnNode = world.getNode(getBodyCoordinate(0).getX(), getBodyCoordinate(0).getY()).getWorm();
    if(wormOnNode  != null && wormOnNode != this)
      throw new NodeAlreadyOccupiedException();
      */
    //
    // Check the head's worm.
    //
    /*
    for (int i = 0; i < getHeadLength(); i++) {
      wormOnNode = world.getNode(getHeadCoordinate(i).getX(), getHeadCoordinate(i).getY()).getWorm();
      if(wormOnNode  != null && wormOnNode != this)
        throw new NodeAlreadyOccupiedException();
    } 
    */           
    //
    // Clean old worm's coordinates.
    // 1. Clean just the last coordinate of the worm's body, corresponding to the tail.
    // 2. Clean all old coordinates of the worm's head.
    //
    world.getNode(getOldBodyCoordinate(getBodyLength()-1).getX(), 
        getOldBodyCoordinate(getBodyLength()-1).getY()).setWorm(null);
    for (int i = 0; i < getHeadLength(); i++) {
      world.getNode(getOldHeadCoordinate(i).getX(), getOldHeadCoordinate(i).getY()).setWorm(null); 
    }
    //
    // Update world under the body's worm.
    // We have to rewrite all body's coordinates (and not just the 0 index) because
    // the clean operation of the head's old coordinates could erase
    // some body coordinates of the world.
    //
    for (int i = 0; i < getBodyLength(); i++) {
      world.getNode(getBodyCoordinate(i).getX(), getBodyCoordinate(i).getY()).setWorm(this);
    } 
    //
    // Update world under the head's worm
    //
    for (int i = 0; i < getHeadLength(); i++) {
      world.getNode(getHeadCoordinate(i).getX(), getHeadCoordinate(i).getY()).setWorm(this); 
    }            
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // --------------- PROTECETD  METHODS   -------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  protected abstract void moveBody(ICoordinate newCoordinate);
  
  protected final void updateHeadCoordinates(){
    int diffx = getBodyCoordinate(0).getX() - getBodyCoordinate(1).getX();
    int diffy = getBodyCoordinate(0).getY() - getBodyCoordinate(1).getY();           
    int headIndex = 0;
    for (int i = 0; i <= headSize; i++){
      for (int j = -headSize + i + 1; j <= headSize - i - 1; j++){
        int x = 0;
        int y = 0;
        if (diffx == 1 && diffy == 0){
          //
          // Up.
          //

          x = getBodyCoordinate(0).getX() + i;
          y = getBodyCoordinate(0).getY() + j; 

        }
        else if (diffx == -1 && diffy == 0){
          //
          // Down.
          //
          x = getBodyCoordinate(0).getX() - i;
          y = getBodyCoordinate(0).getY() + j;
        }
        else if (diffx == 0 && diffy == 1){
          //
          // Right.
          //
          x = getBodyCoordinate(0).getX() + j;
          y = getBodyCoordinate(0).getY() + i;
        }
        else if (diffx == 0 && diffy == -1){
          //
          // Left.
          //
          x = getBodyCoordinate(0).getX() + j;
          y = getBodyCoordinate(0).getY() - i;
        }
        getOldHeadCoordinate(headIndex).setX(getHeadCoordinate(headIndex).getX());
        getOldHeadCoordinate(headIndex).setY(getHeadCoordinate(headIndex).getY());
        getHeadCoordinate(headIndex).setX(x);
        getHeadCoordinate(headIndex).setY(y);
        headIndex++;
      }
    }
  }  
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ----------------- PRIVATE METHODS   --------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  private ICoordinate calcNewCoordinate(Direction direction) {
    int newX = 0, newY = 0;
    int diffx = getBodyCoordinate(0).getX() - getBodyCoordinate(1).getX();
    int diffy = getBodyCoordinate(0).getY() - getBodyCoordinate(1).getY();
    switch (direction){
      case Ahead:{
        newX = Math.abs(diffx) * (getBodyCoordinate(0).getX() + speed * diffx) + Math.abs(diffy) * getBodyCoordinate(0).getX();
        newY = Math.abs(diffy) * (getBodyCoordinate(0).getY() + speed * diffy) + Math.abs(diffx) * getBodyCoordinate(0).getY();
      } break;
      case Right:{
        newX = Math.abs(diffy) * (getBodyCoordinate(0).getX() - speed * diffy) + Math.abs(diffx) * getBodyCoordinate(0).getX();
        newY = Math.abs(diffx) * (getBodyCoordinate(0).getY() - speed * diffx) + Math.abs(diffy) * getBodyCoordinate(0).getY();
      } break;
      case Left:{
        newX = Math.abs(diffy) * (getBodyCoordinate(0).getX() + speed * diffy) + Math.abs(diffx) * getBodyCoordinate(0).getX();
        newY = Math.abs(diffx) * (getBodyCoordinate(0).getY() + speed * diffx) + Math.abs(diffy) * getBodyCoordinate(0).getY();
      } break;
    }

    return coordFac.make(newX, newY);
  }
}
