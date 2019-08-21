
package com.example.mutidatasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 动态配置多数据源
 */

@Component
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Autowired
    private DynamicDataSourceBuilder dynamicDataSourceBuilder;

    //线程本地环境
    /**
     * 注意：数据源标识保存在线程变量中，避免多线程操作数据源时互相干扰
     */
    private static final ThreadLocal<String> dsThreads=new ThreadLocal<>();

    //维护自定义数据源的名称集合
    private static List<String> dsNameList = new ArrayList<String>();

    //获取数据源
    public static String getDsThread(){
        return dsThreads.get();
    }
    //设置数据源
    public static void setDataSource(String name){
        dsThreads.set(name);
    }

    static int addDsName(String name){
        dsNameList.add(name);
        return dsNameList.size();
    }
   //清除数据源
    public static void clearDataSource(){
        dsThreads.remove();
    }

    public static boolean contains(String name){
        return dsNameList.contains(name);
    }

    @Override
    public void afterPropertiesSet(){
       //所有数据源，包括默认数据源
        Map<String, DataSource> customDataSources=dynamicDataSourceBuilder.getTargetDataSources();
        //lookup key
        customDataSources.forEach((key,da)->addDsName(key));

        //DataSource
        DataSource defaultDataSource = dynamicDataSourceBuilder.getDefaultDataSource();
        setDefaultTargetDataSource(defaultDataSource);
        setTargetDataSources(new HashMap<>(customDataSources));

        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dsThreads.get();
    }
}

