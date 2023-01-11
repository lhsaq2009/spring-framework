package org.springframework.beans.factory.aspectj;

import java.io.Serializable;

import org.aspectj.lang.annotation.control.CodeGenerationHint;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotationBeanWiringInfoResolver;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.wiring.BeanConfigurerSupport;

/**
 * Concrete aspect that uses the {@link Configurable} annotation to identify
 * which classes need autowiring.
 *
 * <p>The bean name to look up will be taken from the {@code @Configurable}
 * annotation if specified; otherwise, the default bean name to look up will be
 * the fully qualified name of the class being configured.
 *
 * @author Rod Johnson
 * @author Ramnivas Laddad
 * @author Juergen Hoeller
 * @author Adrian Colyer
 * @since 2.0
 * @see org.springframework.beans.factory.annotation.Configurable
 * @see org.springframework.beans.factory.annotation.AnnotationBeanWiringInfoResolver
 */
public aspect AnnotationBeanConfigurerAspect extends AbstractInterfaceDrivenDependencyInjectionAspect
		implements BeanFactoryAware, InitializingBean, DisposableBean {

	private final BeanConfigurerSupport beanConfigurerSupport = new BeanConfigurerSupport();


	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanConfigurerSupport.setBeanWiringInfoResolver(new AnnotationBeanWiringInfoResolver());
		this.beanConfigurerSupport.setBeanFactory(beanFactory);
	}

	@Override
	public void afterPropertiesSet() {
		this.beanConfigurerSupport.afterPropertiesSet();
	}

	@Override
	public void configureBean(Object bean) {
		this.beanConfigurerSupport.configureBean(bean);
	}

	@Override
	public void destroy() {
		this.beanConfigurerSupport.destroy();
	}


	public pointcut inConfigurableBean() : @this(Configurable);

	public pointcut preConstructionConfiguration() : preConstructionConfigurationSupport(*);

	/*
	 * An intermediary to match preConstructionConfiguration signature (that doesn't expose the annotation object)
	 */
	@CodeGenerationHint(ifNameSuffix="bb0")
	private pointcut preConstructionConfigurationSupport(Configurable c) : @this(c) && if (c.preConstruction());


	declare parents: @Configurable * implements ConfigurableObject;

	/*
	 * This declaration shouldn't be needed,
	 * except for an AspectJ bug (https://bugs.eclipse.org/bugs/show_bug.cgi?id=214559)
	 */
	declare parents: @Configurable Serializable+ implements ConfigurableDeserializationSupport;

}
