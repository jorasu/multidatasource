
package com.example.mutidatasource;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * 对配置文件中的数据源进行解析
 */

@Component
public class DynamicDataSourceBuilder implements EnvironmentAware {
    //定义默认数据源
    private static  final String defaultDataSourceName="datasource";
    private static final String defaultDataSourceType="com.alibaba.druid.pool.DruidDataSource";
    private static final String defaultDataSourcePrefix="spring.datasource";

    //自定义数据源的命名规则
    private static final String customDataSourcePrefix="spring.multi.datasource";
    //自定义数据源的名称，以"."分割
    private static final String customDataSourceNames=customDataSourcePrefix+".name";
    //默认数据源
    private DataSource defaultDataSource;
    //动态数据源
    private Map<String, DataSource> targetDataSources=new HashMap<>();

    private class DataSourcePropertyKey{
        static final String url = "url";
        static final String username = "username";
        static final String password = "password";
        static final String driverClassName = "driverClassName";
        static final String type = "type";
    }
    @Override
    public void setEnvironment(Environment environment) {
        initDefaultDataSource(environment);
        initCustomDataSources(environment);

    }


     /**
     * 加载主默认源配置
     * @param env
     */

    private void initDefaultDataSource(Environment env) {
        defaultDataSource = buildDataSource(env, defaultDataSourcePrefix);
        dataBinder(defaultDataSource, env);
        // 将主数据源添加到数据源库中
        targetDataSources.put(defaultDataSourceName, defaultDataSource);
    }


     /**
     * 初始化定制数据源
     * @param env
     */

    private void initCustomDataSources(Environment env) {
        String dsNames = env.getProperty(customDataSourceNames);
        if (dsNames != null && !dsNames.equals("")) {
            for (String name : dsNames.split(",")) {
                DataSource customDataSource = buildDataSource(env, customDataSourcePrefix + "." + name);
                dataBinder(customDataSource, env);
                targetDataSources.put(name, customDataSource);
            }
        }
    }




/**
     * 创建数据源
     * @param env
     * @param dsPrefix
     * @return
     */

    private DataSource buildDataSource(Environment env, String dsPrefix) {
        try {
            String prefix = dsPrefix + ".";
            String dbType = env.getProperty(prefix + DataSourcePropertyKey.type, defaultDataSourceType);
            Class<? extends DataSource> dsType = (Class<? extends DataSource>) Class.forName(dbType);
            DataSource dataSource = DataSourceBuilder.create()
                    .driverClassName(env.getProperty(prefix + DataSourcePropertyKey.driverClassName))
                    .url(env.getProperty(prefix + DataSourcePropertyKey.url))
                    .username(env.getProperty(prefix + DataSourcePropertyKey.username))
                    .password(env.getProperty(prefix + DataSourcePropertyKey.password)).type(dsType).build();
            return dataSource;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


/**
     * 为DataSource绑定其他属性
     * @param dataSource
     * @param env
     */

    private void dataBinder(DataSource dataSource, Environment env) {
        // https://github.com/spring-projects/spring-boot/wiki/Relaxed-Binding-2.0
        // 从配置中读取时配置名要遵循 “uniform ”
        // 基础配置
        String[] dkeys = { defaultDataSourcePrefix + ".filters", defaultDataSourcePrefix + ".max-active",
                defaultDataSourcePrefix + ".initial-size", defaultDataSourcePrefix + ".max-wait",
                defaultDataSourcePrefix + ".min-idle", defaultDataSourcePrefix + ".time-between-eviction-runs-millis",
                defaultDataSourcePrefix + ".min-evictable-idle-time-millis",
                defaultDataSourcePrefix + ".validation-query", defaultDataSourcePrefix + ".test-while-idle",
                defaultDataSourcePrefix + ".test-on-borrow", defaultDataSourcePrefix + ".test-on-return",
                defaultDataSourcePrefix + ".pool-prepared-statements",
                defaultDataSourcePrefix + ".max-open-prepared-statements" };

        Map<String, Object> def = new HashMap<String, Object>();
        for (String dkey : dkeys) {
            def.put(dkey, env.getProperty(dkey));
        }
        PropertyValues dataSourcePropertyValues = new MutablePropertyValues(def);
        DataBinder dataBinder = new DataBinder(dataSource);
        dataBinder.bind(dataSourcePropertyValues);

    }
    public DataSource getDefaultDataSource(){
        return defaultDataSource;
}

    public Map<String,DataSource> getTargetDataSources(){
        return targetDataSources;
    }
}

