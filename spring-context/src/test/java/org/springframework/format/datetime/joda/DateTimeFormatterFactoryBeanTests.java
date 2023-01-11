package org.springframework.format.datetime.joda;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;





/**
 * @author Phillip Webb
 * @author Sam Brannen
 */
public class DateTimeFormatterFactoryBeanTests {

	private final DateTimeFormatterFactoryBean factory = new DateTimeFormatterFactoryBean();


	@Test
	public void isSingleton() {
		assertThat(factory.isSingleton()).isTrue();
	}

	@Test
	public void getObjectType() {
		assertThat(factory.getObjectType()).isEqualTo(DateTimeFormatter.class);
	}

	@Test
	public void getObject() {
		factory.afterPropertiesSet();
		assertThat(factory.getObject()).isEqualTo(DateTimeFormat.mediumDateTime());
	}

	@Test
	public void getObjectIsAlwaysSingleton() {
		factory.afterPropertiesSet();
		DateTimeFormatter formatter = factory.getObject();
		assertThat(formatter).isEqualTo(DateTimeFormat.mediumDateTime());
		factory.setStyle("LL");
		assertThat(factory.getObject()).isSameAs(formatter);
	}

}
