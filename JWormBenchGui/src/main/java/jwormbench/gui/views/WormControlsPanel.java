package jwormbench.gui.views;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;


public class WormControlsPanel extends JPanel {
  /**
   * 
   */
  private static final long serialVersionUID = -995046351962313856L;
  private JButton btAhead, btRight, btLeft;
  private JComboBox boxWormNames;
  public WormControlsPanel(Collection<String> wormNames, ActionListener handler){
    //
    // Worm Names
    // 
    String [] names = new String[wormNames.size()];
    Arrays.sort(wormNames.toArray(names));
    boxWormNames = new JComboBox(names);
    add(boxWormNames);
    //
    // Control buttons
    //
    btAhead = new JButton("Ahead");
    btRight = new JButton("Right");
    btLeft = new JButton("Left");
    btAhead.addActionListener(handler);
    btLeft.addActionListener(handler);
    btRight.addActionListener(handler);
    add(btAhead); add(btRight); add(btLeft);
  }
  public String getWormName(){
    return boxWormNames.getSelectedItem().toString();
  }
}

