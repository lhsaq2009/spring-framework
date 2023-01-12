package org.springframework.beans.factory.parsing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * {@link ComponentDefinition} implementation that holds one or more nested
 * {@link ComponentDefinition} instances, aggregating them into a named group
 * of components.
 *
 * @author Juergen Hoeller
 * @since 2.0.1
 * @see #getNestedComponents()
 */
public class CompositeComponentDefinition extends AbstractComponentDefinition {

	private final String name;

	@Nullable
	private final Object source;

	/**
	 * CASE 1：{@link org.springframework.aop.config.AopNamespaceUtils#registerComponentIfNecessary)}
	 *
	 * this.nestedComponents = {ArrayList@2217}  size = 2
	 * 		0 = {BeanComponentDefinition@2209} "Bean definition with name 'internalAutoProxyCreator'"
	 * 			beanDefinition = {RootBeanDefinition @2196} "Root bean: class [AspectJAwareAdvisorAutoProxyCreator]; "
	 * 			beanName = "org.springframework.aop.config.internalAutoProxyCreator"
	 *
	 * 		1 = {PointcutComponentDefinition@2219}
	 * 			pointcutBeanName = "pointcut"
	 * 			pointcutDefinition = {RootBeanDefinition@2211} "Root bean: class [org.springframework.aop.aspectj.AspectJExpressionPointcut];"
	 * 			description = "Pointcut <name='pointcut', expression=[execution(* org.springframework.beans.bean.aop.ArithmeticCalculator.*(int, int))]>"
	 *
	 * 		2 = {AdvisorComponentDefinition@2263} "Advisor <advice(ref)='loggingAdvisor', pointcut(ref)='pointcut'>"
	 * 			advisorBeanName = "advisor"
	 * 			advisorDefinition = {RootBeanDefinition@2201} "Root bean: class [org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor]; "
	 * 			description = "Advisor <advice(ref)='loggingAdvisor', pointcut(ref)='pointcut'>"
	 * 			beanReferences = {BeanReference[2]@2274}
	 * 				0 = {RuntimeBeanNameReference@2279} "<loggingAdvisor>"
	 * 				1 = {RuntimeBeanReference@2280} "<pointcut>"
	 */
	private final List<ComponentDefinition> nestedComponents = new ArrayList<>();


	/**
	 * Create a new CompositeComponentDefinition.
	 * @param name the name of the composite component
	 * @param source the source element that defines the root of the composite component
	 */
	public CompositeComponentDefinition(String name, @Nullable Object source) {
		Assert.notNull(name, "Name must not be null");
		this.name = name;
		this.source = source;
	}


	@Override
	public String getName() {
		return this.name;
	}

	@Override
	@Nullable
	public Object getSource() {
		return this.source;
	}


	/**
	 * Add the given component as nested element of this composite component.
	 * @param component the nested component to add
	 */
	public void addNestedComponent(ComponentDefinition component) {
		Assert.notNull(component, "ComponentDefinition must not be null");
		this.nestedComponents.add(component);
	}

	/**
	 * Return the nested components that this composite component holds.
	 * @return the array of nested components, or an empty array if none
	 */
	public ComponentDefinition[] getNestedComponents() {
		return this.nestedComponents.toArray(new ComponentDefinition[0]);
	}

}
