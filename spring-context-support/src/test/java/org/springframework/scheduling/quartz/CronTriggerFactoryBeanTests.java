package org.springframework.scheduling.quartz;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.quartz.CronTrigger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stephane Nicoll
 */
public class CronTriggerFactoryBeanTests {

	@Test
	public void createWithoutJobDetail() throws ParseException {
		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
		factory.setName("myTrigger");
		factory.setCronExpression("0 15 10 ? * *");
		factory.afterPropertiesSet();
		CronTrigger trigger = factory.getObject();
		assertThat(trigger.getCronExpression()).isEqualTo("0 15 10 ? * *");
	}

}
