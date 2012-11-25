package com.nacnez.projects.hazelcast.query.sample1;

import java.util.Collection;

import com.nacnez.projects.hazelcast.query.sample1.model.Person;
import com.nacnez.projects.hazelcast.query.sample1.modelContract.PersonContract;
import com.nacnez.util.modelgen.GeneratorFactory;
import com.nacnez.util.modelgen.ModelGenerator;

public class DataCreator {
	public static Collection<Person> createData(int count) {
		ModelGenerator<Person> smg = GeneratorFactory.get();
		Collection<Person> c = smg.make(count)
				.instancesWith(PersonContract.class).andProvideAsCollection();
		return c;
	}

}
