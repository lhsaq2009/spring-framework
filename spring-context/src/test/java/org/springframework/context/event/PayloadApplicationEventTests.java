package org.springframework.context.event;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 */
public class PayloadApplicationEventTests {

	@Test
	@SuppressWarnings({ "rawtypes", "resource" })
	public void testEventClassWithInterface() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AuditableListener.class);
		AuditablePayloadEvent event = new AuditablePayloadEvent<>(this, "xyz");
		ac.publishEvent(event);
		assertThat(ac.getBean(AuditableListener.class).events.contains(event)).isTrue();
	}


	public interface Auditable {
	}


	@SuppressWarnings("serial")
	public static class AuditablePayloadEvent<T> extends PayloadApplicationEvent<T> implements Auditable {

		public AuditablePayloadEvent(Object source, T payload) {
			super(source, payload);
		}
	}


	@Component
	public static class AuditableListener {

		public final List<Auditable> events = new ArrayList<>();

		@EventListener
		public void onEvent(Auditable event) {
			events.add(event);
		}
	}

}
