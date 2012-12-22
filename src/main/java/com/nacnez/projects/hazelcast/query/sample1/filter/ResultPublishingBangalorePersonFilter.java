package com.nacnez.projects.hazelcast.query.sample1.filter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.nacnez.projects.grid.model.Person;

public class ResultPublishingBangalorePersonFilter implements Callable<Collection<Person>>,Serializable,HazelcastInstanceAware {

	private static final long serialVersionUID = 1L;
	HazelcastInstance instance;
	

	public Collection<Person> call() throws Exception {
		IMap<String,Person> cache = instance.getMap("persons");
 		Set<String> localKeys = cache.localKeySet();
 		Collection<Person> bangPeople = new HashSet<Person>();
 		IQueue<Person> results = this.instance.getQueue("results");
 		for (String key: localKeys) {
 			Person p = cache.get(key);
 			if (p.getCity().equals("Bangalore")) {
 				results.put(p);
 				bangPeople.add(p);
 				System.out.println("Filter data: " + p.getFirstName());
 			}
 		}
 		
		return bangPeople;
	}

	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.instance = hazelcastInstance;
	}

}
