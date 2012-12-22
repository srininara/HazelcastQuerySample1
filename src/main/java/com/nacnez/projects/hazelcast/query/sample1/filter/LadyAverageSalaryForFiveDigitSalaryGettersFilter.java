package com.nacnez.projects.hazelcast.query.sample1.filter;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import com.nacnez.projects.grid.model.Person;

public class LadyAverageSalaryForFiveDigitSalaryGettersFilter implements
		Callable<Double>, Serializable, HazelcastInstanceAware {

	private static final long serialVersionUID = 1L;
	private HazelcastInstance instance;

	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.instance = hazelcastInstance;
	}

	public Double call() throws Exception {
		IMap<String,Person> cache = instance.getMap("persons");
		Set<String> localKeys = cache.localKeySet(new SqlPredicate("gender = Female AND income >= 10000.00"));
		Double localSalarySum = new Double(0.0);
		for (String localKey : localKeys) {
			Person p = cache.get(localKey);
			localSalarySum = localSalarySum + p.getIncome();
		}
		return localSalarySum/localKeys.size();
	}

}
