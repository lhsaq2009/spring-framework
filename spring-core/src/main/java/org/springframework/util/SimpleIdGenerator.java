package org.springframework.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple {@link IdGenerator} that starts at 1, increments up to
 * {@link Long#MAX_VALUE}, and then rolls over.
 *
 * @author Rossen Stoyanchev
 * @since 4.1.5
 */
public class SimpleIdGenerator implements IdGenerator {

	private final AtomicLong leastSigBits = new AtomicLong(0);


	@Override
	public UUID generateId() {
		return new UUID(0, this.leastSigBits.incrementAndGet());
	}

}
