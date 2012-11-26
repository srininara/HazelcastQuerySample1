package com.nacnez.projects.hazelcast.query.sample1.distributed;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiTask;
import com.nacnez.util.microbenchmarktool.TimedTask;
import com.nacnez.util.microbenchmarktool.core.PrototypeTimedTask;

public class DistributedExecTask<T>  extends PrototypeTimedTask {

	private HazelcastInstance instance;

	int execResult;
	
	private Callable<T> filter;

	private Reducer<T> reducer;

	private CloneMaker<T> cloneMaker;

	private static final long serialVersionUID = 1L;

	public DistributedExecTask(HazelcastInstance instance, String name, Callable<T> filter, Reducer<T> reducer,CloneMaker<T> cloneMaker) {
		super(name);
		this.instance = instance;
		this.filter = filter;
		this.reducer = reducer;
		this.cloneMaker = cloneMaker;
	}
	
	public Callable<T> filter() {
		return filter;
	}
	
	public Reducer<T> reducer() {
		return reducer;
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
		return this.cloneMaker.make(instance,name(),filter,reducer,cloneMaker);
//		return new DistributedExecTask(instance, name(),filter,reducer);
	}

	private void queryByDistribution() throws Exception {
		MultiTask<T> task = new MultiTask<T>(filter(),
				instance.getCluster().getMembers());
		ExecutorService executorService = instance.getExecutorService();
		executorService.execute(task);
		Collection<T> results = task.get();
		execResult = reducer.reduce(results);
		

	}

	public boolean idemPotent() {
		return true;
	}

}
