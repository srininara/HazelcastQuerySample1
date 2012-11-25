package com.nacnez.projects.hazelcast.query.sample1.modelContract;

import java.math.BigDecimal;
import java.util.Date;

import com.nacnez.projects.hazelcast.query.sample1.model.Address;
import com.nacnez.util.modelgen.Contract;
import com.nacnez.util.modelgen.GenerationContract;
import com.nacnez.util.modelgen.TargetModel;
import com.nacnez.util.modelgen.generator.rules.Alphabetic;
import com.nacnez.util.modelgen.generator.rules.Alphanumeric;
import com.nacnez.util.modelgen.generator.rules.DateLimit;
import com.nacnez.util.modelgen.generator.rules.DecimalLimit;
import com.nacnez.util.modelgen.generator.rules.FromList;
import com.nacnez.util.modelgen.generator.rules.Size;
@TargetModel(modelClass=com.nacnez.projects.hazelcast.query.sample1.model.Person.class)
public interface PersonContract extends GenerationContract {

	@Alphanumeric
	@Size(maxSize=15)
	void setUniqueId(String uniqueId);

	@Alphabetic
	@Size(maxSize=32)
	void setFirstName(String firstName);

	@Alphabetic
	@Size(maxSize=32)
	void setLastName(String lastName);

	@DateLimit(lowLimit="01-01-1975",highLimit="01-01-2010",format="dd-mm-yyyy")
	void setDateOfBirth(Date dateOfBirth);

	@FromList(fromList={"Male","Female"})
	void setGender(String gender);

	@Contract(contractClass=com.nacnez.projects.hazelcast.query.sample1.modelContract.AddressContract.class)
	void setAddress(Address address);
	
	@DecimalLimit(lowLimit="5000.00",highLimit="5000000.00",scale=2)
	public void setIncome(BigDecimal income);
	
	@FromList(fromList={"Bangalore","Calcutta","Chennai","Mumbai","New Delhi"})
	void setCity(String city);


}