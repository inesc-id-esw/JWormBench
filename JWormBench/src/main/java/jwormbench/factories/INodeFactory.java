package jwormbench.factories;

import jwormbench.core.INode;


public interface INodeFactory {
  INode make(int initValue);
}