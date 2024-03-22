package cn.com.intasect.multiple.file.core.pool;

import cn.com.intasect.multiple.file.core.properties.FtpConfigProperties;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;

/**
  * @Author: renyitian
  * @Description:
  * @Date: 11:35 2024/3/18
  * @Param:  * 参数 null
  */
public final class FtpPooledObjectCreateFactory {


    public static FTPClient createConnection(FtpConfigProperties configProperties) throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setDefaultTimeout(configProperties.getDefaultTimeout());
        ftpClient.setConnectTimeout(configProperties.getConnectTimeout());
        ftpClient.setDataTimeout(configProperties.getDataTimeout());
        ftpClient.setControlEncoding(configProperties.getControlEncoding());
        ftpClient.connect(configProperties.getHost(), configProperties.getPort());
        ftpClient.login(configProperties.getUsername(), configProperties.getPassword());
        ftpClient.setFileType(configProperties.getFileType());
        return ftpClient;
    }

    public static FTPClientConfig getFTPClientConfig(boolean isWinFtp, String serverLanguageCode) {
        String systemKey;
        if (isWinFtp) {
            systemKey = FTPClientConfig.SYST_NT;
        } else {
            systemKey = FTPClientConfig.SYST_UNIX;
        }
        FTPClientConfig conf = new FTPClientConfig(systemKey);
        conf.setServerLanguageCode(serverLanguageCode);
        return conf;
    }


}
