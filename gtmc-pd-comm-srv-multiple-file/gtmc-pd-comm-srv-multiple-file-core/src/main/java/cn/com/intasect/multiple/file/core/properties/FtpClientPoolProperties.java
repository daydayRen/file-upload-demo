package cn.com.intasect.multiple.file.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author YC
 */
@Component
@ConfigurationProperties(prefix = "isct.common.ftp.pool")
public class FtpClientPoolProperties {

    private int poolMaxTotal = 10;

    private int poolMaxIdle = 2;

    private int poolMinIdle = 0;

    private long poolMaxWait = 180000;

    private long timeBetweenEvictionRunsMillis = 110000;

    /**
     * 池对象创建时时验证是否正常可用
     */
    private boolean testOnCreate = true;

    /**
     * 池对象借出时验证是否正常可用
     */
    private boolean testOnBorrow = true;


    /**
     * 池对象归还时验证是否正常可用
     */
    private boolean testOnReturn = true;

    /**
     * 空闲连接自动被空闲连接回收器
     */
    private boolean isTestWhileIdle = true;

    public int getPoolMaxTotal() {
        return poolMaxTotal;
    }

    public void setPoolMaxTotal(int poolMaxTotal) {
        this.poolMaxTotal = poolMaxTotal;
    }

    public int getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(int poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    public int getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(int poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    public long getPoolMaxWait() {
        return poolMaxWait;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public void setPoolMaxWait(long poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    public boolean isTestOnCreate() {
        return testOnCreate;
    }

    public void setTestOnCreate(boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return isTestWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        isTestWhileIdle = testWhileIdle;
    }
}
