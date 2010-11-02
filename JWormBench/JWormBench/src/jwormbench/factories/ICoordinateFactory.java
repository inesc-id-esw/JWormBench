package jwormbench.factories;

import jwormbench.core.ICoordinate;

public interface ICoordinateFactory {

  public abstract ICoordinate make(int x, int y);

}