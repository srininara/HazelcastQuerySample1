package com.nacnez.projects.hazelcast.query.sample1;

import static com.nacnez.projects.grid.modelCreator.DataCreator.createData;
import static com.nacnez.projects.hazelcast.query.sample1.distributed.TaskMaker.*;
import static com.nacnez.util.microbenchmarktool.MicroBenchmarkTool.newSimpleExecutor;
import static com.nacnez.util.microbenchmarktool.MicroBenchmarkTool.newSimpleStandardOutputReporter;
import static com.nacnez.util.microbenchmarktool.MicroBenchmarkTool.newStandardOutputReporter;

import java.util.Collection;
import java.util.Map;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.nacnez.projects.grid.model.Person;
import com.nacnez.util.microbenchmarktool.TimedTask;
import com.nacnez.util.microbenchmarktool.core.SingletonTimedTask;

public class QueryNode {

	public HazelcastInstance instance;
	public Collection<Person> data;
	public IMap<String, Person> cache;

	public static void main(String[] args) throws Exception {
		QueryNode qnode = new QueryNode();
		qnode.startNode();
		qnode.doStuff();
		Thread.sleep(5000);
		qnode.close();
	}

	private void close() {
		ClusterMemberManager.shutdownMember(instance);
	}

	private void doStuff() throws Exception {
		TimedTask generator = new SingletonTimedTask("Data Generator",
				"Generated data") {

			private static final long serialVersionUID = 1L;

			public void doTask() {
				createPersons();
			}

			public boolean idemPotent() {
				return false;
			}
		};
		TimedTask filler = new SingletonTimedTask("Data Filler",
				"Completed grid filling") {

			private static final long serialVersionUID = 1L;

			public void doTask() {
				fillDataGrid(data);
			}

			public boolean idemPotent() {
				return false;
			}
		};
		newSimpleExecutor().with(newStandardOutputReporter())
				.execute(generator).report();
		newSimpleExecutor().with(newStandardOutputReporter()).execute(filler)
				.report();

		// Using Distributed Executor
//		TimedTask distTask = makeBangalorePeopleCountFilterTask(instance);
//		TimedTask distTask = makeBangalorePeopleFilterTask(instance);
//		TimedTask distTask = makeAverageLadySalaryFilterTask(instance);
		TimedTask distTask = makeGMTClosePeopleFilterTask(instance);
		newSimpleExecutor().with(newSimpleStandardOutputReporter())
		.execute(distTask, 10).report();

		// Using SQL Predicate
		// TimedTask query = new PersonQueryTask(getCache(),"Person Query");
		// newSimpleExecutor().with(newSimpleStandardOutputReporter()).execute(query,10)
		// .report();

	}

	void createPersons() {
		data = createData(10000);
	}

	private void fillDataGrid(Collection<Person> data) {
		Map<String, Person> personCache = getCache();
		int filterCount = 0;
		for (Person person : data) {
			if (person.getCity().equals("Bangalore")) {
				filterCount++;
			}
			personCache.put(person.getUniqueId(), person);
		}
		System.out.println("Expected Result: " + filterCount);

	}

	IMap<String, Person> getCache() {
		if (cache == null) {
			cache = instance.getMap("persons");
			// Indexing code start
			cache.addIndex("city", false);
			// Indexing code end
		}
		return cache;
	}

	private void startNode() {
		instance = ClusterMemberManager.createMember();
	}

}
