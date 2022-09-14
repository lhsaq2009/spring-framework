package smoketest.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyCustomApplicationContext extends AnnotationConfigApplicationContext {

    @Override
    protected void initPropertySources() {
        super.initPropertySources();
        // 把 "MYSQL_HOST" 作为启动的时候必须验证的环境变量
        getEnvironment().setRequiredProperties("MYSQL_HOST");
    }
}

