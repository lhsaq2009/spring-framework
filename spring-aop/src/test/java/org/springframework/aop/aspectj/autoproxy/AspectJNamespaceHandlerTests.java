package org.springframework.aop.aspectj.autoproxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.beans.testfixture.beans.CollectingReaderEventListener;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 * @author Chris Beams
 */
public class AspectJNamespaceHandlerTests {

	private ParserContext parserContext;

	private CollectingReaderEventListener readerEventListener = new CollectingReaderEventListener();

	private BeanDefinitionRegistry registry = new DefaultListableBeanFactory();


	@BeforeEach
	public void setUp() throws Exception {
		SourceExtractor sourceExtractor = new PassThroughSourceExtractor();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.registry);
		XmlReaderContext readerContext =
				new XmlReaderContext(null, null, this.readerEventListener, sourceExtractor, reader, null);
		this.parserContext = new ParserContext(readerContext, null);
	}

	@Test
	public void testRegisterAutoProxyCreator() throws Exception {
		AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(this.parserContext, null);
		assertThat(registry.getBeanDefinitionCount()).as("Incorrect number of definitions registered").isEqualTo(1);

		AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(this.parserContext, null);
		assertThat(registry.getBeanDefinitionCount()).as("Incorrect number of definitions registered").isEqualTo(1);
	}

	@Test
	public void testRegisterAspectJAutoProxyCreator() throws Exception {
		AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(this.parserContext, null);
		assertThat(registry.getBeanDefinitionCount()).as("Incorrect number of definitions registered").isEqualTo(1);

		AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(this.parserContext, null);
		assertThat(registry.getBeanDefinitionCount()).as("Incorrect number of definitions registered").isEqualTo(1);

		BeanDefinition definition = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
		assertThat(definition.getBeanClassName()).as("Incorrect APC class").isEqualTo(AspectJAwareAdvisorAutoProxyCreator.class.getName());
	}

	@Test
	public void testRegisterAspectJAutoProxyCreatorWithExistingAutoProxyCreator() throws Exception {
		AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(this.parserContext, null);
		assertThat(registry.getBeanDefinitionCount()).isEqualTo(1);

		AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(this.parserContext, null);
		assertThat(registry.getBeanDefinitionCount()).as("Incorrect definition count").isEqualTo(1);

		BeanDefinition definition = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
		assertThat(definition.getBeanClassName()).as("APC class not switched").isEqualTo(AspectJAwareAdvisorAutoProxyCreator.class.getName());
	}

	@Test
	public void testRegisterAutoProxyCreatorWhenAspectJAutoProxyCreatorAlreadyExists() throws Exception {
		AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(this.parserContext, null);
		assertThat(registry.getBeanDefinitionCount()).isEqualTo(1);

		AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(this.parserContext, null);
		assertThat(registry.getBeanDefinitionCount()).as("Incorrect definition count").isEqualTo(1);

		BeanDefinition definition = registry.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
		assertThat(definition.getBeanClassName()).as("Incorrect APC class").isEqualTo(AspectJAwareAdvisorAutoProxyCreator.class.getName());
	}

}
