package com.nacnez.projects.hazelcast.query.sample1.distributed;

import java.util.Collection;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.nacnez.projects.grid.model.Person;
import com.nacnez.projects.hazelcast.query.sample1.extend.CancellationConstraint;
import com.nacnez.projects.hazelcast.query.sample1.filter.BangalorePersonCountFilter;
import com.nacnez.projects.hazelcast.query.sample1.filter.BangalorePersonFilter;
import com.nacnez.projects.hazelcast.query.sample1.filter.IndiaClosePersonFilter;
import com.nacnez.projects.hazelcast.query.sample1.filter.LadyAverageSalaryForFiveDigitSalaryGettersFilter;
import com.nacnez.util.microbenchmarktool.TimedTask;

public abstract class TaskMaker {

	public static TimedTask makeTimeWastingTask(HazelcastInstance instance) {
		return new TimeWastingTask(instance, "time wasting");
	}

	public static TimedTask makeBangalorePeopleCountFilterTask(
			HazelcastInstance instance) {
		Reducer<Integer> reducer = new Reducer<Integer>() {
			public Integer reduce(Collection<Integer> results) {
				int execResult = 0;
				for (int result : results) {
					execResult = execResult + result;
				}
				return execResult;
			}

		};

		CloneMaker<Integer> cloneMaker = new CloneMaker<Integer>() {
			public TimedTask make(HazelcastInstance instance, String name,
					Callable<Integer> filter, Reducer<Integer> reducer,
					CloneMaker<Integer> cloneMaker) {
				return new DistributedExecTask<Integer>(instance,
						"Bangalore People", new BangalorePersonCountFilter(),
						reducer, cloneMaker);
			}

		};

		TimedTask disttTask = new DistributedExecTask<Integer>(instance,
				"Bangalore People", new BangalorePersonCountFilter(), reducer,
				cloneMaker);
		return disttTask;
	}
	public static TimedTask makeBangalorePeopleFilterTask(
			HazelcastInstance instance) {
		Reducer<Collection<Person>> reducer = new Reducer<Collection<Person>>() {
			public Integer reduce(Collection<Collection<Person>> results) {
				int execResult = 0;
				for (Collection<Person> result : results) {
					execResult = execResult + result.size();
				}
				return execResult;
			}

		};

		CloneMaker<Collection<Person>> cloneMaker = new CloneMaker<Collection<Person>>() {
			public TimedTask make(HazelcastInstance instance, String name,
					Callable<Collection<Person>> filter,
					Reducer<Collection<Person>> reducer,
					CloneMaker<Collection<Person>> cloneMaker) {
				return new DistributedExecTask<Collection<Person>>(instance,
						"Bangalore People", new BangalorePersonFilter(),
						reducer, cloneMaker);
			}

		};

		TimedTask distTask = new DistributedExecTask<Collection<Person>>(
				instance, "Bangalore People", new BangalorePersonFilter(),
				reducer, cloneMaker);
		return distTask;
	}

	public static TimedTask makeAverageLadySalaryFilterTask(
			HazelcastInstance instance) {

		Reducer<Double> reducer = new Reducer<Double>() {
			public Integer reduce(Collection<Double> results) {
				Double execResult = new Double(0.0);
				for (Double result : results) {
					execResult = execResult + result;
				}

				Double output = execResult / results.size();
				return output.intValue();
			}

		};

		CloneMaker<Double> cloneMaker = new CloneMaker<Double>() {
			public TimedTask make(HazelcastInstance instance, String name,
					Callable<Double> filter, Reducer<Double> reducer,
					CloneMaker<Double> cloneMaker) {
				return new DistributedExecTask<Double>(instance,
						"Average Lady Salary",
						new LadyAverageSalaryForFiveDigitSalaryGettersFilter(),
						reducer, cloneMaker);
			}

		};

		TimedTask disttTask = new DistributedExecTask<Double>(instance,
				"Average Lady Salary",
				new LadyAverageSalaryForFiveDigitSalaryGettersFilter(),
				reducer, cloneMaker);
		return disttTask;
	}

	public static TimedTask makeIndiaClosePeopleFilterTask(
			HazelcastInstance instance) {
		Reducer<Collection<Person>> reducer = new Reducer<Collection<Person>>() {
			public Integer reduce(Collection<Collection<Person>> results) {
				int execResult = 0;
				for (Collection<Person> result : results) {
					execResult = execResult + result.size();
				}
				return execResult;
			}

		};

		CloneMaker<Collection<Person>> cloneMaker = new CloneMaker<Collection<Person>>() {
			public TimedTask make(HazelcastInstance instance, String name,
					Callable<Collection<Person>> filter,
					Reducer<Collection<Person>> reducer,
					CloneMaker<Collection<Person>> cloneMaker) {
				return new DistributedExecTask<Collection<Person>>(instance,
						"GMT Close People", new IndiaClosePersonFilter(),
						reducer, cloneMaker);
			}

		};

		TimedTask distTask = new DistributedExecTask<Collection<Person>>(
				instance, "GMT Close People", new IndiaClosePersonFilter(),
				reducer, cloneMaker);
		return distTask;
	}

	// Below are the special case tasks which were used to test cancellation
	
	public static TimedTask makeCancellableBangalorePeopleFilterTask(
			HazelcastInstance instance) {
		Reducer<Collection<Person>> reducer = new Reducer<Collection<Person>>() {
			public Integer reduce(Collection<Collection<Person>> results) {
				int execResult = 0;
				for (Collection<Person> result : results) {
					execResult = execResult + result.size();
				}
				return execResult;
			}

		};

		CancellableTaskCloneMaker<Collection<Person>> cloneMaker = new CancellableTaskCloneMaker<Collection<Person>>() {
			public TimedTask make(HazelcastInstance instance, String name,
					Callable<Collection<Person>> filter,
					Reducer<Collection<Person>> reducer,
					CancellableTaskCloneMaker<Collection<Person>> cloneMaker,CancellationConstraint<Collection<Person>> cancelConstraint) {
				return new CancellableDistributedExecTask<Collection<Person>>(instance,
						"Bangalore People", new BangalorePersonFilter(),
						reducer, cloneMaker,cancelConstraint);
			}

		};
		CancellationConstraint<Collection<Person>> cancelConstraint = new CancellationConstraint<Collection<Person>>() {
			public boolean met(Collection<Collection<Person>> results) {
				int totalResults = 0;
				for (Collection<Person> result : results) {
					totalResults += result.size();
				}
				if (totalResults >= 20) {
					return true;
				}
				return false;
			}
		};

		TimedTask distTask = new CancellableDistributedExecTask<Collection<Person>>(
				instance, "Bangalore People", new BangalorePersonFilter(),
				reducer, cloneMaker,cancelConstraint);
		return distTask;
	}


	public static TimedTask makeOnePageBangalorePeopleFilterTask(HazelcastInstance instance) {
		TimedTask distTask = new ResultPollingDistributedExecTask<Collection<Person>>(instance, "One Page of Bangalore People");
		return distTask;
	}

}