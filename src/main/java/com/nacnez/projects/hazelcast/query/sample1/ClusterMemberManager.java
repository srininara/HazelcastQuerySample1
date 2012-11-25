package com.nacnez.projects.hazelcast.query.sample1;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public abstract class ClusterMemberManager {

	public static HazelcastInstance createMember(SimpleNode simpleNode) {
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(null);
		instance.getCluster().addMembershipListener(simpleNode);
		return instance;
	}

	public static HazelcastInstance createMember() {
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(null);
		return instance;
	}

	public static void shutdownMember(HazelcastInstance shutdownRequester) {
		shutdownRequester.getLifecycleService().shutdown();
	}

}
