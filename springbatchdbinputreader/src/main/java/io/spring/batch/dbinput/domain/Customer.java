package io.spring.batch.dbinput.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Customer {

	private long id;
	private String firstName;
	private String lastName;
	private Date dob;
	
}
