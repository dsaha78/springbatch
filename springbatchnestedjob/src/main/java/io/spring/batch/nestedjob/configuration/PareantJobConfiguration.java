package io.spring.batch.nestedjob.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class PareantJobConfiguration {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobLauncher jobLancher;

	@Autowired
	private Job childJob;

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("Step1").tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
			System.out.println("Step 1 Executed !!!");
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Bean
	public Job parentJob(JobRepository repository, PlatformTransactionManager tramsactionManager) {

		Step childJobStep = new JobStepBuilder(new StepBuilder("ChildStepBuilder")).job(childJob).launcher(jobLancher)
				.transactionManager(tramsactionManager).repository(repository).build();
		return jobBuilderFactory.get("ParentJob").start(step1()).next(childJobStep).build();
	}
}
