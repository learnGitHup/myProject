// ***********************************************
//
// 文件名(FileName)：FtpPool.java
//
// 功能描述(Description)：FTP连接池配置类
//
// 数据表
//
// 作者(Author)：王亮
//
// 创建日期(Created Date)：2019-10-18
//
// 修改记录(Revision Record)：
//
// ***********************************************
package com.example.common.ftp.util;

import com.example.common.ftp.client.FTPClientHelper;
import com.example.common.ftp.config.FtpPoolConfig;
import com.example.common.ftp.core.FTPClientFactory;
import com.example.common.ftp.core.FTPClientPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * FTP连接池配置类
 * @program: pbs
 * @description
 * @author: wl
 * @create: 2019-08-22 16:42
 **/
@Component
@Slf4j
public class FtpPool {
    @Autowired
    private FtpProperties properties;
    @Bean(name = "poolConfig")
    protected FtpPoolConfig setPoolConfig(){
        log.info("加载连接池配置"+properties.toString());
        FtpPoolConfig poolConfig=new FtpPoolConfig();
        poolConfig.setHost(properties.getHost());
        poolConfig.setPort(properties.getPort());
        poolConfig.setUsername(properties.getUsername());
        poolConfig.setPassword(properties.getPassword());
        poolConfig.setConnectTimeOut(properties.getConnectTimeOut());
        poolConfig.setControlEncoding(properties.getControlEncoding());
        poolConfig.setBufferSize(properties.getBufferSize());
        poolConfig.setFileType(properties.getFileType());
        poolConfig.setDataTimeout(properties.getDataTimeout());
        poolConfig.setUseEPSVwithIPv4(properties.isUseEPSVwithIPv4());
        poolConfig.setPassiveMode(properties.isPassiveMode());
        poolConfig.setBlockWhenExhausted(properties.isBlockWhenExhausted());
        poolConfig.setMaxWaitMillis(properties.getMaxWaitMillis());
        poolConfig.setMaxTotal(properties.getMaxTotal());
        poolConfig.setMaxIdle(properties.getMaxIdle());
        poolConfig.setMinIdle(properties.getMinIdle());
        poolConfig.setTestOnBorrow(properties.isTestOnBorrow());
        poolConfig.setTestOnReturn(properties.isTestOnReturn());
        poolConfig.setTestOnCreate(properties.isTestOnCreate());
        poolConfig.setTestWhileIdle(properties.isTestWhileIdle());
        return poolConfig;
    }

    @Bean(name="ftpClientFactory")
    protected FTPClientFactory setFtpClientFactory(@Qualifier("poolConfig") FtpPoolConfig config){
        FTPClientFactory factory=new FTPClientFactory();
        factory.setFtpPoolConfig(config);
        return factory;
    }

    @Bean(name = "ftpClinetPool")
    protected FTPClientPool setFtpClinetPool(@Qualifier("ftpClientFactory")FTPClientFactory factory){
        FTPClientPool ftpClientPool = new FTPClientPool(factory);
        return ftpClientPool;
    }

    @Bean
    public FTPClientHelper setFtpHelper(@Qualifier("ftpClinetPool")FTPClientPool pool){
        FTPClientHelper helper=new FTPClientHelper();
        helper.setFtpClientPool(pool);
        return helper;
    }
}
