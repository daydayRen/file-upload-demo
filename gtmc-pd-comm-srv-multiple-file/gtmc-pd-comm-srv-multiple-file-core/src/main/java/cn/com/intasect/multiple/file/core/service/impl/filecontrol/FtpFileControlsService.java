package cn.com.intasect.multiple.file.core.service.impl.filecontrol;

import cn.com.intasect.multiple.file.constant.FileSrvCode;
import cn.com.intasect.multiple.file.core.entity.bo.DownloadBo;
import cn.com.intasect.multiple.file.core.entity.bo.DownloadWithByteBo;
import cn.com.intasect.multiple.file.core.entity.bo.UploadBo;
import cn.com.intasect.multiple.file.core.exception.FileSrvException;
import cn.com.intasect.multiple.file.core.service.AbstractFileControlsService;
import cn.com.intasect.multiple.file.core.util.FtpTemplate;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author: renyitian
 * @Date: 2024/3/15 17:00
 * @Description:
 */
@Service
@ConditionalOnProperty(name = "isct.common.fs.switchFileWay", havingValue = "FTP")
public class FtpFileControlsService implements AbstractFileControlsService {
    private static final Logger log = LoggerFactory.getLogger(ObsFileControlsService.class);

    @Autowired
    private FtpTemplate ftpTemplate;

    @Resource
    private HttpServletResponse response;

    @Override
    public Boolean upload(UploadBo uploadBoh) {
        log.info("使用FTP上传文件");
        try {
            ftpTemplate.uploadFile(uploadBoh.getMultipartFile().getInputStream(),uploadBoh.getPath());
        } catch (Exception e) {
            throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "Upload file to file failed,The upload path is " + uploadBoh.getPath());
        }
        return true;
    }

    @Override
    public void download(DownloadBo downloadBo) {

        BufferedInputStream bis  = null;
        InputStream is = null;
        try {
            // 设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("application/x-msdownload");
            // 设置文件头：最后一个参数是设置下载的文件名并编码为UTF-8
            //response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadBo.getFileName(), "UTF-8"));
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadBo.getFileName(), "UTF-8"));
            response.addHeader("Access-Control-Expose-Headers","Content-Disposition");//这样前端才能拿到文件名
            is = ftpTemplate.getFileInputStram(downloadBo.getPath());
            bis  = new BufferedInputStream(is);
            OutputStream out = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = bis.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("下载文件异常{}",e);
            throw new FileSrvException(FileSrvCode.FILE_DOWNLOAD_ERROR,"下载文件异常path:"+downloadBo.getPath());
        }finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public DownloadWithByteBo downloadWithByte(DownloadBo downloadBo) {
        try {
            DownloadWithByteBo downloadWithByteBo = new DownloadWithByteBo();
            InputStream inputStream = ftpTemplate.getFileInputStram(downloadBo.getPath());
            byte[] bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
            downloadWithByteBo.setBytes(bytes);
            downloadWithByteBo.setContentLength((long) bytes.length);
            downloadWithByteBo.setContentDisposition(downloadBo.getFileName());
            return downloadWithByteBo;
        } catch (Exception e) {
            log.error("下载文件异常{}",e);
            throw new FileSrvException(FileSrvCode.FILE_DOWNLOAD_ERROR,"下载文件异常path:"+downloadBo.getPath());
        }

    }

    @Override
    public void uploadBatch(List<UploadBo> uploadBoList) {
        log.info("使用FTP批量上传文件");
        try {
            ftpTemplate.uploadFileBath(uploadBoList);
        } catch (Exception e) {
            log.warn("文件批量上传异常{}",e);
            throw new FileSrvException(FileSrvCode.FILE_UPLOAD_ERROR, "Upload file to file failed");
        }
    }


}
