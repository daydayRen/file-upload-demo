package cn.com.intasect.multiple.file.api;


import cn.com.intasect.core.domain.response.R;
import cn.com.intasect.multiple.file.bo.DownloadCondition;
import cn.com.intasect.multiple.file.vo.FileConfigVo;
import cn.com.intasect.multiple.file.bo.FileDto;
import cn.com.intasect.multiple.file.vo.FileUploadBatchVO;
import cn.com.intasect.multiple.file.vo.FileUploadVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
  * @Author: renyitian
  * @Description: 外部调用文件服务API
  * @Date: 15:19 2024/3/15
  */
@Validated
@Api("文件服务API")
@FeignClient(name = "${isct.common.fs.service-name:comm-multiple-file-service}", contextId = "multipleFileServiceApi")
@RequestMapping("/api/common/multiple/file")
public interface FileApi {

    /**
      * @Author: renyitian
      * @Description: 
      * @Date: 12:08 2024/3/15
      * @Param: 
      * @Return: 
      */
    @RequestMapping(value = "upload", consumes = "multipart/form-data", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传", notes = "文件上传", httpMethod = "POST")
    R<FileDto> upload(FileUploadVO fileUploadVO);

    /**
     * @Author: renyitian
     * @Description: 批量上传
     * @Date: 12:08 2024/3/15
     * @Param:
     * @Return:
     */
    @RequestMapping(value = "uploadBatch", consumes = "multipart/form-data", method = RequestMethod.POST)
    @ApiOperation(value = "批量文件上传", notes = "批量文件上传", httpMethod = "POST")
    R<List<FileDto>> uploadBatch(FileUploadBatchVO fileUploadVO);

    /**
      * @Author: renyitian
      * @Description: 文件下载
      * @Date: 15:15 2024/3/15
      * @Param:
      * @Return:
      */
    @RequestMapping(value = "download", method = RequestMethod.POST)
    @ApiOperation(value = "文件下载", notes = "文件下载", httpMethod = "POST")
    void download(@Valid @NotNull(message = "参数不能为空") @RequestBody DownloadCondition downloadCondition);

    /**
      * @Author: renyitian
      * @Description: 微服务Feign调用
      * @Date: 15:15 2024/3/15
      * @Param:
      * @Return:
      */
    @RequestMapping(value = "downloadInService", method = RequestMethod.POST)
    @ApiOperation(value = "微服务Feign调用", notes = "微服务Feign调用", httpMethod = "POST")
    ResponseEntity<byte[]> downloadInService(@Valid @NotNull(message = "参数不能为空") @RequestBody DownloadCondition downloadCondition);

    /**
      * @Author: renyitian
      * @Description: 文件详情
      * @Date: 15:17 2024/3/15
      * @Param:
      * @Return:
      */
    @RequestMapping(value = "detail/{fileId}", method = RequestMethod.GET)
    @ApiOperation(value = "文件详情", notes = "文件详情", httpMethod = "GET")
    R<FileDto> detail(@PathVariable("fileId") Long fileId);

    /**
      * @Author: renyitian
      * @Description: 文件删除
      * @Date: 15:17 2024/3/15
      * @Param:
      * @Return:
      */
    @RequestMapping(value = "delete/{fileId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "文件删除", notes = "文件删除", httpMethod = "DELETE")
    void delete(@PathVariable("fileId") Long fileId);

    /**
      * @Author: renyitian
      * @Description: 文件配置列表
      * @Date: 14:16 2024/3/21
      * @Param:  * 参数
      */
    @RequestMapping(value = "getFileConfigList", method = RequestMethod.GET)
    @ApiOperation(value = "文件配置列表", notes = "文件配置列表", httpMethod = "GET")
    R<List<FileConfigVo>> getFileConfigList();

    /**
     * @Author: renyitian
     * @Description: 文件配置
     * @Date: 14:16 2024/3/21
     * @Param:  * 参数
     */
    @RequestMapping(value = "saveFileConfig", method = RequestMethod.POST)
    @ApiOperation(value = "文件配置", notes = "文件配置", httpMethod = "POST")
    void saveFileConfig(@Valid @NotNull(message = "参数不能为空") @RequestBody FileConfigVo fileConfigVo);

    /**
     * @Author: renyitian
     * @Description: 文件删除
     * @Date: 15:17 2024/3/15
     * @Param:
     * @Return:
     */
    @RequestMapping(value = "deleteFileConfig/{configId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "文件配置删除", notes = "文件配置删除", httpMethod = "DELETE")
    void deleteFileConfig(@PathVariable("configId") String fileId);

}
