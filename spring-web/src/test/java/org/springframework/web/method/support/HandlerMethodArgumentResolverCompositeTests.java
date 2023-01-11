package org.springframework.web.method.support;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.core.MethodParameter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Test fixture with {@link HandlerMethodArgumentResolverComposite}.
 *
 * @author Rossen Stoyanchev
 */
public class HandlerMethodArgumentResolverCompositeTests {

	private HandlerMethodArgumentResolverComposite resolverComposite;

	private MethodParameter paramInt;

	private MethodParameter paramStr;


	@BeforeEach
	public void setup() throws Exception {
		this.resolverComposite = new HandlerMethodArgumentResolverComposite();

		Method method = getClass().getDeclaredMethod("handle", Integer.class, String.class);
		paramInt = new MethodParameter(method, 0);
		paramStr = new MethodParameter(method, 1);
	}


	@Test
	public void supportsParameter() throws Exception {
		this.resolverComposite.addResolver(new StubArgumentResolver(Integer.class));

		assertThat(this.resolverComposite.supportsParameter(paramInt)).isTrue();
		assertThat(this.resolverComposite.supportsParameter(paramStr)).isFalse();
	}

	@Test
	public void resolveArgument() throws Exception {
		this.resolverComposite.addResolver(new StubArgumentResolver(55));
		Object resolvedValue = this.resolverComposite.resolveArgument(paramInt, null, null, null);

		assertThat(resolvedValue).isEqualTo(55);
	}

	@Test
	public void checkArgumentResolverOrder() throws Exception {
		this.resolverComposite.addResolver(new StubArgumentResolver(1));
		this.resolverComposite.addResolver(new StubArgumentResolver(2));
		Object resolvedValue = this.resolverComposite.resolveArgument(paramInt, null, null, null);

		assertThat(resolvedValue).as("Didn't use the first registered resolver").isEqualTo(1);
	}

	@Test
	public void noSuitableArgumentResolver() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				this.resolverComposite.resolveArgument(paramStr, null, null, null));
	}


	@SuppressWarnings("unused")
	private void handle(Integer arg1, String arg2) {
	}

}
