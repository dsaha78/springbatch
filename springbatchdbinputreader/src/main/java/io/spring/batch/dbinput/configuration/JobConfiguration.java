package io.spring.batch.dbinput.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.spring.batch.dbinput.domain.Customer;
import io.spring.batch.dbinput.domain.CustomerMapper;

@Configuration
public class JobConfiguration {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	DataSource dataSource;

	@Bean
	public JdbcCursorItemReader<Customer> cursorItemReader() {
		JdbcCursorItemReader<Customer> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql("select id, firstname, lastname,birthdate from customer order by firstname, lastname");
		reader.setRowMapper(new CustomerMapper());
		return reader;
	}
	@Bean
	public JdbcPagingItemReader<Customer> pagingItemReader(){
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(10);
		reader.setRowMapper(new CustomerMapper());
		
		MySqlPagingQueryProvider query = new MySqlPagingQueryProvider();
		query.setSelectClause("id, firstName, lastName, birthdate");
		query.setFromClause("from customer");
		Map<String, Order> sortKey = new HashMap<>();
		sortKey.put("id", Order.ASCENDING);
		query.setSortKeys(sortKey);
		
		reader.setQueryProvider(query);
		return reader;
	}

	@Bean
	public ItemWriter<Customer> customerItemWriter() {
		return items -> {
			for (Customer customer : items) {
				System.out.println(customer.toString());
			}
		};
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("Step1").<Customer, Customer>chunk(10).reader(pagingItemReader()).writer(customerItemWriter()).build();
	}
	
	@Bean
	public Job job() {
		String name = new Long(System.currentTimeMillis()).toString();
		return jobBuilderFactory.get(name).start(step1()).build();
	}
}
