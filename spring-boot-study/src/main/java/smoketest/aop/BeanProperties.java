package smoketest.aop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>〈功能概述〉.
 *
 * @author haisen /20229/14
 */
@Component
public class BeanProperties {

    @Value("${name}")
    private String name;
}
