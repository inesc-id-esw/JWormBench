package jwormbench.gui.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JComponent;

import jwormbench.core.IWorld;
import jwormbench.core.IWorm;

public class BenchWorldPanel extends JComponent{
  /**
   * 
   */
  private static final long serialVersionUID = 2981543563290801066L;
  private static final Color FREE = Color.WHITE;
  private static final Color WORM_MOVING = new Color(255, 140, 50);
  private static final Random r = new Random();
  private final int DEFAULT_NODE_WIDTH = 4;
  private final int DEFAULT_NODE_HEIGTH = 4;
  private final Map<IWorm, Color> wormColors;
  private IWorm movingWorm;
  
  private IWorld world;
  public BenchWorldPanel(Iterable<IWorm> worms, IWorld world) {
    this.world = world;
    wormColors = new HashMap<IWorm, Color>();
    for (IWorm w : worms) {
      wormColors.put(w, new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
    }
  }
  public void setMovingWorm(IWorm w){
    movingWorm = w;
  }
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(
        world.getColumnsNum()*DEFAULT_NODE_WIDTH, 
        world.getRowsNum() * DEFAULT_NODE_HEIGTH);
  }
  @Override
  protected void paintComponent(Graphics g) {
    // Optimise to just update the Clip Bounds
    
    double nodeW = ((double)getWidth())/world.getColumnsNum();
    double nodeH = ((double)getHeight())/world.getRowsNum();
    double nextX  = 0, nextY = 0;
    for (int i = 0; i < world.getRowsNum(); i++) {
      for (int j = 0; j < world.getColumnsNum(); j++) {
        IWorm w = world.getNode(i, j).getWorm() ;
        g.setColor(w == null? FREE: w == movingWorm? WORM_MOVING: wormColors.get(w));
        g.fill3DRect((int)nextX, (int)nextY, (int)nodeW, (int)nodeH, true);
        nextY += nodeH;
      }
      nextX += nodeW;
      nextY = 0;
    }
  }
}
