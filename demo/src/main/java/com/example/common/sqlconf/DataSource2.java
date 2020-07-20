package com.example.common.sqlconf;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Si LingChao
 * @date 2020/6/28 17:55
 */
@Configuration
@MapperScan(basePackages = "com.example.mapper.test2",sqlSessionFactoryRef = "ds2SqlSessionFactory")
public class DataSource2 {

    /**
     * 返回data1数据库的数据源
     * @return
     */
    @Bean(name="ds2Source")
    @ConfigurationProperties(prefix = "spring.datasource.ds2")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    /**
     * 返回data1数据库的会话工厂
     * @param ds
     * @return
     * @throws Exception
     */
    @Bean(name = "ds2SqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("ds2Source") DataSource ds) throws Exception{
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(ds);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/test2/*.xml"));
        return bean.getObject();
    }

    /**
     * 返回ds数据库的会话模板
     * @param sessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "data2SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("ds2SqlSessionFactory") SqlSessionFactory sessionFactory){
        return  new SqlSessionTemplate(sessionFactory);
    }

//    /**
//     * 返回ds1数据库的事务
//     * @param ds
//     * @return
//     */
//    @Bean(name = "ds2TransactionManager")
//    @Primary
//    public DataSourceTransactionManager transactionManager(@Qualifier("ds2Source") DataSource ds){
//        return new DataSourceTransactionManager(ds);
//    }
}
