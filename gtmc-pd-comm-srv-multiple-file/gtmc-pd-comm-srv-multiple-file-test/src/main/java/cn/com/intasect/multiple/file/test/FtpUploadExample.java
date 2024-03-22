package cn.com.intasect.multiple.file.test;

/**
 * @Author: renyitian
 * @Date: 2024/3/19 15:18
 * @Description:
 */

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FtpUploadExample {

    public static void main(String[] args) throws IOException {
        FTPClient ftpClient = new FTPClient();

        try {
            // 连接FTP服务器
            ftpClient.connect("192.168.139.116", 21);  // 替换为你的FTP服务器地址和端口

            // 登录
            boolean login = ftpClient.login("root", "root");  // 替换为你的FTP用户名和密码
            if (!login) {
                throw new RuntimeException("FTP登录失败");
            }

            // 设置FTP传输模式
            ftpClient.enterLocalPassiveMode(); // 使用被动模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 设置文件类型为二进制

            // 进入被动模式，这对于许多服务器来说是必需的，以允许数据连接
            ftpClient.enterLocalPassiveMode();
            String localFilePath = "D:\\音泰思\\项目\\2、共通项目对齐\\项目规划.sql";
            String remoteFilePath = "2222.sql";
            // 打开本地文件的输入流
            FileInputStream fis = new FileInputStream(localFilePath);
            // 使用 FTPClient 将本地文件上传到远程服务器
            boolean uploadSuccess = ftpClient.storeFile(remoteFilePath, fis);






            if (uploadSuccess) {
                System.out.println("文件已成功上传到 FTP 服务器");
            } else {
                System.out.println("文件上传到 FTP 服务器失败");
            }
            // 这里可以添加你需要执行的FTP操作，例如：上传、下载、删除等

            System.out.println("FTP连接成功");

        } catch (Exception e) {
            System.err.println("FTP连接异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 断开连接
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }
}