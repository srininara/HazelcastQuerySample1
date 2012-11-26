package com.nacnez.projects.hazelcast.query.sample1.filter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import com.nacnez.projects.grid.model.Person;

public class GMTClosePersonFilter implements Callable<Collection<Person>>,
		HazelcastInstanceAware, Serializable {

	private static final long serialVersionUID = 1L;
	private HazelcastInstance instance;

	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.instance = hazelcastInstance;
	}
	
	public Collection<Person> call() throws Exception {
		IMap<String,Person> cache = instance.getMap("persons");
 		Set<String> localKeys = cache.localKeySet(new SqlPredicate("address.currentLocation.longitude BETWEEN -10.00 AND 10.00"));
 		Collection<Person> gmtClosePeople = cache.getAll(localKeys).values();
		return new HashSet<Person>(gmtClosePeople);
	}


}
