package org.springframework.cache.jcache.interceptor;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheMethodDetails;
import javax.cache.annotation.CacheRemoveAll;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stephane Nicoll
 */
public class CacheRemoveAllOperationTests extends AbstractCacheOperationTests<CacheRemoveAllOperation> {

	@Override
	protected CacheRemoveAllOperation createSimpleOperation() {
		CacheMethodDetails<CacheRemoveAll> methodDetails = create(CacheRemoveAll.class,
				SampleObject.class, "simpleRemoveAll");

		return new CacheRemoveAllOperation(methodDetails, defaultCacheResolver);
	}

	@Test
	public void simpleRemoveAll() {
		CacheRemoveAllOperation operation = createSimpleOperation();

		CacheInvocationParameter[] allParameters = operation.getAllParameters();
		assertThat(allParameters.length).isEqualTo(0);
	}

}
