package com.nacnez.projects.hazelcast.query.sample1;

import static com.nacnez.projects.grid.modelCreator.DataCreator.createData;
import static com.nacnez.projects.hazelcast.query.sample1.distributed.TaskMaker.makeBangalorePeopleFilterTask;
import static com.nacnez.projects.hazelcast.query.sample1.distributed.TaskMaker.makeOnePageBangalorePeopleFilterTask;
import static com.nacnez.util.microbenchmarktool.MicroBenchmarkTool.newSimpleExecutor;
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
				.execute(generator, 1).report();
		newSimpleExecutor().with(newStandardOutputReporter())
				.execute(filler, 1).report();

		newSimpleExecutor().with(newStandardOutputReporter())
		.execute(makeBangalorePeopleFilterTask(instance), 1).report();

		newSimpleExecutor().with(newStandardOutputReporter())
		.execute(makeOnePageBangalorePeopleFilterTask(instance), 1).report();

		//		newSimpleExecutor().with(newStandardOutputReporter())
//		.execute(makeCancellableBangalorePeopleFilterTask(instance), 1).report();
		//		newSimpleExecutor().with(newStandardOutputReporter())
//		.execute(makeTimeWastingTask(instance), 1).report();

		
		
		//		Thread.sleep(5000);

//		newSimpleExecutor().with(newStandardOutputReporter())
//		.execute(makeBangalorePeopleCountFilterTask(instance), 1).report();
		
		// Using Distributed Executor
		// newSimpleExecutor().with(newStatRichSimpleStandardOutputReporter())
		// .execute(makeBangalorePeopleCountFilterTask(instance), 50)
		// .report();
		// newSimpleExecutor().with(newStatRichSimpleStandardOutputReporter())
		// .execute(makeBangalorePeopleFilterTask(instance), 50).report();
		// newSimpleExecutor().with(newStatRichSimpleStandardOutputReporter())
		// .execute(makeAverageLadySalaryFilterTask(instance), 50)
		// .report();
		// newSimpleExecutor().with(newStatRichSimpleStandardOutputReporter())
		// .execute(makeGMTClosePeopleFilterTask(instance), 50).report();

//		newSimpleExecutor().with(newStatRichStandardOutputReporter())
//				.execute(makeBangalorePeopleCountFilterTask(instance), 50)
//				.report();
//		newSimpleExecutor().with(newStatRichStandardOutputReporter())
//				.execute(makeBangalorePeopleFilterTask(instance), 50).report();
//		newSimpleExecutor().with(newStatRichStandardOutputReporter())
//				.execute(makeAverageLadySalaryFilterTask(instance), 50)
//				.report();
//		newSimpleExecutor().with(newStatRichStandardOutputReporter())
//				.execute(makeGMTClosePeopleFilterTask(instance), 2).report();

		// Using SQL Predicate
		// TimedTask query = new PersonQueryTask(getCache(),"Person Query");
		// newSimpleExecutor().with(newSimpleStandardOutputReporter()).execute(query,10)
		// .report();

	}

	void createPersons() {
		data = createData(1000);
	}

	private void fillDataGrid(Collection<Person> data) {
		Map<String, Person> personCache = getCache();
		int personfilterCount = 0;
		int generatedDataBasedExpectedCountLadyAvg = 0;
		Double generatedDataBasedExpectedSalLadyAvg = new Double(0.0);
		int generatedDataBasedExpectedCountGMT = 0;
		Double lowVal = new Double(-0);
		Double hiVal = new Double(60);

		for (Person person : data) {
			if (person.getCity().equals("Bangalore")) {
				personfilterCount++;
			}
			if ("Female".equals(person.getGender()) && person.getIncome().compareTo(new Double(10000))>=0) {
				generatedDataBasedExpectedSalLadyAvg = generatedDataBasedExpectedSalLadyAvg + person.getIncome();
				generatedDataBasedExpectedCountLadyAvg++;
			}
			Double longitude = person.getAddress().getCurrentLocation().getLongitude();
			if (longitude.compareTo(lowVal)>=0 && longitude.compareTo(hiVal)<=0) {
				generatedDataBasedExpectedCountGMT++;
			}
			
			personCache.put(person.getUniqueId(), person);
		}
//		System.out.println("Expected Result Bangalore Person: " + personfilterCount);
//		System.out.println("Expected Result Lady Avg Sal: " + generatedDataBasedExpectedSalLadyAvg/generatedDataBasedExpectedCountLadyAvg);
//		System.out.println("Expected Result GMT Person: " + generatedDataBasedExpectedCountGMT);

	}

	IMap<String, Person> getCache() {
		if (cache == null) {
			cache = instance.getMap("persons");
			// Indexing code start
			cache.addIndex("city", false);
			cache.addIndex("income", false);
			cache.addIndex("address.currentLocation.longitude", false);
			// Indexing code end
		}
		return cache;
	}

	private void startNode() {
		instance = ClusterMemberManager.createMember();
	}

}
