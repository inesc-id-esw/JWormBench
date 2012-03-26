package jwormbench.app.config;

import jvstm.lockfree.Transaction;
import jwormbench.factories.INodeFactory;
import jwormbench.factories.IStepFactory;
import jwormbench.sync.jvstmdbllockfree.aom.TransactionalNodeFactory;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class JvstmLockfreeDblLayoutSyncModuleAOM extends AbstractModule{
    static{
	//
	// Print number of aborted transactions
	// 
	Runtime.getRuntime().addShutdownHook(new Thread() {
	    public void run() {
		System.out.println("Nr of aborted trxs: " + Transaction.nrOfAborts);;
	    }
	});
    }

    @Override
    protected void configure() {
	bind(IStepFactory.class)
	.to(jwormbench.sync.jvstmdbllockfree.JvstmStepFactory.class)
	.in(Singleton.class);
	bind(INodeFactory.class)
	.to(TransactionalNodeFactory.class)
	.in(Singleton.class);
    }
}
