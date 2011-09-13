package jwormbench.factories;

import java.util.List;

import jwormbench.core.AbstractStep;

public interface IStepFactory {

  List<AbstractStep> make();

}