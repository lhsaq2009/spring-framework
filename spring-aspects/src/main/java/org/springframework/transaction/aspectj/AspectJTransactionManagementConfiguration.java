package org.springframework.transaction.aspectj;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.annotation.AbstractTransactionManagementConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurationSelector;
import org.springframework.transaction.config.TransactionManagementConfigUtils;

/**
 * {@code @Configuration} class that registers the Spring infrastructure beans necessary
 * to enable AspectJ-based annotation-driven transaction management for Spring's own
 * {@link org.springframework.transaction.annotation.Transactional} annotation.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 * @see EnableTransactionManagement
 * @see TransactionManagementConfigurationSelector
 * @see AspectJJtaTransactionManagementConfiguration
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class AspectJTransactionManagementConfiguration extends AbstractTransactionManagementConfiguration {

	@Bean(name = TransactionManagementConfigUtils.TRANSACTION_ASPECT_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public AnnotationTransactionAspect transactionAspect() {
		AnnotationTransactionAspect txAspect = AnnotationTransactionAspect.aspectOf();
		if (this.txManager != null) {
			txAspect.setTransactionManager(this.txManager);
		}
		return txAspect;
	}

}
