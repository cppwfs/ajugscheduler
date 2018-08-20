package gemschedule.scheduler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gemschedule.scheduler.configuration.AppProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.deployer.spi.core.AppDefinition;
import org.springframework.cloud.scheduler.spi.core.ScheduleInfo;
import org.springframework.cloud.scheduler.spi.core.ScheduleRequest;
import org.springframework.cloud.scheduler.spi.core.Scheduler;
import org.springframework.cloud.scheduler.spi.core.SchedulerPropertyKeys;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class SchedulerApplication {

	private static final String SCHEDULE = "schedule";

	private static final String UNSCHEDULE = "unschedule";

	@Autowired
	Scheduler scheduler;

	@Autowired
	Resource artifactResource;

	@Autowired
	AppProperties properties;

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {

				if (properties.getCommand().equals(SCHEDULE)) {
					schedule(properties.getScheduleName(),
							properties.getAppName(),
							properties.getExpression());
				}
				if (properties.getCommand().equals(UNSCHEDULE)) {
					unSchedule(properties.getScheduleName());
				}
				printSchedules();
			}
		};
	}

	private void schedule(String scheduleName, String appName, String expression) {
		ScheduleRequest request = new ScheduleRequest(
				getAppDefinition(appName),
				getScheduleProperties(expression),
				deploymentProperties(),
				scheduleName,
				artifactResource);
		scheduler.schedule(request);
	}

	private void unSchedule(String scheduleName) {
		scheduler.unschedule(scheduleName);
	}

	private void printSchedules() {
		List<ScheduleInfo> result = scheduler.list();
		System.out.println("*************");
		result.stream().forEach(scheduleInfo -> {
			System.out.println(String.format("Schedule Name -> %s with schedule %s",
					scheduleInfo.getScheduleName(),
					scheduleInfo.getScheduleProperties().get(SchedulerPropertyKeys.CRON_EXPRESSION)));
		});
		System.out.println("*************");
	}

	/**
	 * Create a deployment properties map to direct how the application should be deployed in the cloud.
	 *
	 * @return
	 */
	private Map<String, String> deploymentProperties() {
		return new HashMap<>();
	}

	/**
	 * Create a scheduler properties map for a cron expression.
	 *
	 * @param expression
	 * @return
	 */
	private Map<String, String> getScheduleProperties(String expression) {
		return Collections.singletonMap(SchedulerPropertyKeys.CRON_EXPRESSION, expression);
	}

	/**
	 * Create definition with definition name and the command line args for the application.
	 *
	 * @param definitionName
	 * @return
	 */
	private AppDefinition getAppDefinition(String definitionName) {
		return new AppDefinition(definitionName, new HashMap<String, String>());
	}

}
