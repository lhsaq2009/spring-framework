package smoketest.aop;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        ctx.getEnvironment().setRequiredProperties("MYSQL_HOST");   // 把 "MYSQL_HOST" 作为启动的时候必须验证的环境变量
    }
}


