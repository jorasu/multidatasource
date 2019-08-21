
package com.example.mutidatasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 定义AOP切面以便拦截所有带有注解@DataSource的方法，取出值作为数据源标识放到DynamicDataSource的线程变量中
 */
@Component
@Order(-10)
@Aspect
public class DynamicDataSourceAspect {
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) throws Throwable {
        String dataSourceName = targetDataSource.value();
        System.out.println("注解的数据源为:" + dataSourceName);
        if (DynamicDataSource.contains(dataSourceName)) {
            DynamicDataSource.setDataSource(dataSourceName);
        } else {
            System.out.println("数据源" + dataSourceName + "不存在，使用默认数据源" + point.getSignature());
        }
    }

    @After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) throws Throwable {
        DynamicDataSource.clearDataSource();
    }
}

