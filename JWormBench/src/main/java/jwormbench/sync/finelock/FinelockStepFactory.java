package jwormbench.sync.finelock;

import java.util.Arrays;
import java.util.Comparator;

import com.google.inject.Inject;

import jwormbench.core.ICoordinate;
import jwormbench.core.INode;
import jwormbench.core.IStep;
import jwormbench.core.Direction;
import jwormbench.core.IOperation;
import jwormbench.core.IWorld;
import jwormbench.core.IWorm;
import jwormbench.core.OperationKind;
import jwormbench.factories.AbstractStepFactory;
import jwormbench.factories.IOperationFactory;
import jwormbench.setup.IStepSetup;

public class FinelockStepFactory extends AbstractStepFactory{
  private IWorld world;
  @Inject
  public FinelockStepFactory(IStepSetup opsSetup, IOperationFactory opFac, IWorld world) {
    super(opsSetup, opFac);
    this.world = world;
  }

  @Override
  protected IStep factoryMethod(final IOperation<?> op, final Direction direction) {
    return new IStep(){
      public Direction getDirection(){return direction;}
      public OperationKind getOpKind() {return op.getKind();}
      public boolean isWorldModified() {return op.isWorldModified();}
      public Object performStep(IWorm worm) {
        //
        // Perform operation
        //
        ICoordinate [] coords = new ICoordinate[worm.getHeadLength()];
        for (int i = 0; i < worm.getHeadLength(); i++) {
          coords[i] = worm.getHeadCoordinate(i);
        }
        Arrays.sort(coords, new Comparator<ICoordinate>() {
          public int compare(ICoordinate o1, ICoordinate o2) {
            if(o1.getY() != o2.getY()) return o1.getY() - o2.getY();
            else return o1.getX() - o2.getX();
          }
        });
        Object res = performAtomicOperation(0, coords, worm);
        //
        // Move worm
        //
        worm.move(direction);
        worm.updateWorldUnderWorm();
        return res;
      }
      private Object performAtomicOperation(int headIdx, ICoordinate [] coords, IWorm worm) {
        if(headIdx >= worm.getHeadLength()){
          return op.performOperation(worm);
        }else{
          INode node = world.getNode(coords[headIdx]);
          synchronized (node) {
            return performAtomicOperation(++headIdx, coords, worm); 
          }
        }
      }      
    };
  }

}
