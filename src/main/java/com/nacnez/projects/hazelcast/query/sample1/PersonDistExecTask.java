package com.nacnez.projects.hazelcast.query.sample1;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiTask;
import com.nacnez.util.microbenchmarktool.TimedTask;
import com.nacnez.util.microbenchmarktool.core.PrototypeTimedTask;

public class PersonDistExecTask extends PrototypeTimedTask {

	private HazelcastInstance instance;

	int execResult;

	private static final long serialVersionUID = 1L;

	public PersonDistExecTask(HazelcastInstance instance, String name) {
		super(name);
		this.instance = instance;
	}

	public void doTask() {
		try {
			queryByDistribution();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String completedExecutionMessage() {
		return " Returned " + this.execResult + " results. ";

	}

	@Override
	public TimedTask clone() {
		return new PersonDistExecTask(instance, name());
	}

	private void queryByDistribution() throws Exception {
		MultiTask<Integer> task = new MultiTask<Integer>(new PersonFilter(),
				instance.getCluster().getMembers());
		ExecutorService executorService = instance.getExecutorService();
		executorService.execute(task);
		Collection<Integer> results = task.get();
		for (int result : results) {
			execResult = execResult + result;
		}
	}

	public boolean idemPotent() {
		return true;
	}

}
