// ***********************************************
//
// 文件名(FileName)：FtpProperties.java
//
// 功能描述(Description)：FTP连接池属性配置
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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.apache.naming.SelectorContext.prefix;

/**
 * FTP连接池属性配置
 * @program: pbs
 * @description
 * @author: wl
 * @create: 2019-08-22 16:55
 **/
@Component
@ConfigurationProperties(prefix = "ftp")
@Data
public class FtpProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Integer connectTimeOut;
    private String controlEncoding;
    private Integer bufferSize;
    private Integer fileType;
    private Integer dataTimeout;
    private boolean useEPSVwithIPv4;
    private boolean passiveMode;
    private boolean blockWhenExhausted;
    private Integer maxWaitMillis;
    private Integer maxTotal;
    private Integer maxIdle;
    private Integer minIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean testOnCreate;
    private boolean testWhileIdle;
}
