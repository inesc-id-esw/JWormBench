package jwormbench.sync.deuce;

import jwormbench.core.INode;
import jwormbench.setup.IWorlSetup;

import org.deuce.transform.Exclude;

@Exclude
public class NodesMatrix {
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // ---------------------- FIELDS --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  final INode[][] world;
  public NodesMatrix(final IWorlSetup bwSetup) {
    INode[][] aux  = bwSetup.loadWorld();
    world = new INode[aux.length][aux[0].length];
    for (int i = 0; i < aux.length; i++) {
      for (int j = 0; j < aux[i].length; j++) {
        world[i][j] = aux[i][j];
      }
    }
  }
  public INode getNode(int x, int y){
    return world[x][y];
  }
  public int getRowsNum(){
    return world.length;
  }
 public int getColsNum(){
    return world[0].length;
  }
}
