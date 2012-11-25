package com.nacnez.projects.hazelcast.query.sample1;

import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.nacnez.util.microbenchmarktool.TimedTask;

public interface CloneMaker<T> {
	TimedTask make(HazelcastInstance instance, String name, Callable<T> filter, Reducer<T> reducer,CloneMaker<T> cloneMaker);
}
