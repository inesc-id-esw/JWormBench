package jwormbench.factories;


import jwormbench.core.IWorm;


public interface IWormFactory {

  public abstract Iterable<IWorm> make(int nrOfWorms);
}