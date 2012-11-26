package com.nacnez.projects.hazelcast.query.sample1.filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import com.nacnez.projects.grid.model.Person;

public class LadyAverageSalaryForFiveDigitSalaryGettersFilter implements
		Callable<BigDecimal>, Serializable, HazelcastInstanceAware {

	private static final long serialVersionUID = 1L;
	private HazelcastInstance instance;

	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.instance = hazelcastInstance;
	}

	public BigDecimal call() throws Exception {
		IMap<String,Person> cache = instance.getMap("persons");
		Set<String> localKeys = cache.localKeySet(new SqlPredicate("gender = Female AND income >= 10000.00"));
		BigDecimal localSalarySum = new BigDecimal(0);
		for (String localKey : localKeys) {
			Person p = cache.get(localKey);
			localSalarySum = localSalarySum.add(p.getIncome());
		}
		return localSalarySum.divide(new BigDecimal(localKeys.size()));
	}

}
