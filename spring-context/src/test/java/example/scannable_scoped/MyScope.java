package example.scannable_scoped;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.ScopedProxyMode;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyScope {
	String value() default ConfigurableBeanFactory.SCOPE_SINGLETON;
	ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;
}
