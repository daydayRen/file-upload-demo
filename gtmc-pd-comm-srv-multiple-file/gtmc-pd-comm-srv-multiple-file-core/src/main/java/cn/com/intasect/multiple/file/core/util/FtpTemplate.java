package cn.com.intasect.multiple.file.core.util;

import cn.com.intasect.multiple.file.constant.FileSrvCode;
import cn.com.intasect.multiple.file.core.entity.bo.UploadBo;
import cn.com.intasect.multiple.file.core.exception.FileSrvException;
import cn.com.intasect.multiple.file.core.pool.FtpGenericObjectPool;
import cn.com.intasect.multiple.file.core.properties.FtpConfigProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * @author YC
 */
public class FtpTemplate {

    private static final String PATH_SEPARATE = "/";

    private FtpGenericObjectPool ftpGenericObjectPool;

    private final String workDirectory;

    public FtpTemplate(FtpGenericObjectPool ftpGenericObjectPool, FtpConfigProperties configProperties) {
        this.ftpGenericObjectPool = ftpGenericObjectPool;
        this.workDirectory = configProperties.getWorkDirectory();
    }

    /**
     * 上传文件
     *
     * @param fileStream 文件流
     * @param path       文件路径（当路径在ftp服务器上不存时将会创建）
     * @return 当前成功返回true 失败返回true
     * @throws Exception ex
     */
    public void uploadFile(InputStream fileStream, String path) throws Exception {
        FTPClient ftpClient = this.ftpGenericObjectPool.borrowObject();
        try {
            path = this.pathNormalize(path);
            path = this.workDirectory + path;

            //创建ftp文件路径
            String remoteDir = StringUtils.substringBeforeLast(path, "/");
            if (!ftpClient.changeWorkingDirectory(remoteDir)) {
                String[] dirs = remoteDir.split("/");
                StringBuffer sb = new StringBuffer();
                for (String dir : dirs) {
                    if (dir.equals("")) {
                        continue;
                    }
                    sb.append("/").append(dir);
                    if (!ftpClient.changeWorkingDirectory(sb.toString())) {
                        if (!ftpClient.makeDirectory(sb.toString())) {
                            throw new IOException("Could not create directory: " + sb.toString());
                        }
                        ftpClient.changeWorkingDirectory(sb.toString());
                    }
                }
            }

            String fileName = StringUtils.substringAfterLast(path, "/");
            fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");

            boolean storeFile = ftpClient.storeFile(remoteDir + "/" + fileName, fileStream);
            if (!storeFile) {
                throw new FileSrvException(FileSrvCode.FILE_UPLOAD_ERROR, "Upload file to file failed,The upload path is " + path);
            }
        } finally {
            this.returnObject(ftpClient);
        }
    }


    /**
      * @Author: renyitian
      * @Description:  批量上传
      * @Date: 14:00 2024/3/21
      * @Param:  * 参数 uploadBoList
      */
    public void uploadFileBath(List<UploadBo> uploadBoList) throws Exception {
        FTPClient ftpClient = this.ftpGenericObjectPool.borrowObject();
        String path = uploadBoList.get(0).getPath();
        try {
            path = this.pathNormalize(path);
            path = this.workDirectory + path;

            //创建ftp文件路径
            String remoteDir = StringUtils.substringBeforeLast(path, "/");
            if (!ftpClient.changeWorkingDirectory(remoteDir)) {
                String[] dirs = remoteDir.split("/");
                StringBuffer sb = new StringBuffer();
                for (String dir : dirs) {
                    if (dir.equals("")) {
                        continue;
                    }
                    sb.append("/").append(dir);
                    if (!ftpClient.changeWorkingDirectory(sb.toString())) {
                        if (!ftpClient.makeDirectory(sb.toString())) {
                            throw new IOException("Could not create directory: " + sb.toString());
                        }
                        ftpClient.changeWorkingDirectory(sb.toString());
                    }
                }
            }
            //批量上传
            for (UploadBo uploadBo:uploadBoList){
                String fileName = StringUtils.substringAfterLast(uploadBo.getPath(), "/");
                fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");

                boolean storeFile = ftpClient.storeFile(remoteDir + "/" + fileName, uploadBo.getMultipartFile().getInputStream());
                if (!storeFile) {
                    throw new FileSrvException(FileSrvCode.FILE_UPLOAD_ERROR, "Upload file to file failed,The upload path is " + path);
                }
            }
        } finally {
            this.returnObject(ftpClient);
        }
    }

    /**
     * 下载文件
     *
     * @param path 文件路径
     * @return 文件流
     * @throws Exception ex
     */
    public InputStream downloadFile(String path) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.downloadFileFromPath(path, outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * 从指定路径下载文件
     *
     * @param path         文件路径（包含文件名）
     * @param outputStream 文件输出流
     * @return 文件下载成功到输出流返回true 失败返回false
     * @throws Exception ex
     */
    public void downloadFileFromPath(String path, OutputStream outputStream) throws Exception {
        FTPClient ftpClient = null;
        try {
            if (StringUtils.isBlank(path)) {
                throw new FileSrvException(FileSrvCode.FILE_DOWNLOAD_ERROR, "File path cant not blank");
            }
            path = this.pathNormalize(path);
            path = this.workDirectory + path;
            ftpClient = this.ftpGenericObjectPool.borrowObject();
            if (!ftpClient.retrieveFile(path, outputStream)) {
                throw new FileSrvException(FileSrvCode.FILE_DOWNLOAD_ERROR, "Download file to outputStream failed with ftp path '" + path + "'");
            }
        } finally {
            this.returnObject(ftpClient);
        }
    }

    /**
     * @Author: renyitian
     * @Description: 获取文件流
     * @Date: 16:29 2024/3/18
     * @Param: * 参数 path
     */
    public InputStream getFileInputStram(String path) throws Exception {
        FTPClient ftpClient = null;
        // 初始化FTP客户端
        ftpClient = this.ftpGenericObjectPool.borrowObject();
        ftpClient.enterLocalPassiveMode(); // 进入被动模式
        // 获取文件输入流
        InputStream inputStream = ftpClient.retrieveFileStream(path);
        // 检查是否成功获取到了文件流
        boolean success = ftpClient.completePendingCommand();
        if (!success) {
            throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "Failed to retrieve file from FTP server.");
        }

        return inputStream;
    }

    public long getSizeFile(String path) throws Exception {
        FTPClient ftpClient = null;
        // 初始化FTP客户端
        ftpClient = this.ftpGenericObjectPool.borrowObject();
        ftpClient.enterLocalPassiveMode(); // 进入被动模式
        FTPFile[] files = ftpClient.listFiles(path);
        if (files.length > 0) {
            FTPFile file = files[0];
            long fileSize = file.getSize();
            System.out.println("文件大小：" + fileSize + " bytes");
            return fileSize;
        }
        return 0L;
    }

    /**
     * 删除文件
     *
     * @param path 文件路径（包含文件名）
     * @return 成功返回true, 失败返回false
     * @throws Exception
     */
    public boolean deleteFile(String path) throws Exception {
        FTPClient ftpClient = null;
        try {
            if (StringUtils.isBlank(path)) {
                throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "Ftp path can not blank");
            }
            path = this.pathNormalize(path);
            path = this.workDirectory + path;
            ftpClient = this.ftpGenericObjectPool.borrowObject();
            return ftpClient.deleteFile(path);
        } finally {
            this.returnObject(ftpClient);
        }
    }

    /**
     * 删除文件目录
     *
     * @param path path
     * @return
     * @throws Exception
     */
    public boolean deleteFileDirectory(String path) throws Exception {
        FTPClient ftpClient = null;
        try {
            if (StringUtils.isBlank(path)) {
                throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "Ftp path can not blank");
            }
            path = this.pathNormalize(path);
            path = this.workDirectory + path;
            ftpClient = this.ftpGenericObjectPool.borrowObject();
            return ftpClient.removeDirectory(path);
        } finally {
            this.returnObject(ftpClient);
        }
    }

    /**
     * 文件是否存在
     *
     * @param path 路径
     * @return 存在返回true 否则返回false
     * @throws Exception ex
     */
    public boolean fileIsExist(String path) throws Exception {
        FTPClient ftpClient = null;
        try {
            path = this.pathNormalize(path);
            path = this.workDirectory + path;
            ftpClient = this.ftpGenericObjectPool.borrowObject();
            FTPFile[] ftpFiles = ftpClient.listFiles(path);
            if (Objects.isNull(ftpFiles) || ftpFiles.length == 0) {
                ftpFiles = ftpClient.listFiles(path.substring(0, path.lastIndexOf(PATH_SEPARATE)));
                String pathLastLevel = path.substring(path.lastIndexOf(PATH_SEPARATE) + 1);
                return Objects.nonNull(ftpFiles)
                        &&
                        ftpFiles.length > 0
                        &&
                        Arrays.stream(ftpFiles).map(FTPFile::getName).anyMatch(n -> n.equals(pathLastLevel));
            }
            return true;
        } finally {
            this.returnObject(ftpClient);
        }
    }

    /**
     * 获取文件列表
     *
     * @return
     * @throws Exception ex
     */
    public FTPFile[] fileList() throws Exception {
        FTPClient ftpClient = null;
        try {
            ftpClient = this.ftpGenericObjectPool.borrowObject();
            return ftpClient.listFiles();
        } finally {
            this.returnObject(ftpClient);
        }
    }

    /**
     * 获取指定路径的文件列表
     *
     * @param path 路径
     * @return
     * @throws Exception e
     */
    public FTPFile[] fileList(String path) throws Exception {
        FTPClient ftpClient = null;
        try {
            path = this.pathNormalize(path);
            path = this.workDirectory + path;
            ftpClient = this.ftpGenericObjectPool.borrowObject();
            return ftpClient.listFiles(path);
        } finally {
            this.returnObject(ftpClient);
        }
    }

    /**
     * 获取指定路径的文件列表
     *
     * @param path       路径
     * @param fileFilter 文件过滤器
     * @return
     * @throws Exception ex
     */
    public FTPFile[] fileList(String path, FTPFileFilter fileFilter) throws Exception {
        FTPClient ftpClient = null;
        try {
            path = this.pathNormalize(path);
            path = this.workDirectory + path;
            ftpClient = this.ftpGenericObjectPool.borrowObject();
            return ftpClient.listFiles(path, fileFilter);
        } finally {
            this.returnObject(ftpClient);
        }
    }

    /**
     * 创建目录
     *
     * @param path 路径
     * @return
     * @throws Exception
     */
    public boolean createDirectory(String path) throws Exception {
        FTPClient ftpClient = null;
        try {
            boolean result = false;
            if (StringUtils.isNotBlank(path)) {
                ftpClient = this.ftpGenericObjectPool.borrowObject();
                StringTokenizer token = new StringTokenizer(path, "\\/");
                while (token.hasMoreTokens()) {
                    String currentPath = token.nextToken();
                    ftpClient.makeDirectory(currentPath);
                    result = ftpClient.changeWorkingDirectory(currentPath);
                    if (!result) {
                        throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "Create ftp path failed current path '" + currentPath + "', create path is '" + path + "'");
                    }
                }
                ftpClient.changeWorkingDirectory(this.workDirectory);
            }
            return result;
        } finally {
            this.returnObject(ftpClient);
        }
    }

    public boolean switchWorkDirectory(FTPClient ftpClient, String path, boolean forcedIncrease) throws Exception {
        if (StringUtils.isNotBlank(path)) {
            boolean result = ftpClient.changeWorkingDirectory(path);
            String currentWorkDirectory;
            if (result) {
                currentWorkDirectory = path;
            } else if (forcedIncrease) {
                StringTokenizer token = new StringTokenizer(path, "\\/");
                while (token.hasMoreTokens()) {
                    String currentPath = token.nextToken();
                    ftpClient.makeDirectory(currentPath);
                    result = ftpClient.changeWorkingDirectory(currentPath);
                    if (result) {
                        currentWorkDirectory = currentPath;
                    } else {
                        break;
                    }
                }
            }
            return result;
        }
        return false;
    }

    private void returnObject(FTPClient client) throws Exception {
        if (Objects.nonNull(client)) {
            this.ftpGenericObjectPool.returnObject(client);
        }
    }

    private String pathNormalize(String path) {
        StringTokenizer token = new StringTokenizer(path, "\\/");
        String[] builder = new String[token.countTokens()];
        int index = 0;
        while (token.hasMoreTokens()) {
            builder[index] = token.nextToken();
            index++;
        }
        return StringUtils.join(builder, "/");
    }

}
