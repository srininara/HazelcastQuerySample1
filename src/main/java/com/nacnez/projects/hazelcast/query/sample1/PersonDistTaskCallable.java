package com.nacnez.projects.hazelcast.query.sample1;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiTask;

public class PersonDistTaskCallable implements Callable<Integer> {

	private HazelcastInstance instance;

	public PersonDistTaskCallable(HazelcastInstance instance) {
		this.instance = instance;
	}

	public Integer call() throws Exception {
		int execResult = 0;
		try {
			MultiTask<Integer> task = new MultiTask<Integer>(
					new PersonFilter(), instance.getCluster().getMembers());
			ExecutorService executorService = instance.getExecutorService();
			executorService.execute(task);
			Collection<Integer> results = task.get();
			for (int result : results) {
				execResult = execResult + result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return execResult;

	}

}
