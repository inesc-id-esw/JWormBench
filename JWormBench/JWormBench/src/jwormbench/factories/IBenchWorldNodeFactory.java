package jwormbench.factories;

import jwormbench.core.IBenchWorldNode;


public interface IBenchWorldNodeFactory {
  IBenchWorldNode make(int initValue);
}