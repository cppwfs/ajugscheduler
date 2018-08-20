package gemschedule.scheduler;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.scheduler.spi.core.ScheduleInfo;
import org.springframework.cloud.scheduler.spi.core.ScheduleRequest;
import org.springframework.cloud.scheduler.spi.core.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Configuration
	public static class TestConfiguration {

		@Bean
		public Scheduler scheduler() {
			return new Scheduler() {
				@Override
				public void schedule(ScheduleRequest scheduleRequest) {

				}

				@Override
				public void unschedule(String s) {

				}

				@Override
				public List<ScheduleInfo> list(String s) {
					return null;
				}

				@Override
				public List<ScheduleInfo> list() {
					return null;
				}
			};
		}
	}

}
