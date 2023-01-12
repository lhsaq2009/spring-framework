package org.springframework.context.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.weaving.AspectJWeavingEnabler;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

/**
 * Parser for the &lt;context:load-time-weaver/&gt; element.
 *
 * @author Juergen Hoeller
 * @since 2.5
 */
class LoadTimeWeaverBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	/**
	 * The bean name of the internally managed AspectJ weaving enabler.
	 * @since 4.3.1
	 */
	public static final String ASPECTJ_WEAVING_ENABLER_BEAN_NAME =
			"org.springframework.context.config.internalAspectJWeavingEnabler";

	private static final String ASPECTJ_WEAVING_ENABLER_CLASS_NAME =
			"org.springframework.context.weaving.AspectJWeavingEnabler";

	private static final String DEFAULT_LOAD_TIME_WEAVER_CLASS_NAME =
			"org.springframework.context.weaving.DefaultContextLoadTimeWeaver";

	private static final String WEAVER_CLASS_ATTRIBUTE = "weaver-class";

	private static final String ASPECTJ_WEAVING_ATTRIBUTE = "aspectj-weaving";


	@Override
	protected String getBeanClassName(Element element) {
		if (element.hasAttribute(WEAVER_CLASS_ATTRIBUTE)) {		// weaver-class
			return element.getAttribute(WEAVER_CLASS_ATTRIBUTE);	// weaver-class
		}
		return DEFAULT_LOAD_TIME_WEAVER_CLASS_NAME;						// DefaultContextLoadTimeWeaver
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
		return ConfigurableApplicationContext.LOAD_TIME_WEAVER_BEAN_NAME;	// loadTimeWeaver
	}

	@Override	// core
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		// 判断是否配置开启 AOP，<context:load-time-weaver aspectj-weaving="..."/>，aspectj-weaving 有三个选项：on, off, auto-detect
		if (isAspectJWeavingEnabled(element.getAttribute(ASPECTJ_WEAVING_ATTRIBUTE), parserContext)) {

			// 需要开启 AspectJ，向 IOC 容器中，注册 AspectJWeavingEnabler 和 TODO：DefaultContextLoadTimeWeaver
			// TODO：<context:load-time-weaver weaver-class="xxxx.xxx"/> 配置了 weaver-class 这里会注册该类

			if (!parserContext.getRegistry().containsBeanDefinition(ASPECTJ_WEAVING_ENABLER_BEAN_NAME)) {
				RootBeanDefinition def = new RootBeanDefinition(ASPECTJ_WEAVING_ENABLER_CLASS_NAME);
				parserContext.registerBeanComponent(new BeanComponentDefinition(def, ASPECTJ_WEAVING_ENABLER_BEAN_NAME));
			}
			// 判断是否存在：AnnotationBeanConfigurerAspect
			if (isBeanConfigurerAspectEnabled(parserContext.getReaderContext().getBeanClassLoader())) {
				new SpringConfiguredBeanDefinitionParser().parse(element, parserContext);	// PPArf8u7^Nbw
			}
		}
	}

	/**
	 * 判断是否启用 AspectJ 的逻辑是：
	 * 		如果配置 aspectj-weaving=on 则表示开启
	 * 		如果配置 aspectj-weaving=off 则表示不开启，关闭
	 * 		其他情况，判断当前环境下是否存在 META-INF/aop.xml，如果存在，表示开启 AspectJ
	 */
	protected boolean isAspectJWeavingEnabled(String value, ParserContext parserContext) {
		if ("on".equals(value)) {			// <context:load-time-weaver aspectj-weaving="on"/>
			return true;
		}
		else if ("off".equals(value)) {		// <context:load-time-weaver aspectj-weaving="off"/>
			return false;
		}
		else {								// 存在资源：META-INF/aop.xml
			// Determine default...
			ClassLoader cl = parserContext.getReaderContext().getBeanClassLoader();
			return (cl != null && cl.getResource(AspectJWeavingEnabler.ASPECTJ_AOP_XML_RESOURCE) != null);
		}
	}

	protected boolean isBeanConfigurerAspectEnabled(@Nullable ClassLoader beanClassLoader) {
		return ClassUtils.isPresent(SpringConfiguredBeanDefinitionParser.BEAN_CONFIGURER_ASPECT_CLASS_NAME, beanClassLoader);
	}

}
