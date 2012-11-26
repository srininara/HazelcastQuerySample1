package com.nacnez.projects.hazelcast.query.sample1.alternate;

import java.util.Set;

import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import com.nacnez.projects.grid.model.Person;
import com.nacnez.util.microbenchmarktool.TimedTask;
import com.nacnez.util.microbenchmarktool.core.PrototypeTimedTask;

public class PersonQueryTask extends PrototypeTimedTask {

	private static final long serialVersionUID = 1L;
	
	private IMap<String, Person> personCache;
	
	int queryCount;
	
	public PersonQueryTask(IMap<String, Person> personCache,String name) {
		super(name);
		this.personCache = personCache;
	}

	private int queryGridForCountOfBangaloreFolks() {
		Set<String> personsIds = (Set<String>) personCache.keySet(new SqlPredicate("city = Bangalore"));
		return personsIds.size();
	}

	
	public void doTask() {
		this.queryCount = queryGridForCountOfBangaloreFolks();
	}

	public String completedExecutionMessage() {
		return " Returned " + this.queryCount + " results. ";
	}

	public TimedTask clone() {
		return new PersonQueryTask(personCache,name());
	}

	public boolean idemPotent() {
		return true;
	}

}
