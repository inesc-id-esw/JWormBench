package app;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jwormbench.core.IWorld;
import jwormbench.core.IWorm;
import jwormbench.defaults.World;
import jwormbench.defaults.DefaultNodeFactory;
import jwormbench.defaults.DefaultCoordinateFactory;
import jwormbench.defaults.DefaultWormFactory;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.gui.controller.WormBenchController;
import jwormbench.setup.WorldFileLoader;
import jwormbench.setup.IWormsSetup;
import jwormbench.setup.WormsFileLoader;

public class Program {

  static void launchDialog(
      final JComponent cmp, final boolean modal, final String title){
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JFrame dialog = new JFrame();
        dialog.add(cmp, BorderLayout.CENTER);
        // dialog.setModal(modal);
        dialog.setTitle(title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setVisible(true);
      }
    });
  }
  public static void main(String[] args) {
    final String WORLD_CONFIG_FILE = "config/512.txt";
    // final String WORM_CONFIG_FILE = "config/W-B[1.8]-H[4.16]-32.txt";
    // final String WORM_CONFIG_FILE = "config/W-B[1.8]-H[1.8]-128.txt";
    final String WORM_CONFIG_FILE = "config/W-B[1.1]-H[2.16]-512.txt";
    // final String WORM_CONFIG_FILE = "config/genome_W-B[1.1]-H[32.32]-52.txt";
   
    ICoordinateFactory cordFac =  new DefaultCoordinateFactory();
    IWormsSetup wormSetup = new WormsFileLoader(WORM_CONFIG_FILE, cordFac);
    
    
    IWorld world = new World(
        new WorldFileLoader(
            WORLD_CONFIG_FILE, new DefaultNodeFactory()));
    IWormFactory wormFac = new DefaultWormFactory(cordFac, world, wormSetup);
    Iterable<IWorm> worms = wormFac.make();
    
    WormBenchController ctr = new WormBenchController(worms, world);
    ctr.showFrane();
  }
}
