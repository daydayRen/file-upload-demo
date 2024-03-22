package cn.com.intasect.multiple.file.core.service.impl.filecontrol;

import cn.com.intasect.multiple.file.constant.FileSrvCode;
import cn.com.intasect.multiple.file.core.config.HweiOBSConfig;
import cn.com.intasect.multiple.file.core.entity.bo.DownloadBo;
import cn.com.intasect.multiple.file.core.entity.bo.DownloadWithByteBo;
import cn.com.intasect.multiple.file.core.entity.bo.UploadBo;
import cn.com.intasect.multiple.file.core.exception.FileSrvException;
import cn.com.intasect.multiple.file.core.service.AbstractFileControlsService;
import cn.hutool.core.util.StrUtil;
import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author: renyitian
 * @Date: 2024/3/15 16:08
 * @Description: Obs文件操作
 */
@Service
@ConditionalOnProperty(name = "isct.common.fs.switchFileWay", havingValue = "OBS",matchIfMissing = true)
public class ObsFileControlsService implements AbstractFileControlsService {
    private static final Logger log = LoggerFactory.getLogger(ObsFileControlsService.class);
    @Resource
    HweiOBSConfig hweiOBSConfig;

    @Resource
    private HttpServletResponse response;

    @Override
    public Boolean upload(UploadBo uploadBoh) {
        log.info("使用OBS上传文件");
        // 生成华为obs客户端实例
        ObsClient obsClient = hweiOBSConfig.getInstance();
        try {
            InputStream inputStream = uploadBoh.getMultipartFile().getInputStream();
            obsClient.putObject(uploadBoh.getBucketName(), uploadBoh.getPath(), inputStream);
            inputStream.close();
        } catch (IOException e) {
            log.debug("上传出错！！！！", e);
            throw new FileSrvException(FileSrvCode.FILE_UPLOAD_ERROR, "Upload file to file failed");
        }
        log.debug("上传成功！！！！");
        hweiOBSConfig.destroy(obsClient);//销毁obs客户端实例
        return true;
    }

    @Override
    public void download(DownloadBo downloadBo) {
        log.info("使用OBS下载文件");
        //1.生成华为obs客户端实例
        ObsClient obsClient = hweiOBSConfig.getInstance();
        ObsObject obsObject = obsClient.getObject(downloadBo.getBucketName(), downloadBo.getPath());
        //3.获取response
        hweiOBSConfig.destroy(obsClient);//销毁obs客户端实例
        if (StrUtil.isNotBlank(downloadBo.getFileName())) {
            try {
                obsObject.getMetadata().setContentDisposition("attachment; filename=" + URLEncoder.encode(downloadBo.getFileName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("下载-文件名编码发生错误", e);
            }
        }
        if (obsObject == null) {
            return;
        }

        // 设置response
        response.setContentType(obsObject.getMetadata().getContentType());
        response.setContentLengthLong(obsObject.getMetadata().getContentLength());
        response.addHeader("Content-Disposition", obsObject.getMetadata().getContentDisposition());
        response.addHeader("Access-Control-Expose-Headers","Content-Disposition");//这样前端才能拿到文件名

        try {
            InputStream in = obsObject.getObjectContent();
            OutputStream out = response.getOutputStream();
            IOUtils.copy(in, out);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            log.error("下载文件发生错误", e);
        }
    }

    @Override
    public DownloadWithByteBo downloadWithByte(DownloadBo downloadBo) {
        log.info("使用FeignClient-OBS下载文件");
        //1.生成华为obs客户端实例
        ObsClient obsClient = hweiOBSConfig.getInstance();
        ObsObject obsObject = obsClient.getObject(downloadBo.getBucketName(), downloadBo.getPath());
        //3.获取response
        hweiOBSConfig.destroy(obsClient);//销毁obs客户端实例
        if (StrUtil.isNotBlank(downloadBo.getFileName())) {
            try {
                obsObject.getMetadata().setContentDisposition("attachment; filename=" + URLEncoder.encode(downloadBo.getFileName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("下载-文件名编码发生错误", e);
            }
        }
        if (obsObject == null) {
            return null;
        }
        DownloadWithByteBo downloadWithByteBo = new DownloadWithByteBo();
        try {
            InputStream in = obsObject.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(in);
            in.close();
            downloadWithByteBo.setBytes(bytes);
            downloadWithByteBo.setContentDisposition(obsObject.getMetadata().getContentDisposition());
            downloadWithByteBo.setContentLength(obsObject.getMetadata().getContentLength());
            return downloadWithByteBo;
        } catch (Exception e) {
            log.error("下载文件发生错误", e);
        }
        return null;
    }

    @Override
    public void uploadBatch(List<UploadBo> uploadBoList) {
        log.info("使用OBS批量上传文件");
        // 生成华为obs客户端实例
        ObsClient obsClient = hweiOBSConfig.getInstance();
        try {
            for (UploadBo uploadBo:uploadBoList){
                InputStream inputStream = uploadBo.getMultipartFile().getInputStream();
                obsClient.putObject(uploadBo.getBucketName(), uploadBo.getPath(), inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            log.debug("上传出错！！！！{}", e);
            throw new FileSrvException(FileSrvCode.FILE_UPLOAD_ERROR, "Upload file to file failed");
        }
        log.debug("上传成功！！！！");
        hweiOBSConfig.destroy(obsClient);//销毁obs客户端实例
    }
}