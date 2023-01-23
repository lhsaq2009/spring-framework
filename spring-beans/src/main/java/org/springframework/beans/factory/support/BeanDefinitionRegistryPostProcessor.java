/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * Extension to the standard {@link BeanFactoryPostProcessor} SPI, allowing for
 * the registration of further bean definitions <i>before</i> regular
 * BeanFactoryPostProcessor detection kicks in. In particular,
 * BeanDefinitionRegistryPostProcessor may register further bean definitions
 * which in turn define BeanFactoryPostProcessor instances.<br/><br/>
 *
 * 对标准 {@link BeanFactoryPostProcessor} SPI 的扩展，
 * 允许在常规 BeanFactoryPostProcessor 检测开始之前，注册更多的 bean definition。
 * 特别是，BeanDefinitionRegistryPostProcessor 可以注册更多的 bean definition，
 * 这些 bean definition 又定义了 BeanFactoryPostProcessor 实例。<br/><br/>
 *
 * @author Juergen Hoeller
 * @since 3.0.1
 * @see org.springframework.context.annotation.ConfigurationClassPostProcessor
 *
 * 注册 Bean 到 Spring 容器
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

	/**
	 * 提供了丰富的方法，来操作 BeanDefinition，判断、注册、移除等方法：
	 *
	 * Modify the application context's internal bean definition registry after its
	 * standard initialization. All regular bean definitions will have been loaded,
	 * but no beans will have been instantiated yet. This allows for adding further
	 * bean definitions before the next post-processing phase kicks in.<br/><br/>
	 *
	 * 在标准初始化后，修改 application context's 的内部 Bean 定义注册表。
	 * 所有常规的 Bean definition 都将被加载，但尚未实例化任何 bean。
	 * 这允许在 next post-processing 阶段开始之前，添加更多的 Bean 定义。<br/><br/>
	 *
	 * @param registry the bean definition registry used by the application context
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;

}
