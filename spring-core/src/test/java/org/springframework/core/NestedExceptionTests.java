package org.springframework.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
@SuppressWarnings("serial")
class NestedExceptionTests {

	@Test
	void nestedRuntimeExceptionWithNoRootCause() {
		String mesg = "mesg of mine";
		// Making a class abstract doesn't _really_ prevent instantiation :-)
		NestedRuntimeException nex = new NestedRuntimeException(mesg) {};
		assertThat(nex.getCause()).isNull();
		assertThat(mesg).isEqualTo(nex.getMessage());

		// Check printStackTrace
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		nex.printStackTrace(pw);
		pw.flush();
		String stackTrace = new String(baos.toByteArray());
		assertThat(stackTrace.contains(mesg)).isTrue();
	}

	@Test
	void nestedRuntimeExceptionWithRootCause() {
		String myMessage = "mesg for this exception";
		String rootCauseMsg = "this is the obscure message of the root cause";
		Exception rootCause = new Exception(rootCauseMsg);
		// Making a class abstract doesn't _really_ prevent instantiation :-)
		NestedRuntimeException nex = new NestedRuntimeException(myMessage, rootCause) {};
		assertThat(rootCause).isEqualTo(nex.getCause());
		assertThat(nex.getMessage().contains(myMessage)).isTrue();
		assertThat(nex.getMessage().endsWith(rootCauseMsg)).isTrue();

		// check PrintStackTrace
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		nex.printStackTrace(pw);
		pw.flush();
		String stackTrace = new String(baos.toByteArray());
		assertThat(stackTrace.contains(rootCause.getClass().getName())).isTrue();
		assertThat(stackTrace.contains(rootCauseMsg)).isTrue();
	}

	@Test
	void nestedCheckedExceptionWithNoRootCause() {
		String mesg = "mesg of mine";
		// Making a class abstract doesn't _really_ prevent instantiation :-)
		NestedCheckedException nex = new NestedCheckedException(mesg) {};
		assertThat(nex.getCause()).isNull();
		assertThat(mesg).isEqualTo(nex.getMessage());

		// Check printStackTrace
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		nex.printStackTrace(pw);
		pw.flush();
		String stackTrace = new String(baos.toByteArray());
		assertThat(stackTrace.contains(mesg)).isTrue();
	}

	@Test
	void nestedCheckedExceptionWithRootCause() {
		String myMessage = "mesg for this exception";
		String rootCauseMsg = "this is the obscure message of the root cause";
		Exception rootCause = new Exception(rootCauseMsg);
		// Making a class abstract doesn't _really_ prevent instantiation :-)
		NestedCheckedException nex = new NestedCheckedException(myMessage, rootCause) {};
		assertThat(rootCause).isEqualTo(nex.getCause());
		assertThat(nex.getMessage().contains(myMessage)).isTrue();
		assertThat(nex.getMessage().endsWith(rootCauseMsg)).isTrue();

		// check PrintStackTrace
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		nex.printStackTrace(pw);
		pw.flush();
		String stackTrace = new String(baos.toByteArray());
		assertThat(stackTrace.contains(rootCause.getClass().getName())).isTrue();
		assertThat(stackTrace.contains(rootCauseMsg)).isTrue();
	}

}
