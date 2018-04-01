package io.spring.batch.ffinput.domain;

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
	private String birthdate;
	
}
