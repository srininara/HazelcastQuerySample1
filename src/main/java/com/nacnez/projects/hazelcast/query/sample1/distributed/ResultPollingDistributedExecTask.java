package com.nacnez.projects.hazelcast.query.sample1.distributed;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.MultiTask;
import com.nacnez.projects.grid.model.Person;
import com.nacnez.projects.hazelcast.query.sample1.filter.ResultPublishingBangalorePersonFilter;
import com.nacnez.util.microbenchmarktool.TimedTask;
import com.nacnez.util.microbenchmarktool.core.PrototypeTimedTask;

public class ResultPollingDistributedExecTask<T>  extends PrototypeTimedTask {

	private HazelcastInstance instance;
	
	IQueue<Person> results;

	int execResult;
	
	private static final long serialVersionUID = 1L;

	public ResultPollingDistributedExecTask(HazelcastInstance instance, String name) { //, Callable<T> filter, Reducer<T> reducer,CloneMaker<T> cloneMaker
		super(name);
		this.instance = instance;
	}
	
	public Callable<Collection<Person>> filter() {
		results = this.instance.getQueue("results");
		return new ResultPublishingBangalorePersonFilter();
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
		return new ResultPollingDistributedExecTask<T>(instance, name());
	}

	private void queryByDistribution() throws Exception {
		MultiTask<Collection<Person>> task = new MultiTask<Collection<Person>>(filter(),
				instance.getCluster().getMembers());
		ExecutorService executorService = instance.getExecutorService();
		executorService.execute(task);
		execResult = 0;
		Collection<Person> output = new HashSet<Person>();
		while(true) {
			if (execResult>=10) {
				break;
			}
			Person p = results.take();
			output.add(p);
			execResult++;
		}
		task.cancel(true);
		for (Person p : output) {
			System.out.println("Person Data: " + p.getFirstName() + " : " + p.getLastName() + " : " + p.getCity());
		}
		results.destroy();
	}

	public boolean idemPotent() {
		return false;
	}

}
