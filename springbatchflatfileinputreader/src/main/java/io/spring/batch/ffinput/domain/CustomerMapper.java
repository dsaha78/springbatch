package io.spring.batch.ffinput.domain;

import org.springframework.batch.item.file.LineMapper;

public class CustomerMapper implements LineMapper<Customer> {

	@Override
	public Customer mapLine(String line, int lineNumber) throws Exception {
		
		return new Customer(Long.parseLong(line.split(",")[0]), line.split(",")[2],line.split(",")[2], line.split(",")[3]);
	}

}
