package org.springframework.cache.aspectj;

/**
 * Utility to trick the compiler to throw a valid checked
 * exceptions within the interceptor.
 *
 * @author Stephane Nicoll
 */
final class AnyThrow {

	private AnyThrow() {
	}


	static void throwUnchecked(Throwable e) {
		AnyThrow.<RuntimeException>throwAny(e);
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwAny(Throwable e) throws E {
		throw (E) e;
	}
}
