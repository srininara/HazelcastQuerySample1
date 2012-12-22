package com.nacnez.projects.hazelcast.query.sample1.extend;

import java.util.Collection;

public interface CancellationConstraint<V> {
	boolean met(Collection<V> results);
}
