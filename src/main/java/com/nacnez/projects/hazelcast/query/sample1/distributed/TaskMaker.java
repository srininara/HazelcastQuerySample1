package com.nacnez.projects.hazelcast.query.sample1.distributed;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.nacnez.projects.grid.model.Person;
import com.nacnez.projects.hazelcast.query.sample1.filter.BangalorePersonCountFilter;
import com.nacnez.projects.hazelcast.query.sample1.filter.BangalorePersonFilter;
import com.nacnez.projects.hazelcast.query.sample1.filter.GMTClosePersonFilter;
import com.nacnez.projects.hazelcast.query.sample1.filter.LadyAverageSalaryForFiveDigitSalaryGettersFilter;
import com.nacnez.util.microbenchmarktool.TimedTask;

public abstract class TaskMaker {
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
					Callable<Collection<Person>> filter, Reducer<Collection<Person>> reducer,
					CloneMaker<Collection<Person>> cloneMaker) {
				return new DistributedExecTask<Collection<Person>>(instance,
						"Bangalore People", new BangalorePersonFilter(),
						reducer, cloneMaker);
			}

		};

		TimedTask distTask = new DistributedExecTask<Collection<Person>>(instance,
				"Bangalore People", new BangalorePersonFilter(), reducer,
				cloneMaker);
		return distTask;
	}

	
	public static TimedTask makeAverageLadySalaryFilterTask(
			HazelcastInstance instance) {
		
		Reducer<BigDecimal> reducer = new Reducer<BigDecimal>() {
			public Integer reduce(Collection<BigDecimal> results) {
				BigDecimal execResult = new BigDecimal(0);
				for (BigDecimal result : results) {
					execResult = execResult.add(result);
				}
				return execResult.divide(new BigDecimal(results.size())).intValue();
			}

		};

		CloneMaker<BigDecimal> cloneMaker = new CloneMaker<BigDecimal>() {
			public TimedTask make(HazelcastInstance instance, String name,
					Callable<BigDecimal> filter, Reducer<BigDecimal> reducer,
					CloneMaker<BigDecimal> cloneMaker) {
				return new DistributedExecTask<BigDecimal>(instance,
						"Average Lady Salary", new LadyAverageSalaryForFiveDigitSalaryGettersFilter(),
						reducer, cloneMaker);
			}

		};

		TimedTask disttTask = new DistributedExecTask<BigDecimal>(instance,
				"Average Lady Salary", new LadyAverageSalaryForFiveDigitSalaryGettersFilter(), reducer,
				cloneMaker);
		return disttTask;
	}

	public static TimedTask makeGMTClosePeopleFilterTask(
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
					Callable<Collection<Person>> filter, Reducer<Collection<Person>> reducer,
					CloneMaker<Collection<Person>> cloneMaker) {
				return new DistributedExecTask<Collection<Person>>(instance,
						"GMT Close People", new GMTClosePersonFilter(),
						reducer, cloneMaker);
			}

		};

		TimedTask distTask = new DistributedExecTask<Collection<Person>>(instance,
				"GMT Close People", new GMTClosePersonFilter(), reducer,
				cloneMaker);
		return distTask;
	}
	
}