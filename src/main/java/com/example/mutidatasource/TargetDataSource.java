
package com.example.mutidatasource;

import java.lang.annotation.*;


/**
 * 注解，标识要使用的数据源
 *
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TargetDataSource {
    String value();
}

