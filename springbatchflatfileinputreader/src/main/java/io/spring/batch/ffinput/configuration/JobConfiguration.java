package io.spring.batch.ffinput.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import io.spring.batch.ffinput.domain.Customer;
import io.spring.batch.ffinput.domain.CustomerMapper;

@Configuration
public class JobConfiguration {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	DataSource dataSource;

	@Bean
	public FlatFileItemReader<Customer> flatFileItemReader(){
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setLineMapper(new CustomerMapper());
		reader.setResource(new ClassPathResource("/data/customer.csv"));
		
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
	public JdbcBatchItemWriter<Customer> jdbcItemWriter(){
		JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT INTO CUSTOMER01 VALUES(:id, :firstName, :lastName, :birthdate)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		
		
		return writer;
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("Step1").<Customer, Customer>chunk(10).reader(flatFileItemReader()).writer(jdbcItemWriter()).build();
	}
	
	@Bean
	public Job job() {
		String name = new Long(System.currentTimeMillis()).toString();
		return jobBuilderFactory.get(name).start(step1()).build();
	}
}
