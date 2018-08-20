/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gemschedule.scheduler.configuration;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.deployer.resource.docker.DockerResource;
import org.springframework.cloud.scheduler.spi.core.Scheduler;
import org.springframework.cloud.scheduler.spi.kubernetes.KubernetesSchedulerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.cloud.scheduler.spi.kubernetes.KubernetesAppScheduler;

@Configuration
public class KScheduleConfiguration {

	@Configuration
	@EnableAutoConfiguration
	@EnableConfigurationProperties
	@Profile("kubernetes")
	public static class Config {
		private KubernetesSchedulerProperties kubernetesSchedulerProperties = new KubernetesSchedulerProperties();

		@Bean
		public Scheduler scheduler() {
			io.fabric8.kubernetes.client.Config config = io.fabric8.kubernetes.client.Config.autoConfigure(null);
			config.setNamespace(kubernetesSchedulerProperties.getNamespace());

			return new KubernetesAppScheduler(new DefaultKubernetesClient(config), kubernetesSchedulerProperties);
		}
	}

	@Bean
	@ConditionalOnMissingBean
	@Profile("kubernetes")
	public Resource getMavenResource() {
		return new DockerResource("springcloud/spring-cloud-scheduler-spi-test-app:latest");
	}

}
