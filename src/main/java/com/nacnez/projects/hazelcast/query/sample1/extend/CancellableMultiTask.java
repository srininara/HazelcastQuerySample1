package com.nacnez.projects.hazelcast.query.sample1.extend;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.hazelcast.core.Member;
import com.hazelcast.core.MultiTask;

public class CancellableMultiTask<V> extends MultiTask<V> {
	
	private CancellationConstraint<V> cancelConstraint;

	public CancellableMultiTask(Callable<V> callable, Set<Member> members, CancellationConstraint<V> cancelConstraint) {
		super(callable, members);
		this.cancelConstraint = cancelConstraint;
	}

	@Override
	public void onResult(Object result) {
		super.onResult(result);
		if (this.shouldBeCancelled()) {
			System.out.println("Cancelled!");
			this.cancel(true);
		}
	}

	private boolean shouldBeCancelled() {
		return cancelConstraint.met(this.results);
	}

	@Override
	public Collection<V> get() throws ExecutionException, InterruptedException {
		try {
			super.get();
		} catch (CancellationException c) {
			// Eat it up
		}
		return results;
	}

	@Override
	public Collection<V> get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		try {
			super.get(timeout, unit);
		} catch (CancellationException c) {
			// Eat it up
		}
		return results;
	}

}
