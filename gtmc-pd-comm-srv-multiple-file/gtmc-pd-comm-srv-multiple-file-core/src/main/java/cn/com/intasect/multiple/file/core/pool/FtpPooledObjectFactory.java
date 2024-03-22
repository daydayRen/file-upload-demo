package cn.com.intasect.multiple.file.core.pool;

import cn.com.intasect.multiple.file.constant.FileSrvCode;
import cn.com.intasect.multiple.file.core.exception.FileSrvException;
import cn.com.intasect.multiple.file.core.properties.FtpConfigProperties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * @author YC
 */
public class FtpPooledObjectFactory extends BasePooledObjectFactory<FTPClient> {

    private static final Logger log = LoggerFactory.getLogger(FtpPooledObjectFactory.class);

    private static final String OPTS_UTF8_COMMAND = "OPTS UTF8";

    private static final String OPTS_UTF8_COMMAND_ARGS = "ON";

    private static final String DEFAULT_ENCODING = "UTF-8";

    private FtpConfigProperties configProperties;

    public FtpPooledObjectFactory(FtpConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    /**
      * @Author: renyitian
      * @Description: 创建客户端
      * @Date: 14:27 2024/3/18
      * @Param:  * 参数
      */
    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = FtpPooledObjectCreateFactory.createConnection(configProperties);
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            ftpClient.abort();
            ftpClient.disconnect();
            throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "Connection ftp server failed hots:" + configProperties.getHost() + " port:" + configProperties.getPort());
        }
        if (DEFAULT_ENCODING.equalsIgnoreCase(configProperties.getControlEncoding())) {
            if (!FTPReply.isPositiveCompletion(ftpClient.sendCommand(OPTS_UTF8_COMMAND, OPTS_UTF8_COMMAND_ARGS))) {
                throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "ftp server not support encoding utf-8");
            }
        }
        FTPClientConfig config = FtpPooledObjectCreateFactory.getFTPClientConfig(configProperties.isWinFtp(), configProperties.getServerLanguageCode());
        ftpClient.configure(config);
        if (configProperties.isLocalPassiveMode()) {
            //设置被动模式
            ftpClient.enterLocalPassiveMode();
        }
        // 设置模式
        ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
        boolean changeWorkingDirectory = ftpClient.changeWorkingDirectory(configProperties.getWorkDirectory());
        if (!changeWorkingDirectory) {
            throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "FTP work directory:" + configProperties.getWorkDirectory() + "is not exist");
        }
        return ftpClient;
    }

    /**
     * 用PooledObject封装对象放入池中
     */
    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    /**
     * 销毁FtpClient对象
     */
    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws Exception {
        FTPClient ftpClient = p.getObject();
        if (Objects.nonNull(ftpClient) && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 验证FtpClient对象
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        FTPClient ftpClient = p.getObject();
        try {
            return Objects.nonNull(ftpClient) && ftpClient.sendNoOp();
        } catch (IOException e) {
            log.error("Failed to validate client: {}", e);
            return false;
        }
    }

}
