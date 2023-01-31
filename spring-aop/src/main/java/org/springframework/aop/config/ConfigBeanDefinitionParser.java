package org.springframework.aop.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.springframework.aop.aspectj.AspectJAfterAdvice;
import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.aop.aspectj.DeclareParentsAdvisor;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;

/**
 * {@link BeanDefinitionParser} for the {@code <aop:config>} tag.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Adrian Colyer
 * @author Mark Fisher
 * @author Ramnivas Laddad
 * @since 2.0
 */
class ConfigBeanDefinitionParser implements BeanDefinitionParser {		// 用于解析：<aop:config>

	private static final String ASPECT = "aspect";
	private static final String EXPRESSION = "expression";
	private static final String ID = "id";
	private static final String POINTCUT = "pointcut";
	private static final String ADVICE_BEAN_NAME = "adviceBeanName";
	private static final String ADVISOR = "advisor";
	private static final String ADVICE_REF = "advice-ref";
	private static final String POINTCUT_REF = "pointcut-ref";
	private static final String REF = "ref";
	private static final String BEFORE = "before";
	private static final String DECLARE_PARENTS = "declare-parents";
	private static final String TYPE_PATTERN = "types-matching";
	private static final String DEFAULT_IMPL = "default-impl";
	private static final String DELEGATE_REF = "delegate-ref";
	private static final String IMPLEMENT_INTERFACE = "implement-interface";
	private static final String AFTER = "after";
	private static final String AFTER_RETURNING_ELEMENT = "after-returning";
	private static final String AFTER_THROWING_ELEMENT = "after-throwing";
	private static final String AROUND = "around";
	private static final String RETURNING = "returning";
	private static final String RETURNING_PROPERTY = "returningName";
	private static final String THROWING = "throwing";
	private static final String THROWING_PROPERTY = "throwingName";
	private static final String ARG_NAMES = "arg-names";
	private static final String ARG_NAMES_PROPERTY = "argumentNames";
	private static final String ASPECT_NAME_PROPERTY = "aspectName";
	private static final String DECLARATION_ORDER_PROPERTY = "declarationOrder";
	private static final String ORDER_PROPERTY = "order";
	private static final int METHOD_INDEX = 0;
	private static final int POINTCUT_INDEX = 1;
	private static final int ASPECT_INSTANCE_FACTORY_INDEX = 2;

	private ParseState parseState = new ParseState();


	@Override
	@Nullable
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		// compositeDef = {CompositeComponentDefinition@2762} "aop:config"
		CompositeComponentDefinition compositeDef =
				new CompositeComponentDefinition(element.getTagName(), parserContext.extractSource(element));
		parserContext.pushContainingComponent(compositeDef);
		/*
		 * 注册：beanName = "internalAutoProxyCreator" --> {@link AspectJAwareAdvisorAutoProxyCreator}
		 * -----------------------------------------------------------------------------------------
		 * =>> AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(..);
		 * 	   BeanDefinition beanDefinition = registerOrEscalateApcAsRequired(AspectJAwareAdvisorAutoProxyCreator.class, registry, source);
		 * 	   =>> AopConfigUtils.registerOrEscalateApcAsRequired(...)
		 * 		   // cls = {Class@2203} "class org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator"
		 *         new RootBeanDefinition(cls);
		 * 		   // AUTO_PROXY_CREATOR_BEAN_NAME = "internalAutoProxyCreator"
		 * 		   registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);	// 注册到 Bean 工厂
		 *
		 * 	   useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
		 * 	   registerComponentIfNecessary(beanDefinition, parserContext);
		 */
		configureAutoProxyCreator(parserContext, element);
		/*
		 * 得到 <aop:config> 的子标签：
		 *
		 * childElts = {ArrayList@2287}  size = 2
		 * 		0 = {DeferredElementNSImpl@2264} "[aop:pointcut: null]"
		 * 		1 = {DeferredElementNSImpl@2240} "[aop:advisor : null]"
		 * 		2 = {DeferredElementNSImpl@2241} "[aop:aspect  : null]"
		 */
		List<Element> childElts = DomUtils.getChildElements(element);
		for (Element elt: childElts) {									// 按顺序定义，按顺序解析
			String localName = parserContext.getDelegate().getLocalName(elt);
			if (POINTCUT.equals(localName)) {							// <aop:pointcut>
				parsePointcut(elt, parserContext);		// AspectJExpressionPointcut
			}
			else if (ADVISOR.equals(localName)) {						// <aop:advisor>
				parseAdvisor(elt, parserContext);		// DefaultBeanFactoryPointcutAdvisor
			}
			else if (ASPECT.equals(localName)) {						// <aop:aspect>
				parseAspect(elt, parserContext);
			}
		}

		// 特别注意：这里又把 <aop:aspect> 对应的复合对象，移到了所属组 compositeDef 下面
		parserContext.popAndRegisterContainingComponent();
		return null;
	}

	/**
	 * Configures the auto proxy creator needed to support the {@link BeanDefinition BeanDefinitions}
	 * created by the '{@code <aop:config/>}' tag. Will force class proxying if the
	 * '{@code proxy-target-class}' attribute is set to '{@code true}'.
	 * @see AopNamespaceUtils
	 */
	private void configureAutoProxyCreator(ParserContext parserContext, Element element) {
		AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(parserContext, element);
	}

	/**
	 * Parses the supplied {@code <advisor>} element and registers the resulting
	 * {@link org.springframework.aop.Advisor} and any resulting {@link org.springframework.aop.Pointcut}
	 * with the supplied {@link BeanDefinitionRegistry}.
	 */
	private void parseAdvisor(Element advisorElement, ParserContext parserContext) {
		AbstractBeanDefinition advisorDef = createAdvisorBeanDefinition(advisorElement, parserContext);
		String id = advisorElement.getAttribute(ID);

		try {
			this.parseState.push(new AdvisorEntry(id));
			String advisorBeanName = id;

			// org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor
			if (StringUtils.hasText(advisorBeanName)) {
				parserContext.getRegistry().registerBeanDefinition(advisorBeanName, advisorDef);
			}
			else {
				advisorBeanName = parserContext.getReaderContext().registerWithGeneratedName(advisorDef);
			}

			// 解析对应的 'pointcut' and 'pointcut-ref'，不允许同时配置
			Object pointcut = parsePointcutProperty(advisorElement, parserContext);
			// 配置了内联切点：pointcut="..."
			if (pointcut instanceof BeanDefinition) {
				advisorDef.getPropertyValues().add(POINTCUT, pointcut);
				parserContext.registerComponent(
						new AdvisorComponentDefinition(advisorBeanName, advisorDef, (BeanDefinition) pointcut));
			}
			// 配置了引用切点：pointcut-ref="pointcut"
			else if (pointcut instanceof String) {
				advisorDef.getPropertyValues().add(POINTCUT, new RuntimeBeanReference((String) pointcut));
				parserContext.registerComponent(
						new AdvisorComponentDefinition(advisorBeanName, advisorDef));
			}
		}
		finally {
			this.parseState.pop();
		}
	}

	/**
	 * Create a {@link RootBeanDefinition} for the advisor described in the supplied. Does <strong>not</strong>
	 * parse any associated '{@code pointcut}' or '{@code pointcut-ref}' attributes.
	 */
	private AbstractBeanDefinition createAdvisorBeanDefinition(Element advisorElement, ParserContext parserContext) {
		RootBeanDefinition advisorDefinition = new RootBeanDefinition(DefaultBeanFactoryPointcutAdvisor.class);
		advisorDefinition.setSource(parserContext.extractSource(advisorElement));

		String adviceRef = advisorElement.getAttribute(ADVICE_REF);
		if (!StringUtils.hasText(adviceRef)) {
			parserContext.getReaderContext().error(
					"'advice-ref' attribute contains empty value.", advisorElement, this.parseState.snapshot());
		}
		else {
			advisorDefinition.getPropertyValues().add(
					ADVICE_BEAN_NAME, new RuntimeBeanNameReference(adviceRef));
		}

		if (advisorElement.hasAttribute(ORDER_PROPERTY)) {
			advisorDefinition.getPropertyValues().add(
					ORDER_PROPERTY, advisorElement.getAttribute(ORDER_PROPERTY));
		}

		return advisorDefinition;
	}

	/**
	 * 解析：<aop:aspect>
	 *
	 * <bean id="loggingAspect" class="org.springframework.beans.bean.aop.LoggingAspect"/>
	 * <aop:config proxy-target-class="true">
	 *      <aop:pointcut id="pointcut" ..></aop:pointcut>
	 * 		<aop:aspect ref="loggingAspect" order="2">
	 * 		    <aop:before method="beforeMethod" pointcut-ref="pointcut"/>
	 * 		    <aop:after  method="afterMethod"  pointcut-ref="pointcut"/>
	 * 		</aop:aspect>
	 * </aop:config>
	 */
	private void parseAspect(Element aspectElement, ParserContext parserContext) {
		String aspectId = aspectElement.getAttribute(ID);			//
		String aspectName = aspectElement.getAttribute(REF);		// <aop:aspect ref="loggingAspect"

		try {
			this.parseState.push(new AspectEntry(aspectId, aspectName));
			/*
			 * beanDefinitions = {ArrayList@2206}  size = 2
			 * 		0 = {RootBeanDefinition@2229} "Root bean: class [org.springframework.aop.aspectj.AspectJPointcutAdvisor]"
			 * 		1 = {RootBeanDefinition@2240} "Root bean: class [org.springframework.aop.aspectj.AspectJPointcutAdvisor]"
			 */
			List<BeanDefinition> beanDefinitions = new ArrayList<>();	// AspectJPointcutAdvisor
			/*
			 * beanReferences = {ArrayList@2207}  size = 3
			 * 		0 = {RuntimeBeanReference@2266} "<loggingAspect>"	// 代理对象 beanName
			 * 		1 = {RuntimeBeanReference@2267} "<pointcut>"
			 * 		2 = {RuntimeBeanReference@2268} "<pointcut>"		// 同一个？？重复了
			 */
			List<BeanReference> beanReferences = new ArrayList<>();
			// 解析：<aop:declare-parents> 子标签
			List<Element> declareParents = DomUtils.getChildElementsByTagName(aspectElement, DECLARE_PARENTS);
			for (int i = METHOD_INDEX; i < declareParents.size(); i++) {
				Element declareParentsElement = declareParents.get(i);
				beanDefinitions.add(parseDeclareParents(declareParentsElement, parserContext));
			}

			// We have to parse "advice" and all the advice kinds in one loop, to get the
			// ordering semantics right.
			NodeList nodeList = aspectElement.getChildNodes();
			boolean adviceFoundAlready = false;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				// 判断子标签：<aop:before method = ...，after | after-returning | after-throwing | around
				if (isAdviceNode(node, parserContext)) {
					// <aop:aspect ref=".."> 必须设置 ref，否则 代理谁呢. 这个 ref 只需解析一次就好，所有有个 adviceFoundAlready flag
					if (!adviceFoundAlready) {
						adviceFoundAlready = true;
						if (!StringUtils.hasText(aspectName)) {
							parserContext.getReaderContext().error(
									"<aspect> tag needs aspect bean reference via 'ref' attribute when declaring advices.",
									aspectElement, this.parseState.snapshot());
							return;
						}
						beanReferences.add(new RuntimeBeanReference(aspectName));
					}
					/**
					 * public class LoggingAspect {
					 *     public void beforeMethod(JoinPoint joinPoint) {...}
					 * }
					 *
					 * <bean id="loggingAspect" class="org.springframework.beans.bean.aop.LoggingAspect"/>
					 * <aop:aspect ref="loggingAspect" order="2">
					 *     <aop:before method="beforeMethod" pointcut-ref="pointcut"/>
					 * </aop:aspect>
					 *
					 * ------------------------
					 *
					 * 以 <aop:before method="beforeMethod" pointcut-ref="pointcut"/> 解析为例：
					 *
					 * ------------------------
					 *
					 * advisorDefinition = {RootBeanDefinition@2624} "Root bean: class [AspectJPointcutAdvisor]"
					 *
					 * 		// 01、解析 <aop:aspect order=".."> 属性
					 * 		propertyValues = {MutablePropertyValues@2282} "PropertyValues: length=1; bean property 'order'"
					 * 			propertyValueList = {ArrayList@2936}  size = 1
					 * 				0 = {PropertyValue@2938} "bean property 'order'"
					 * 					value = "2"
					 *
					 * 		// 02、主要 method 内容如何封装表示
					 * 		constructorArgumentValues = {ConstructorArgumentValues@2281}
					 * 			genericArgumentValues = {ArrayList@2745}  size = 1
					 *				0 = {ConstructorArgumentValues$ValueHolder@2747}
					 *
					 *					// 02-1、每个 <aop:xxx method="..." 被封装成 AspectJMethodBeforeAdvice
					 *					value = {RootBeanDefinition@2261} "Root bean: class [org.springframework.aop.aspectj.AspectJMethodBeforeAdvice]"
					 *
					 * 						// 02-1-1、aspectName：代理对象 beanName；declarationOrder：<aop:xxx method="..." 书写声明顺序
					 * 						propertyValues = {MutablePropertyValues@2753}
					 * 							propertyValueList = {ArrayList@2811}  size = 2
					 * 								0 = {PropertyValue@2813} "bean property 'aspectName'"
					 * 									value = "loggingAspect"
					 * 								1 = {PropertyValue@2814} "bean property 'declarationOrder'"
					 * 									value = {Integer@2785} 1
					 *
					 * 						// 02-1-2、为 AspectJMethodBeforeAdvice 构造函数服务，收集三部分：代理对象是哪个？代理方法是哪个？被代理方法是哪个？
					 * 						constructorArgumentValues = {ConstructorArgumentValues@2752}
					 *							indexedArgumentValues = {LinkedHashMap@2776}  size = 3
					 *
					 *								// 02-1-2-1、该通知的代理方法是哪个类、哪个方法；比如：在调用目标方法前，执行打印日志，这里指的就是打印日志的方法。
					 * 								{Integer@2783} 0 -> {ConstructorArgumentValues$ValueHolder@2784}
					 * 									value = {ConstructorArgumentValues$ValueHolder@2784}
					 * 										value = {RootBeanDefinition@2253} "Root bean: class [org.springframework.aop.config.MethodLocatingFactoryBean]"
					 * 											propertyValues = {MutablePropertyValues@2793}
					 * 												propertyValueList = {ArrayList@2798}  size = 2
					 * 													0 = {PropertyValue@2800} "bean property 'targetBeanName'"
					 * 														value = "loggingAspect"
					 * 													1 = {PropertyValue@2801} "bean property 'methodName'"
					 * 														value = "beforeMethod"
					 *
					 *								// 02-1-2-2、该通知的需要代理哪些方法，通过 pointcut 来描述
					 * 								{Integer@2785} 1 -> {ConstructorArgumentValues$ValueHolder@2786}
					 * 									value = {ConstructorArgumentValues$ValueHolder@2786}
					 * 										value = {RuntimeBeanReference@2809} "<pointcut>"
					 * 											beanName = "pointcut"
					 *
					 * 								// 02-1-2-3、代理对象的 beanName
					 * 								{Integer@2787} 2 -> {ConstructorArgumentValues$ValueHolder@2788}
					 *									value = {ConstructorArgumentValues$ValueHolder@2788}
					 *										value = {RootBeanDefinition@2254} "Root bean: class [org.springframework.aop.config.SimpleBeanFactoryAwareAspectInstanceFactory]"
					 * 											propertyValues = {MutablePropertyValues@2869}
					 * 												propertyValueList = {ArrayList@2909}  size = 1
					 *													0 = {PropertyValue@2911} "bean property 'aspectBeanName'"
					 * 														value = "loggingAspect"
					 */
					AbstractBeanDefinition advisorDefinition = parseAdvice(		// =>>
							aspectName, i, aspectElement, (Element) node, parserContext, beanDefinitions, beanReferences);
					beanDefinitions.add(advisorDefinition);						//
				}
			}

			AspectComponentDefinition aspectComponentDefinition = createAspectComponentDefinition(
					aspectElement, aspectId, beanDefinitions, beanReferences, parserContext);
			parserContext.pushContainingComponent(aspectComponentDefinition);

			List<Element> pointcuts = DomUtils.getChildElementsByTagName(aspectElement, POINTCUT);
			for (Element pointcutElement : pointcuts) {
				parsePointcut(pointcutElement, parserContext);
			}

			parserContext.popAndRegisterContainingComponent();
		}
		finally {
			this.parseState.pop();
		}
	}

	private AspectComponentDefinition createAspectComponentDefinition(
			Element aspectElement, String aspectId, List<BeanDefinition> beanDefs,
			List<BeanReference> beanRefs, ParserContext parserContext) {

		BeanDefinition[] beanDefArray = beanDefs.toArray(new BeanDefinition[0]);
		BeanReference[] beanRefArray = beanRefs.toArray(new BeanReference[0]);
		Object source = parserContext.extractSource(aspectElement);
		return new AspectComponentDefinition(aspectId, beanDefArray, beanRefArray, source);
	}

	/**
	 * Return {@code true} if the supplied node describes an advice type. May be one of:
	 * '{@code before}', '{@code after}', '{@code after-returning}',
	 * '{@code after-throwing}' or '{@code around}'.
	 */
	private boolean isAdviceNode(Node aNode, ParserContext parserContext) {
		if (!(aNode instanceof Element)) {
			return false;
		}
		else {
			String name = parserContext.getDelegate().getLocalName(aNode);
			return (BEFORE.equals(name) || AFTER.equals(name) || AFTER_RETURNING_ELEMENT.equals(name) ||
					AFTER_THROWING_ELEMENT.equals(name) || AROUND.equals(name));
		}
	}

	/**
	 * Parse a '{@code declare-parents}' element and register the appropriate
	 * DeclareParentsAdvisor with the BeanDefinitionRegistry encapsulated in the
	 * supplied ParserContext.
	 */
	private AbstractBeanDefinition parseDeclareParents(Element declareParentsElement, ParserContext parserContext) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DeclareParentsAdvisor.class);
		builder.addConstructorArgValue(declareParentsElement.getAttribute(IMPLEMENT_INTERFACE));
		builder.addConstructorArgValue(declareParentsElement.getAttribute(TYPE_PATTERN));

		String defaultImpl = declareParentsElement.getAttribute(DEFAULT_IMPL);
		String delegateRef = declareParentsElement.getAttribute(DELEGATE_REF);

		if (StringUtils.hasText(defaultImpl) && !StringUtils.hasText(delegateRef)) {
			builder.addConstructorArgValue(defaultImpl);
		}
		else if (StringUtils.hasText(delegateRef) && !StringUtils.hasText(defaultImpl)) {
			builder.addConstructorArgReference(delegateRef);
		}
		else {
			parserContext.getReaderContext().error(
					"Exactly one of the " + DEFAULT_IMPL + " or " + DELEGATE_REF + " attributes must be specified",
					declareParentsElement, this.parseState.snapshot());
		}

		AbstractBeanDefinition definition = builder.getBeanDefinition();
		definition.setSource(parserContext.extractSource(declareParentsElement));
		parserContext.getReaderContext().registerWithGeneratedName(definition);
		return definition;
	}

	/**
	 * Parses one of '{@code before}', '{@code after}', '{@code after-returning}',
	 * '{@code after-throwing}' or '{@code around}' and registers the resulting
	 * BeanDefinition with the supplied BeanDefinitionRegistry.
	 * @return the generated advice RootBeanDefinition
	 */
	private AbstractBeanDefinition parseAdvice(
			String aspectName, int order, Element aspectElement, Element adviceElement, ParserContext parserContext,
			List<BeanDefinition> beanDefinitions, List<BeanReference> beanReferences) {

		try {
			this.parseState.push(new AdviceEntry(parserContext.getDelegate().getLocalName(adviceElement)));

			// create the method factory bean		// <aop:before method=".." -> method 的封装信息
			RootBeanDefinition methodDefinition = new RootBeanDefinition(MethodLocatingFactoryBean.class);
			methodDefinition.getPropertyValues().add("targetBeanName", aspectName);
			methodDefinition.getPropertyValues().add("methodName", adviceElement.getAttribute("method"));
			methodDefinition.setSynthetic(true);

			// create instance factory definition
			RootBeanDefinition aspectFactoryDef =	// <aop:aspect ref=".."
					new RootBeanDefinition(SimpleBeanFactoryAwareAspectInstanceFactory.class);
			aspectFactoryDef.getPropertyValues().add("aspectBeanName", aspectName);
			aspectFactoryDef.setSynthetic(true);

			// register the pointcut
			// adviceDef = {RootBeanDefinition@2389} "Root bean: class [org.springframework.aop.aspectj.AspectJMethodBeforeAdvice]"
			AbstractBeanDefinition adviceDef = createAdviceDefinition(
					adviceElement, parserContext, aspectName, order, methodDefinition, aspectFactoryDef,
					beanDefinitions, beanReferences);

			// configure the advisor
			RootBeanDefinition advisorDefinition = new RootBeanDefinition(AspectJPointcutAdvisor.class);
			advisorDefinition.setSource(parserContext.extractSource(adviceElement));
			advisorDefinition.getConstructorArgumentValues().addGenericArgumentValue(adviceDef);
			if (aspectElement.hasAttribute(ORDER_PROPERTY)) {
				advisorDefinition.getPropertyValues().add(
						ORDER_PROPERTY, aspectElement.getAttribute(ORDER_PROPERTY));
			}

			// register the final advisor
			parserContext.getReaderContext().registerWithGeneratedName(advisorDefinition);

			return advisorDefinition;
		}
		finally {
			this.parseState.pop();
		}
	}

	/**
	 * Creates the RootBeanDefinition for a POJO advice bean. Also causes pointcut
	 * parsing to occur so that the pointcut may be associate with the advice bean.
	 * This same pointcut is also configured as the pointcut for the enclosing
	 * Advisor definition using the supplied MutablePropertyValues.
	 */
	private AbstractBeanDefinition createAdviceDefinition(
			Element adviceElement, ParserContext parserContext, String aspectName, int order,
			RootBeanDefinition methodDef, RootBeanDefinition aspectFactoryDef,
			List<BeanDefinition> beanDefinitions, List<BeanReference> beanReferences) {

		/**
		 * 根据「通知标签」，返回对应的类的 RootBeanDefinition
		 * 		<aop:before>                    -- AspectJMethodBeforeAdvice.class
		 * 		<aop:after>                     -- AspectJAfterAdvice.class
		 * 		<aop:after-returning>   		-- AspectJAfterReturningAdvice.class
		 * 		<aop:after-throwing>    		-- AspectJAfterThrowingAdvice.class
		 * 		<aop:around>                    -- AspectJAroundAdvice.class
		 * 	------------------------------
		 * 	eg：adviceElement	= {DeferredElementNSImpl@2263} "[aop:before: null]"
		 * 	adviceDefinition	= {RootBeanDefinition@2379} "Root bean: class [org.springframework.aop.aspectj.AspectJMethodBeforeAdvice]"
		 */
		RootBeanDefinition adviceDefinition = new RootBeanDefinition(getAdviceClass(adviceElement, parserContext));
		adviceDefinition.setSource(parserContext.extractSource(adviceElement));

		adviceDefinition.getPropertyValues().add(ASPECT_NAME_PROPERTY, aspectName);
		// order：就是 <aop:xxx method=".."> 书写顺序
		adviceDefinition.getPropertyValues().add(DECLARATION_ORDER_PROPERTY, order);

		if (adviceElement.hasAttribute(RETURNING)) {
			adviceDefinition.getPropertyValues().add(
					RETURNING_PROPERTY, adviceElement.getAttribute(RETURNING));
		}
		if (adviceElement.hasAttribute(THROWING)) {
			adviceDefinition.getPropertyValues().add(
					THROWING_PROPERTY, adviceElement.getAttribute(THROWING));
		}
		if (adviceElement.hasAttribute(ARG_NAMES)) {
			adviceDefinition.getPropertyValues().add(
					ARG_NAMES_PROPERTY, adviceElement.getAttribute(ARG_NAMES));
		}

		/*
		 * 返回这个「Advice 通知类」的构造函数：
		 * 	public AspectJMethodBeforeAdvice(Method aspectJBeforeAdviceMethod,		// <aop:before method="..."
		 * 									 AspectJExpressionPointcut pointcut,	// <aop:before pointcut-ref="..." 或 pointcut="..."
		 * 									 AspectInstanceFactory aif)				// <aop:aspect ref="..."
		 *  下面分三步：去收集该构造函数的入参：
		 * 	<aop:aspect ref="loggingAspect">				：ASPECT_INSTANCE_FACTORY_INDEX
		 * 		<aop:before method="beforeMethod"			：METHOD_INDEX
		 * 					pointcut-ref="pointcut"			：POINTCUT_INDEX
		 * 		/>
 		 */
		ConstructorArgumentValues cav = adviceDefinition.getConstructorArgumentValues();
		cav.addIndexedArgumentValue(METHOD_INDEX, methodDef);				// 参数 1：该通知对应的方法

		Object pointcut = parsePointcutProperty(adviceElement, parserContext);
		if (pointcut instanceof BeanDefinition) {
			cav.addIndexedArgumentValue(POINTCUT_INDEX, pointcut);			// 参数 2：该通知使用的 切点 Bean 定义
			beanDefinitions.add((BeanDefinition) pointcut);
		}
		else if (pointcut instanceof String) {
			RuntimeBeanReference pointcutRef = new RuntimeBeanReference((String) pointcut);
			cav.addIndexedArgumentValue(POINTCUT_INDEX, pointcutRef);		// 参数 2：该通知使用的 切点 Bean 定义 的 BeanName
			beanReferences.add(pointcutRef);
		}

		cav.addIndexedArgumentValue(ASPECT_INSTANCE_FACTORY_INDEX, aspectFactoryDef);	// 参数 3：被代理对象的 Bean 定义

		return adviceDefinition;
	}

	/**
	 * Gets the advice implementation class corresponding to the supplied {@link Element}.
	 *
	 * BEFORE 					-- AspectJMethodBeforeAdvice
	 * AFTER  					-- AspectJAfterAdvice
	 * AFTER_RETURNING_ELEMENT 	-- AspectJAfterReturningAdvice
	 * AFTER_THROWING_ELEMENT	-- AspectJAfterThrowingAdvice
	 * AROUND					-- AROUND
	 */
	private Class<?> getAdviceClass(Element adviceElement, ParserContext parserContext) {
		String elementName = parserContext.getDelegate().getLocalName(adviceElement);
		if (BEFORE.equals(elementName)) {
			return AspectJMethodBeforeAdvice.class;
		}
		else if (AFTER.equals(elementName)) {
			return AspectJAfterAdvice.class;
		}
		else if (AFTER_RETURNING_ELEMENT.equals(elementName)) {
			return AspectJAfterReturningAdvice.class;
		}
		else if (AFTER_THROWING_ELEMENT.equals(elementName)) {
			return AspectJAfterThrowingAdvice.class;
		}
		else if (AROUND.equals(elementName)) {
			return AspectJAroundAdvice.class;
		}
		else {
			throw new IllegalArgumentException("Unknown advice kind [" + elementName + "].");
		}
	}

	/**
	 * Parses the supplied {@code <pointcut>} and registers the resulting
	 * Pointcut with the BeanDefinitionRegistry.
	 */
	private AbstractBeanDefinition parsePointcut(Element pointcutElement, ParserContext parserContext) {	//
		String id = pointcutElement.getAttribute(ID);
		String expression = pointcutElement.getAttribute(EXPRESSION);

		AbstractBeanDefinition pointcutDefinition = null;

		try {
			this.parseState.push(new PointcutEntry(id));
			/*
			 *
			 * pointcutDefinition = {RootBeanDefinition@2742}
			 *      beanClass = {Class@2741} "class {@link org.springframework.aop.aspectj.AspectJExpressionPointcut}"
			 *      ...
			 *      propertyValues = {MutablePropertyValues@2753}
			 *           propertyValueList = {ArrayList@2758}  size = 1
			 *               0 = {PropertyValue@2760} "bean property 'expression'"
			 *                   name = "expression"
			 *                   value = "execution(* org.springframework.beans.bean.aop.ArithmeticCalculator.*(int, int))"
			 */
			pointcutDefinition = createPointcutDefinition(expression);
			pointcutDefinition.setSource(parserContext.extractSource(pointcutElement));

			String pointcutBeanName = id;
			if (StringUtils.hasText(pointcutBeanName)) {
				// Bean 注册：BeanRowName = pointcutBeanName = "pointcut"，
				parserContext.getRegistry().registerBeanDefinition(pointcutBeanName, pointcutDefinition);
			}
			else {
				// 如果没有配置 id，则取名类似：org.springframework.aop.aspectj.AspectJExpressionPointcut#0
				pointcutBeanName = parserContext.getReaderContext().registerWithGeneratedName(pointcutDefinition);
			}

			parserContext.registerComponent(new PointcutComponentDefinition(pointcutBeanName, pointcutDefinition, expression));
		}
		finally {
			this.parseState.pop();
		}

		return pointcutDefinition;
	}

	/**
	 * Parses the {@code pointcut} or {@code pointcut-ref} attributes of the supplied
	 * {@link Element} and add a {@code pointcut} property as appropriate. Generates a
	 * {@link org.springframework.beans.factory.config.BeanDefinition} for the pointcut if  necessary
	 * and returns its bean name, otherwise returns the bean name of the referred pointcut.
	 */
	@Nullable
	private Object parsePointcutProperty(Element element, ParserContext parserContext) {
		if (element.hasAttribute(POINTCUT) && element.hasAttribute(POINTCUT_REF)) {
			parserContext.getReaderContext().error(
					"Cannot define both 'pointcut' and 'pointcut-ref' on <advisor> tag.",
					element, this.parseState.snapshot());
			return null;
		}
		else if (element.hasAttribute(POINTCUT)) {
			// Create a pointcut for the anonymous pc and register it.
			String expression = element.getAttribute(POINTCUT);
			AbstractBeanDefinition pointcutDefinition = createPointcutDefinition(expression);
			pointcutDefinition.setSource(parserContext.extractSource(element));
			return pointcutDefinition;
		}
		else if (element.hasAttribute(POINTCUT_REF)) {
			String pointcutRef = element.getAttribute(POINTCUT_REF);
			if (!StringUtils.hasText(pointcutRef)) {
				parserContext.getReaderContext().error(
						"'pointcut-ref' attribute contains empty value.", element, this.parseState.snapshot());
				return null;
			}
			return pointcutRef;
		}
		else {
			parserContext.getReaderContext().error(
					"Must define one of 'pointcut' or 'pointcut-ref' on <advisor> tag.",
					element, this.parseState.snapshot());
			return null;
		}
	}

	/**
	 * Creates a {@link BeanDefinition} for the {@link AspectJExpressionPointcut} class using
	 * the supplied pointcut expression.
	 */
	protected AbstractBeanDefinition createPointcutDefinition(String expression) {
		// <aop:pointcut id="pointcutXML" expression="execution(* org.example.service.tx.TransactionByAnnotation.*(..))"/>

		RootBeanDefinition beanDefinition = new RootBeanDefinition(AspectJExpressionPointcut.class);
		beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		beanDefinition.setSynthetic(true);		// TODO：
		beanDefinition.getPropertyValues().add(EXPRESSION, expression);
		return beanDefinition;
	}

}
