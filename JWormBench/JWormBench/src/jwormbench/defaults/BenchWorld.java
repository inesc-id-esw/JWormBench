package jwormbench.defaults;

import com.google.inject.Inject;

import jwormbench.core.IBenchWorld;
import jwormbench.core.IBenchWorldNode;
import jwormbench.core.ICoordinate;
import jwormbench.setup.IBenchWorlSetup;

/**
 * Abstracts the BenchWorld - the underlying data structure that
 * represents the matrix so that it can be changed. 
 * 
 * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
 */
public class BenchWorld implements IBenchWorld {
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  final IBenchWorldNode[][] world;
  final int rowsNum;
  final int columnsNum;
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------   CONSTRUCTOR ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  @Inject
  public BenchWorld(IBenchWorlSetup bwSetup) {
    this.world = bwSetup.loadWorld();
    rowsNum = world.length;
    columnsNum = world[0].length;
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // -------------------  PROPERTIES   ----------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * @see jwormbench.core.IBenchWorld#getRowsNum()
   */
  public int getRowsNum() {
    return rowsNum;
  }
  /**
   * @see jwormbench.core.IBenchWorld#getColumnsNum()
   */
  public int getColumnsNum() {
    return columnsNum;
  }
  /**
   * @see jwormbench.core.IBenchWorld#getNode(int, int)
   */
  public IBenchWorldNode getNode(int x, int y){
    x = x % rowsNum;
    y = y % columnsNum;
    if (x < 0) x = x + rowsNum;
    if (y < 0) y = y + columnsNum;
    return world[x][y];
  }
  /**
   * @see jwormbench.core.IBenchWorld#getNode(jwormbench.core.ICoordinate)
   */
  public IBenchWorldNode getNode(ICoordinate c){
    return getNode(c.getX(), c.getY());
  }
  @Override
  public int getSumOfAllNodes() {
    int total = 0;
    for (int i = 0; i < getColumnsNum(); i++) {
      for (int j = 0; j < getRowsNum(); j++) {
        total += world[i][j].getValue();
      }
    }
    return total;
  }
}

