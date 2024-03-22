package cn.com.intasect.multiple.file.core.properties;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author YC
 */
@Component
@ConfigurationProperties(prefix = "isct.common.ftp.config")
public class FtpConfigProperties {

    /**
     * ftp 服务器地址
     */
    private String host = "localhost";

    /**
     * ftp 服务器端口
     */
    private Integer port = 21;

    /**
     * 用户名
     */
    private String username = "Anonymous";

    /**
     * 密码
     */
    private String password = "";

    /**
     * 连接超时时间（单位：毫秒）
     */
    private int connectTimeout = 3000;

    /**
     * 默认超时时间（单位：毫秒）
     */
    private int defaultTimeout = 5000;

    /**
     * 数据超时时间（单位：毫秒）
     */
    private int dataTimeout = 5000;

    /**
     * 是否为windows ftp
     */
    private boolean isWinFtp = true;

    /**
     * 字符编码集
     */
    private String controlEncoding = "UTF-8";

    /**
     * ftp 服务器语言编码
     */
    private String serverLanguageCode = "zh";

    /**
     * 文件传输类型（默认二进制）
     */
    private int fileType = FTPClient.BINARY_FILE_TYPE;

    /**
     * 是否使用被动模式传输
     */
    private boolean isLocalPassiveMode = true;

    /**
     * 默认文件操作目录
     */
    private String workDirectory = "/";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public int getDataTimeout() {
        return dataTimeout;
    }

    public void setDataTimeout(int dataTimeout) {
        this.dataTimeout = dataTimeout;
    }

    public boolean isWinFtp() {
        return isWinFtp;
    }

    public void setWinFtp(boolean winFtp) {
        isWinFtp = winFtp;
    }

    public String getControlEncoding() {
        return controlEncoding;
    }

    public void setControlEncoding(String controlEncoding) {
        this.controlEncoding = controlEncoding;
    }

    public String getServerLanguageCode() {
        return serverLanguageCode;
    }

    public void setServerLanguageCode(String serverLanguageCode) {
        this.serverLanguageCode = serverLanguageCode;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public boolean isLocalPassiveMode() {
        return isLocalPassiveMode;
    }

    public void setLocalPassiveMode(boolean localPassiveMode) {
        isLocalPassiveMode = localPassiveMode;
    }

    public String getWorkDirectory() {
        return workDirectory;
    }

    public void setWorkDirectory(String workDirectory) {
        this.workDirectory = workDirectory;
    }
}
