package jwormbench.defaults;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.inject.Inject;

import jwormbench.core.AbstractWorm;
import jwormbench.core.IWorld;
import jwormbench.core.ICoordinate;
import jwormbench.core.IWorm;
import jwormbench.factories.ICoordinateFactory;
import jwormbench.factories.IWormFactory;
import jwormbench.setup.IDisposable;
import jwormbench.setup.IWormsSetup;
import jwormbench.setup.IWormsSetup.WormProperties;


public class DefaultWormFactory implements IWormFactory {
  private ICoordinateFactory coordFac;
  private IWorld world;
  private IWormsSetup wormsSetup;
  
  @Inject
  public DefaultWormFactory(ICoordinateFactory coordFac, IWorld world, IWormsSetup wormsSetup){
    this.coordFac = coordFac;
    this.world = world;
    this.wormsSetup = wormsSetup;
  }
  /**
   * @see jwormbench.factories.IWormFactory#make()
   */
  public Collection<IWorm> make(){
    LinkedList<IWorm> worms = new LinkedList<IWorm>();
    Iterator<WormProperties> iterator = null;
    try{
      iterator = wormsSetup.iterator(); 
      while(iterator.hasNext()) {
        WormProperties p = iterator.next();
        worms.add(
            new Worm(p.id, p.headSize, p.speed, p.groupId, p.bodyLength, p.name, p.body, p.head, world, coordFac));
      }
    }
    finally{
     if(iterator instanceof IDisposable)
       ((IDisposable) iterator).dispose();
    }
    return worms;
  }
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // --------------- NESTED TYPES  --------------------- 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * Abstracts the worm object. This is an active object - has a correspondent thread. 
   * 
   * @author F. Miguel Carvalho mcarvalho[@]cc.isel.pt 
   */
  private static class Worm extends AbstractWorm{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ---------------------- FIELDS --------------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // index 0 is in the Worm's head and (length-1)'s 
    // index is the tail.
    //
    private final ICoordinate[] body, oldBody;
    //
    // The mapping between the headBuffer indices and the original coordinates
    // in BenchWorld. This is updated when buffer is being read.
    //
    private final ICoordinate[] head, oldHead;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // -------------------   CONSTRUCTOR ----------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Worm(int id, int headSize, int speed, int groupId,
        int bodyLength, String name, ICoordinate[] body, 
        ICoordinate[] head, IWorld world, ICoordinateFactory coordFac){
      super(id, headSize, speed, groupId,bodyLength, name, world, coordFac);
      this.body = body;
      this.head = head;
      this.oldBody = new ICoordinate[body.length];
      for (int i = 0; i < oldBody.length; i++) {
        oldBody[i] = coordFac.make(0, 0);
      }
      this.oldHead = new ICoordinate[head.length]; // head.length = headSize*headSize
      for (int i = 0; i < oldHead.length; i++) {
        oldHead[i] = coordFac.make(0, 0);
      }
      updateHeadCoordinates();
      //
      // update world under the body's worm
      //
      for (int i = 0; i < getBodyLength(); i++) {
        world.getNode(getBodyCoordinate(i).getX(), getBodyCoordinate(i).getY()).setWorm(this);
      }
      //
      // update world under the head's worm
      //
      for (int i = 0; i < getHeadLength(); i++) {
        world.getNode(getHeadCoordinate(i).getX(), getHeadCoordinate(i).getY()).setWorm(this); 
      }            
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // -------------------  PROPERTIES   ----------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public ICoordinate getBodyCoordinate(int i){
      return body[i];
    }
    @Override
    public ICoordinate getHeadCoordinate(int i) {
      return head[i];
    }
    @Override
    public ICoordinate getOldBodyCoordinate(int i) {
      return oldBody[i];
    }

    @Override
    public ICoordinate getOldHeadCoordinate(int i) {
      // TODO Auto-generated method stub
      return oldHead[i];
    }
    @Override
    public String getName() {
      return name;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // ---------------- PROTECTED METHODS ---------------- 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    @Override
    protected void moveBody(ICoordinate newCoordinate){
      ICoordinate oldCoordinate;
      for (int i = 0; i < body.length; i++){
          oldBody[i]= oldCoordinate = body[i];
          body[i] = newCoordinate;
          newCoordinate = oldCoordinate;
      }
    }
  }
}
