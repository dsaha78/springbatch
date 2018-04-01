package io.spring.batch.dbinput.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CustomerMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet result, int arg1) throws SQLException {

		return new Customer(result.getLong("id"), result.getString("firstName"), result.getString("lastName"),
				result.getDate("birthdate"));
	}

}
