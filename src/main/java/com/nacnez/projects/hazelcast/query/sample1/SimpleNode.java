package com.nacnez.projects.hazelcast.query.sample1;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;

public class SimpleNode implements MembershipListener {

	private HazelcastInstance instance;

	public static void main(String[] args) {
		new SimpleNode().startNode();
	}

	private void startNode() {
		instance = ClusterMemberManager.createMember(this);
	}

	public void memberAdded(MembershipEvent membershipEvent) {
		// Do nothing
	}

	public void memberRemoved(MembershipEvent membershipEvent) {
		ClusterMemberManager.shutdownMember(instance);
	}

}
