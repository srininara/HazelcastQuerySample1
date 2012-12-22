package com.nacnez.projects.hazelcast.query.sample1.distributed;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.hazelcast.core.HazelcastInstance;
import com.nacnez.projects.hazelcast.query.sample1.extend.CancellableMultiTask;
import com.nacnez.projects.hazelcast.query.sample1.extend.CancellationConstraint;
import com.nacnez.util.microbenchmarktool.TimedTask;
import com.nacnez.util.microbenchmarktool.core.PrototypeTimedTask;

public class CancellableDistributedExecTask<T>  extends PrototypeTimedTask {

	private HazelcastInstance instance;

	int execResult;
	
	private Callable<T> filter;

	private Reducer<T> reducer;

	private CancellableTaskCloneMaker<T> cloneMaker;

	private CancellationConstraint<T> cancelConstraint;

	private static final long serialVersionUID = 1L;

	public CancellableDistributedExecTask(HazelcastInstance instance, String name, Callable<T> filter, Reducer<T> reducer,CancellableTaskCloneMaker<T> cloneMaker,CancellationConstraint<T> cancelConstraint) {
		super(name);
		this.instance = instance;
		this.filter = filter;
		this.reducer = reducer;
		this.cloneMaker = cloneMaker;
		this.cancelConstraint = cancelConstraint;
	}
	
	public CancellationConstraint<T> cancelConstraint() {
		return cancelConstraint;
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
		return this.cloneMaker.make(instance,name(),filter,reducer,cloneMaker,cancelConstraint);
//		return new DistributedExecTask(instance, name(),filter,reducer);
	}

	private void queryByDistribution() throws Exception {
		CancellableMultiTask<T> task = new CancellableMultiTask<T>(filter(),
				instance.getCluster().getMembers(),cancelConstraint());
		ExecutorService executorService = instance.getExecutorService();
		executorService.execute(task);
		Collection<T> results = task.get();
		execResult = reducer.reduce(results);
		

	}

	public boolean idemPotent() {
		return false;
	}

}
