package org.springframework.scheduling.quartz;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.quartz.SimpleTrigger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stephane Nicoll
 */
public class SimpleTriggerFactoryBeanTests {

	@Test
	public void createWithoutJobDetail() throws ParseException {
		SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
		factory.setName("myTrigger");
		factory.setRepeatCount(5);
		factory.setRepeatInterval(1000L);
		factory.afterPropertiesSet();
		SimpleTrigger trigger = factory.getObject();
		assertThat(trigger.getRepeatCount()).isEqualTo(5);
		assertThat(trigger.getRepeatInterval()).isEqualTo(1000L);
	}

}
