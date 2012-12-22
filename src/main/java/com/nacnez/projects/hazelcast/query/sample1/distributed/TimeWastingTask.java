package com.nacnez.projects.hazelcast.query.sample1.distributed;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiTask;
import com.nacnez.projects.hazelcast.query.sample1.filter.TimeWastingCallable;
import com.nacnez.util.microbenchmarktool.TimedTask;
import com.nacnez.util.microbenchmarktool.core.PrototypeTimedTask;

public class TimeWastingTask extends PrototypeTimedTask {

	private static final long serialVersionUID = 1L;
	String output;
	private HazelcastInstance instance;

	// int execResult;

	public TimeWastingTask(HazelcastInstance instance, String name) {
		super(name);
		this.instance = instance;
	}

	@Override
	public boolean idemPotent() {
		return false;
	}

	@Override
	public void doTask() {
		try {
			output = doTimeWasteTask();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String completedExecutionMessage() {
		return output;
	}

	@Override
	public TimedTask clone() {
		return new TimeWastingTask(instance, name());
	}

	private String doTimeWasteTask() throws InterruptedException,
			ExecutionException {
		MultiTask<Integer> task = new MultiTask<Integer>(
				new TimeWastingCallable(), instance.getCluster().getMembers());
		ExecutorService executorService = instance.getExecutorService();
		executorService.execute(task);
	
		try {
			task.get(1, TimeUnit.SECONDS);
		} catch (TimeoutException te) {
			task.cancel(true);
			System.out.println("Cancelled");
		}

		return "Count: " + 0;
	}

}
