package cn.com.intasect.multiple.file.core.controller;

import cn.com.intasect.core.data.util.BeanConvertUtils;
import cn.com.intasect.core.domain.response.R;
import cn.com.intasect.multiple.file.api.FileApi;
import cn.com.intasect.multiple.file.bo.DownloadCondition;
import cn.com.intasect.multiple.file.bo.FileDto;
import cn.com.intasect.multiple.file.core.entity.FileConfig;
import cn.com.intasect.multiple.file.core.entity.FileEntity;
import cn.com.intasect.multiple.file.core.entity.bo.DownloadWithByteBo;
import cn.com.intasect.multiple.file.core.service.CommFileService;
import cn.com.intasect.multiple.file.vo.FileConfigVo;
import cn.com.intasect.multiple.file.vo.FileUploadBatchVO;
import cn.com.intasect.multiple.file.vo.FileUploadVO;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class FileApiController implements FileApi {

    @Resource
    private CommFileService commFileService;

    @Resource
    private HttpServletResponse response;

    @Override
    public R<FileDto> upload(FileUploadVO fileUploadVO) {
        FileEntity fileDto = commFileService.upload(fileUploadVO);
        return R.build(BeanConvertUtils.convertTo(fileDto, FileDto::new));
    }

    @Override
    public R<List<FileDto>> uploadBatch(FileUploadBatchVO fileUploadVO) {
        List<FileEntity> fileDtos = commFileService.uploadBatch(fileUploadVO);
        if(CollectionUtils.isEmpty(fileDtos)){
            return R.build();
        }
        List<FileDto> collect = fileDtos.stream().map(item -> {
            FileDto fileDto = new FileDto();
            BeanUtil.copyProperties(item, fileDto);
            return fileDto;
        }).collect(Collectors.toList());
        return R.build(collect);
    }

    @Override
    public void download(@Valid @NotNull(message = "参数不能为空") DownloadCondition downloadCondition) {
        commFileService.download(downloadCondition);
    }

    @Override
    public ResponseEntity<byte[]> downloadInService(DownloadCondition downloadCondition) {
        DownloadWithByteBo downloadWithByteBo = commFileService.downloadWithByte(downloadCondition);
        if (downloadWithByteBo == null || downloadWithByteBo.getBytes() == null) {
            return null;
        }

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            //声明返回的类型 为一个stream流
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            //设置文件名称
            httpHeaders.set("Content-Disposition", downloadWithByteBo.getContentDisposition());
            //大小
            httpHeaders.setContentLength(downloadWithByteBo.getContentLength());
            return new ResponseEntity<>(downloadWithByteBo.getBytes(), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            log.error("下载文件发生错误", e);
        }
        return null;
    }

    @Override
    public R<FileDto> detail(@PathVariable("fileId") Long fileId) {
        FileEntity fileDto = commFileService.detail(fileId);
        return R.build(BeanConvertUtils.convertTo(fileDto, FileDto::new));
    }

    @Override
    public void delete(@PathVariable("fileId") Long fileId) {
        commFileService.delete(fileId);
    }

    @Override
    public R<List<FileConfigVo>> getFileConfigList() {
        List<FileConfig> fileConfigs = commFileService.getFileConfigList();
        if(CollectionUtils.isEmpty(fileConfigs)){
            return R.build();
        }
        List<FileConfigVo> collect = fileConfigs.stream().map(item -> {
            FileConfigVo fileConfigVo = new FileConfigVo();
            BeanUtil.copyProperties(item, fileConfigVo);
            fileConfigVo.setId(item.getId());
            return fileConfigVo;
        }).collect(Collectors.toList());
        return R.build(collect);
    }

    @Override
    public void saveFileConfig(FileConfigVo fileConfigVo) {
        FileConfig fileConfig =  new FileConfig();
        BeanUtil.copyProperties(fileConfigVo,fileConfig);
        commFileService.saveFileConfig(fileConfig);
    }

    @Override
    public void deleteFileConfig(String configId) {
        commFileService.deleteFileConfig(configId);
    }

}
