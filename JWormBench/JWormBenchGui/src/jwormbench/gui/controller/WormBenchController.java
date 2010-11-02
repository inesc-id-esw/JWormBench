package jwormbench.gui.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jwormbench.core.Direction;
import jwormbench.core.IBenchWorld;
import jwormbench.core.IWorm;
import jwormbench.gui.views.BenchWorldPanel;
import jwormbench.gui.views.WormControlsPanel;

public class WormBenchController implements ActionListener{
  private JFrame frm;
  private Map<String, IWorm> worms;
  WormControlsPanel wormsPanel;
  BenchWorldPanel worldPanel;
  public  WormBenchController(final Iterable<IWorm> worms, IBenchWorld world){
    frm = new JFrame("WormBenchController");
    this.worms = new HashMap<String, IWorm>();
    for (IWorm w : worms) {
      this.worms.put(w.getName(), w);
    }
    //
    // CenterPanel
    //
    frm.add(worldPanel = new BenchWorldPanel(worms, world), BorderLayout.CENTER);
    //
    // North panel
    // 
    wormsPanel = new WormControlsPanel(this.worms.keySet(), this);
    frm.add(wormsPanel, BorderLayout.NORTH);
  }
  public void showFrane(){
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        frm.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frm.pack();
        frm.setVisible(true);
      }
    });
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    IWorm w = worms.get(wormsPanel.getWormName());
    if(e.getActionCommand().equals("Ahead")) w.move(Direction.Ahead);
    if(e.getActionCommand().equals("Right"))w.move(Direction.Right);
    if(e.getActionCommand().equals("Left"))w.move(Direction.Left);
    w.updateWorldUnderWorm();
    worldPanel.setMovingWorm(w);
    worldPanel.repaint();    
  }
}
