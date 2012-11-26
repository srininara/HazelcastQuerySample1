package com.nacnez.projects.hazelcast.query.sample1.distributed;

import java.util.Collection;

public interface Reducer<T> {
	Integer reduce(Collection<T> results);
}
