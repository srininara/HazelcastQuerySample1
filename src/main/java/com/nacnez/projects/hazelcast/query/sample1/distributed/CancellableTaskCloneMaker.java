package com.nacnez.projects.hazelcast.query.sample1.distributed;

import java.util.concurrent.Callable;

import com.hazelcast.core.HazelcastInstance;
import com.nacnez.projects.hazelcast.query.sample1.extend.CancellationConstraint;
import com.nacnez.util.microbenchmarktool.TimedTask;

public interface CancellableTaskCloneMaker<T> {
	TimedTask make(HazelcastInstance instance, String name, Callable<T> filter, Reducer<T> reducer,CancellableTaskCloneMaker<T> cloneMaker,CancellationConstraint<T> cancelConstraint);
}
