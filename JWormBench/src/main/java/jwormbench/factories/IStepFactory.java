package jwormbench.factories;

import java.util.List;

import jwormbench.core.IStep;

public interface IStepFactory {

  List<IStep> make();

}