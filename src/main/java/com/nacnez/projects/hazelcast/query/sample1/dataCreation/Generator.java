package com.nacnez.projects.hazelcast.query.sample1.dataCreation;

import static com.nacnez.projects.grid.modelCreator.DataCreator.createData;

import java.util.Collection;

import com.nacnez.projects.grid.model.Person;
import com.nacnez.util.microbenchmarktool.TimedTask;
import com.nacnez.util.microbenchmarktool.core.PrototypeTimedTask;

public class Generator extends PrototypeTimedTask {

	private static final long serialVersionUID = 1L;
	
	public Collection<Person> data;

	private int genNum;
	
	public Generator(int genNum, String name) {
		super(name);
		this.genNum = genNum;
	}

	@Override
	public void doTask() {
		data = createData(genNum);
	}

	@Override
	public String completedExecutionMessage() {
		return "Finished Generation of " + genNum +" objects";
	}

	@Override
	public boolean idemPotent() {
		return false;
	}

	@Override
	public TimedTask clone() {
		return this;
	}
	
	public Collection<Person> get() {
		return data;
	}

}
