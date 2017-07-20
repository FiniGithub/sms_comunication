package com.dzd.db.mysql;

import com.dzd.utils.PropertiesUtils;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @Author WHL
 * @Date 2017-3-23.
 */
public class MysqlOperator extends  JdbcTemplate{
    public final static MysqlOperator I = new MysqlOperator();

    public MysqlOperator(){
        com.alibaba.druid.pool.DruidDataSource dataSource = new com.alibaba.druid.pool.DruidDataSource();

        dataSource.setDriverClassName( PropertiesUtils.common.getProperty("driverClassName"));
        dataSource.setUrl( PropertiesUtils.common.getProperty("url") );
        dataSource.setUsername( PropertiesUtils.common.getProperty("username")  );
        dataSource.setPassword( PropertiesUtils.common.getProperty("password")  );
        dataSource.setMaxActive(  Integer.valueOf( PropertiesUtils.common.getProperty("maxActive") )  );
        dataSource.setInitialSize(  Integer.valueOf( PropertiesUtils.common.getProperty("initialSize") )  );
        dataSource.setMaxWait(  Integer.valueOf( PropertiesUtils.common.getProperty("maxWait") )  );
        dataSource.setMaxIdle(  Integer.valueOf( PropertiesUtils.common.getProperty("maxIdle") )  );
        dataSource.setMinIdle(  Integer.valueOf( PropertiesUtils.common.getProperty("minIdle") )  );
        dataSource.setRemoveAbandoned(    PropertiesUtils.common.getProperty("removeAbandoned").equals("true")  );
        dataSource.setRemoveAbandonedTimeout(Integer.valueOf( PropertiesUtils.common.getProperty("removeAbandonedTimeout") ) );


        this.setDataSource( dataSource );
    }

}
