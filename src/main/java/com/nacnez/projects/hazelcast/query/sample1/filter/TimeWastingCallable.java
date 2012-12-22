package com.nacnez.projects.hazelcast.query.sample1.filter;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.nacnez.projects.grid.model.Person;

public class TimeWastingCallable implements
Callable<Integer>,Serializable,HazelcastInstanceAware  {

	private static final long serialVersionUID = 1L;
	private HazelcastInstance instance;

	private Random random = new Random();

	@Override
	public Integer call() throws Exception {
		int count = 0;
		int loopLength = random.nextInt(50);
		IMap<String,Person> cache = instance.getMap("persons");
		System.out.println("Loop length: "+ loopLength+ " ; thread id: "+Thread.currentThread().getId());
		for (int i=0;i<loopLength;i++) {
			Thread.sleep(500);
			System.out.println("Loop Counter: "+ i + " ; thread id: "+Thread.currentThread().getId()+" ; size: " + cache.size());
		}
		return count;
	}



	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		this.instance = hazelcastInstance;
	}

}
