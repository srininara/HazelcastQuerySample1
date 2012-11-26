package com.nacnez.projects.hazelcast.query.sample1.filter;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import com.nacnez.projects.grid.model.Person;

public class BangalorePersonCountFilter implements Callable<Integer>,Serializable,HazelcastInstanceAware {

	private static final long serialVersionUID = 1L;
	HazelcastInstance instance;

	public Integer call() throws Exception {
		IMap<String,Person> cache = instance.getMap("persons");
		Set<String> localKeys = cache.localKeySet(new SqlPredicate("city = Bangalore"));
		return localKeys.size();
	}

	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.instance = hazelcastInstance;
	}

}
