package cn.com.intasect.multiple.file.core.config;

import cn.com.intasect.multiple.file.core.pool.FtpGenericObjectPool;
import cn.com.intasect.multiple.file.core.pool.FtpPooledObjectFactory;
import cn.com.intasect.multiple.file.core.properties.FtpClientPoolProperties;
import cn.com.intasect.multiple.file.core.properties.FtpConfigProperties;
import cn.com.intasect.multiple.file.core.util.FtpTemplate;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author YC
 */
@Configuration
@ConditionalOnProperty(name = "isct.common.fs.switchFileWay", havingValue = "FTP")
public class FtpClientAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpClientAutoConfiguration.class);

    @Bean
    public GenericObjectPoolConfig<FTPClient> ftpClientPoolConfig(FtpClientPoolProperties poolProperties) {
        GenericObjectPoolConfig<FTPClient> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMinIdle(poolProperties.getPoolMinIdle());
        poolConfig.setMaxIdle(poolProperties.getPoolMaxIdle());
        poolConfig.setMaxWaitMillis(poolProperties.getPoolMaxWait());
        poolConfig.setMaxTotal(poolProperties.getPoolMaxTotal());
        poolConfig.setTimeBetweenEvictionRunsMillis(poolProperties.getTimeBetweenEvictionRunsMillis());
        poolConfig.setTestOnCreate(poolProperties.isTestOnCreate());
        poolConfig.setTestOnBorrow(poolProperties.isTestOnBorrow());
        poolConfig.setTestOnReturn(poolProperties.isTestOnReturn());
        poolConfig.setTestWhileIdle(poolProperties.isTestWhileIdle());
        poolConfig.setFairness(true);
        poolConfig.setJmxEnabled(false);
        LOGGER.info("ftp client maxIdle:{},maxIdle:{},maxWaitMillis:{},maxTotal:{},timeBetweenEvictionRunsMillis:{}",
                poolProperties.getPoolMinIdle(),
                poolProperties.getPoolMaxIdle(),
                poolProperties.getPoolMaxWait(),
                poolProperties.getPoolMaxTotal(),
                poolProperties.getTimeBetweenEvictionRunsMillis());

        return poolConfig;
    }

    @Bean
    public FtpPooledObjectFactory ftpPooledObjectFactory(FtpConfigProperties configProperties) throws Exception {
        LOGGER.info("Create ftp client pool object");

        FtpPooledObjectFactory ftpPooledObjectFactory = new FtpPooledObjectFactory(configProperties);
        try {
            FTPClient ftpClient = ftpPooledObjectFactory.create();
            ftpPooledObjectFactory.destroyObject(new DefaultPooledObject<>(ftpClient));
        } catch (Exception e) {
            throw new BeanCreationException("Create ftp pool object factory failed nested exception: " + e.getMessage(), e);
        }
        return ftpPooledObjectFactory;
    }

    @Bean
    public FtpGenericObjectPool ftpGenericObjectPool(GenericObjectPoolConfig<FTPClient> ftpClientPoolConfig, FtpPooledObjectFactory ftpPooledObjectFactory) {
        LOGGER.info("Create ftp client object pool");
        return new FtpGenericObjectPool(ftpPooledObjectFactory, ftpClientPoolConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public FtpTemplate ftpTemplate(FtpGenericObjectPool objectPool, FtpConfigProperties configProperties) {
        LOGGER.info("Create ftp client template");
        return new FtpTemplate(objectPool, configProperties);
    }

}
