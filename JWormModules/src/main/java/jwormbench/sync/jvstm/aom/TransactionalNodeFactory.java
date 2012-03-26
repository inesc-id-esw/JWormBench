package jwormbench.sync.jvstm.aom;

import jwormbench.core.INode;
import jwormbench.factories.INodeFactory;

public class TransactionalNodeFactory implements INodeFactory{
    @Override
    public INode make(int initValue) {
	return new TransactionalNode(initValue);
    }
}
