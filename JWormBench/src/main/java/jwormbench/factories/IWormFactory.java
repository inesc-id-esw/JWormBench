package jwormbench.factories;


import java.util.Collection;

import jwormbench.core.IWorm;


public interface IWormFactory {

  public abstract Collection<IWorm> make();
}