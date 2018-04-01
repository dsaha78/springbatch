package io.spring.batch.flow.decision.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfiguration {

	@Autowired
	JobBuilderFactory jobBuilderFactory;
	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step startStep() {
		return stepBuilderFactory.get("StartStep").tasklet((contribution, chunkContext) -> {
			System.out.println("Thsi is the start step");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Step oddStep() {
		return stepBuilderFactory.get("OddStep").tasklet((contribution, chunkContext) -> {
			System.out.println("Thsi is the odd step");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Step evenStep() {
		return stepBuilderFactory.get("EvenStep").tasklet((contribution, chunkContext) -> {
			System.out.println("Thsi is the Even step");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new OddDecider();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("DecisionJob")
		.start(startStep())
		.next(decider())
		.from(decider()).on("EVEN").to(evenStep())
		.from(decider()).on("ODD").to(oddStep())
		.from(oddStep()).on("*").to(decider())
		.end()
		.build();
	}

	public static class OddDecider implements JobExecutionDecider {
		int count = 0;

		@Override
		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
			count++;
			if (count % 2 == 0) {
				return new FlowExecutionStatus("EVEN");
			} else {
				return new FlowExecutionStatus("ODD");
			}
		}
	}
}
