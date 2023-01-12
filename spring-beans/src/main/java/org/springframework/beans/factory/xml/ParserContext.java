package org.springframework.beans.factory.xml;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.lang.Nullable;
import org.w3c.dom.Element;

/**
 * Context that gets passed along a bean definition parsing process,
 * encapsulating all relevant configuration as well as state.
 * Nested inside an {@link XmlReaderContext}.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see XmlReaderContext
 * @see BeanDefinitionParserDelegate
 */
public final class ParserContext {

	private final XmlReaderContext readerContext;						//

	private final BeanDefinitionParserDelegate delegate;				//

	@Nullable
	private BeanDefinition containingBeanDefinition;
	/**
	 * TODO：总结把相关的 beanDefinition 组合到一起，便于使用。
	 *
	 * push：
	 * 		解析 <aop:config> 时
	 *      {@link org.springframework.aop.config.ConfigBeanDefinitionParser#parse}
	 *       	CompositeComponentDefinition compositeDef = ...
	 *       	parserContext.pushContainingComponent(compositeDef);
	 *
	 * read：{@link ParserContext#registerComponent} =>> getContainingComponent()
	 *
	 * ---------------------------------------------
	 *
	 * containingComponents = {ArrayDeque@2191}  size = 2
	 * 		1 = {CompositeComponentDefinition@2192} "aop:config"
	 * 			name = "aop:config"
	 *			nestedComponents = {ArrayList@2199}  size = 3
	 * 				0 = {BeanComponentDefinition@2210} "Bean definition with name 'org.springframework.aop.config.internalAutoProxyCreator'"
	 * 				1 = {PointcutComponentDefinition@2235} "Pointcut <name='pointcut', expression=[execution(* org.springframework.beans.bean.aop.ArithmeticCalculator.*(int, int))]>"
	 * 				2 = {AdvisorComponentDefinition@2242} "Advisor <advice(ref)='loggingAdvisor', pointcut(ref)='pointcut'>"
	 *
	 * 				// 从 containingComponents 下面，转移到所属的这个组下面
	 *		 		4 = {AspectComponentDefinition@2249} ""
	 *		 			name = ""
	 *		 			beanDefinitions = {BeanDefinition[2]@2251}
	 *		 				0 = {RootBeanDefinition@2257} "Root bean: class [org.springframework.aop.aspectj.AspectJPointcutAdvisor]"
	 *		 				1 = {RootBeanDefinition@2258} "Root bean: class [org.springframework.aop.aspectj.AspectJPointcutAdvisor]"
	 *		 			beanReferences = {BeanReference[3]@2252}
	 *		 				0 = {RuntimeBeanReference@2261} "<loggingAspect>"
	 *		 				1 = {RuntimeBeanReference@2262} "<pointcut>"
	 *		 				2 = {RuntimeBeanReference@2263} "<pointcut>"
	 */
	private final Deque<CompositeComponentDefinition> containingComponents = new ArrayDeque<>();


	public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate) {
		this.readerContext = readerContext;
		this.delegate = delegate;
	}

	public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate,
			@Nullable BeanDefinition containingBeanDefinition) {

		this.readerContext = readerContext;
		this.delegate = delegate;
		this.containingBeanDefinition = containingBeanDefinition;
	}


	public final XmlReaderContext getReaderContext() {
		return this.readerContext;
	}

	public final BeanDefinitionRegistry getRegistry() {
		return this.readerContext.getRegistry();
	}

	public final BeanDefinitionParserDelegate getDelegate() {
		return this.delegate;
	}

	@Nullable
	public final BeanDefinition getContainingBeanDefinition() {
		return this.containingBeanDefinition;
	}

	public final boolean isNested() {
		return (this.containingBeanDefinition != null);
	}

	public boolean isDefaultLazyInit() {
		return BeanDefinitionParserDelegate.TRUE_VALUE.equals(this.delegate.getDefaults().getLazyInit());
	}

	@Nullable
	public Object extractSource(Object sourceCandidate) {
		return this.readerContext.extractSource(sourceCandidate);
	}

	@Nullable
	public CompositeComponentDefinition getContainingComponent() {		//
		return this.containingComponents.peek();
	}

	public void pushContainingComponent(CompositeComponentDefinition containingComponent) {
		this.containingComponents.push(containingComponent);
	}

	public CompositeComponentDefinition popContainingComponent() {
		return this.containingComponents.pop();
	}

	public void popAndRegisterContainingComponent() {
		registerComponent(popContainingComponent());
	}

	public void registerComponent(ComponentDefinition component) {
		CompositeComponentDefinition containingComponent = getContainingComponent();
		if (containingComponent != null) {
			containingComponent.addNestedComponent(component);
		}
		else {
			this.readerContext.fireComponentRegistered(component);
		}
	}

	public void registerBeanComponent(BeanComponentDefinition component) {
		BeanDefinitionReaderUtils.registerBeanDefinition(component, getRegistry());
		registerComponent(component);
	}

}
