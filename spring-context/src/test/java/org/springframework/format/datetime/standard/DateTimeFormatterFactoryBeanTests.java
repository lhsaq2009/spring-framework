package org.springframework.format.datetime.standard;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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
		assertThat(factory.getObject().toString()).isEqualTo(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).toString());
	}

	@Test
	public void getObjectIsAlwaysSingleton() {
		factory.afterPropertiesSet();
		DateTimeFormatter formatter = factory.getObject();
		assertThat(formatter.toString()).isEqualTo(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).toString());
		factory.setStylePattern("LL");
		assertThat(factory.getObject()).isSameAs(formatter);
	}

}
