package com.nacnez.projects.hazelcast.query.sample1.dataCreation;

import java.util.Collection;
import java.util.Map;

import com.nacnez.projects.grid.model.Person;
import com.nacnez.util.microbenchmarktool.TimedTask;
import com.nacnez.util.microbenchmarktool.core.PrototypeTimedTask;

public class Populator extends PrototypeTimedTask {

	private static final long serialVersionUID = 1L;

	Map<String, Person> personCache;
	Collection<Person> data;
	
	public Populator(Map<String, Person> personCache, Collection<Person> data,String name) {
		super(name);
		this.personCache = personCache;
		this.data = data;
	}
	
	@Override
	public void doTask() {
		for (Person person : data) {
			personCache.put(person.getUniqueId(), person);
		}
		
	}

	@Override
	public String completedExecutionMessage() {
		return "Completed populating the grid for task: " + name();
	}

	@Override
	public boolean idemPotent() {
		return false;
	}

	@Override
	public TimedTask clone() {
		return this;
	}

}
