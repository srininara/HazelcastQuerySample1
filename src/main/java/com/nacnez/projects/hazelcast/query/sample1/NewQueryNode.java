package com.nacnez.projects.hazelcast.query.sample1;

import static com.nacnez.projects.hazelcast.query.sample1.distributed.TaskMaker.makeAverageLadySalaryFilterTask;
import static com.nacnez.projects.hazelcast.query.sample1.distributed.TaskMaker.makeBangalorePeopleCountFilterTask;
import static com.nacnez.projects.hazelcast.query.sample1.distributed.TaskMaker.makeBangalorePeopleFilterTask;
import static com.nacnez.projects.hazelcast.query.sample1.distributed.TaskMaker.makeIndiaClosePeopleFilterTask;
import static com.nacnez.util.microbenchmarktool.MicroBenchmarkTool.newFileOutputReporter;
import static com.nacnez.util.microbenchmarktool.MicroBenchmarkTool.newSimpleExecutor;
import static com.nacnez.util.microbenchmarktool.MicroBenchmarkTool.newStatRichSimpleFileOutputReporter;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.nacnez.projects.grid.model.Person;
import com.nacnez.projects.hazelcast.query.sample1.dataCreation.Generator;
import com.nacnez.projects.hazelcast.query.sample1.dataCreation.Populator;
import com.nacnez.util.microbenchmarktool.TimedTask;

public class NewQueryNode {

	public static final String REPORT_BASE_PATH = "/home/narayasr/MyRoot/WorkArea/PerfBenchmarks/hazelcast/";

	private static final String TXT_EXTN = ".txt";

	public HazelcastInstance instance;
	public Collection<Person> data;
	public IMap<String, Person> cache;

	public static void main(String[] args) throws Exception {
		NewQueryNode qnode = new NewQueryNode();
		qnode.startNode();
		qnode.doStuff();
		Thread.sleep(5000);
		qnode.close();
	}

	private void close() {
		ClusterMemberManager.shutdownMember(instance);
	}

	private void doStuff() throws Exception {
		for (int i = 0; i < 8; i++) {
			doTestAndMeasure(fullFileName("Test-5Nodes-" + ((i+1)*5000)+" - "));
		}

	}

	private void doTestAndMeasure(String fileName) {
		TimedTask generator = new Generator(5000, "Initial Generation");
		newSimpleExecutor().with(newFileOutputReporter(fileName))
				.execute(generator, 1).report();

		TimedTask populator = new Populator(getCache(),
				((Generator) generator).get(), "Initial Generation Populate");
		newSimpleExecutor().with(newFileOutputReporter(fileName))
				.execute(populator, 1).report();

		newSimpleExecutor().with(newStatRichSimpleFileOutputReporter(fileName))
				.execute(makeBangalorePeopleCountFilterTask(instance), 50)
				.report();
		newSimpleExecutor().with(newStatRichSimpleFileOutputReporter(fileName))
				.execute(makeBangalorePeopleFilterTask(instance), 50).report();
		newSimpleExecutor().with(newStatRichSimpleFileOutputReporter(fileName))
				.execute(makeAverageLadySalaryFilterTask(instance), 50)
				.report();
		newSimpleExecutor().with(newStatRichSimpleFileOutputReporter(fileName))
				.execute(makeIndiaClosePeopleFilterTask(instance), 50).report();
	}


	private String fullFileName(String basicName) {
		return REPORT_BASE_PATH + basicName + nowInString() + TXT_EXTN;
	}

	public static String nowInString() {
		return new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
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
