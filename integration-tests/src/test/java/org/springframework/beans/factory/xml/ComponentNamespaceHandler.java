package org.springframework.beans.factory.xml;

public class ComponentNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("component", new ComponentBeanDefinitionParser());
	}
}
