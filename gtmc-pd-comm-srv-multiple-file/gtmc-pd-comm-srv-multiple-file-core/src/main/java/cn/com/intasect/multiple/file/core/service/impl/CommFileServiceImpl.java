package cn.com.intasect.multiple.file.core.service.impl;

import cn.com.intasect.core.domain.response.R;
import cn.com.intasect.multiple.file.bo.DownloadCondition;
import cn.com.intasect.multiple.file.constant.FSConstants;
import cn.com.intasect.multiple.file.constant.FileSrvCode;
import cn.com.intasect.multiple.file.core.config.HweiOBSConfig;
import cn.com.intasect.multiple.file.core.entity.FileConfig;
import cn.com.intasect.multiple.file.core.entity.FileDownloadRecord;
import cn.com.intasect.multiple.file.core.entity.FileEntity;
import cn.com.intasect.multiple.file.core.entity.bo.DownloadBo;
import cn.com.intasect.multiple.file.core.entity.bo.DownloadWithByteBo;
import cn.com.intasect.multiple.file.core.entity.bo.UploadBo;
import cn.com.intasect.multiple.file.core.exception.FileSrvException;
import cn.com.intasect.multiple.file.core.mapper.FileConfigMapper;
import cn.com.intasect.multiple.file.core.mapper.FileDownloadRecordMapper;
import cn.com.intasect.multiple.file.core.mapper.FileMapper;
import cn.com.intasect.multiple.file.core.properties.FileServiceProperties;
import cn.com.intasect.multiple.file.core.service.AbstractFileControlsService;
import cn.com.intasect.multiple.file.core.service.CommFileService;
import cn.com.intasect.multiple.file.core.service.FileConfigService;
import cn.com.intasect.multiple.file.core.util.MD5FileUtil;
import cn.com.intasect.multiple.file.vo.FileUploadBatchVO;
import cn.com.intasect.multiple.file.vo.FileUploadVO;
import cn.com.intasect.parameter.api.DictionaryApi;
import cn.com.intasect.parameter.bo.DictQueryCondition;
import cn.com.intasect.parameter.bo.DictionaryBO;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@DS(FSConstants.DS_NAME_FS)
@Service
public class CommFileServiceImpl implements CommFileService {

    private static final Logger log = LoggerFactory.getLogger(CommFileServiceImpl.class);

    @Resource
    FileMapper fileMapper;

    @Resource
    FileConfigMapper fileConfigMapper;

    @Resource
    FileDownloadRecordMapper fileDownloadRecordMapper;

    @Resource
    FileConfigService fileConfigService;

    @Resource
    HweiOBSConfig hweiOBSConfig;

    @Resource
    DictionaryApi dictionaryApi;

    @Resource
    private AbstractFileControlsService abstractFileControlsService;

    @Resource
    private FileServiceProperties fileServiceProperties;

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public FileEntity upload(FileUploadVO fileUploadVO) {
        // 1.根据文件业务code查询对应存储介质、桶名称、桶路径、存储目录
        FileConfig fileConfigParam = new FileConfig();
        fileConfigParam.setDomainCode(fileUploadVO.getDomainCode());
        fileConfigParam.setSubdomainCode(fileUploadVO.getSubdomainCode());
        fileConfigParam.setBusinessCode(fileUploadVO.getBusinessCode());

        FileConfig fileConfig = fileConfigService.findFileConfigByBizCode(fileConfigParam);
        if (fileConfig == null) {
            throw new FileSrvException(FileSrvCode.FILE_CONFIG_NOT_FOUND);
        }

        if("OBS".equals(fileServiceProperties.getSwitchFileWay())){
            if (fileConfig.getStorageService() == null || StrUtil.hasBlank(fileConfig.getBucketName(), fileConfig.getFileDirectory())) {
                throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "OBS文件配置错误：" + JSONUtil.toJsonStr(fileConfig));
            }
        } else if ("FTP".equals(fileServiceProperties.getSwitchFileWay())) {
            if (fileConfig.getStorageService() == null || StrUtil.hasBlank(fileConfig.getFileDirectory())) {
                throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "FTP文件配置错误：" + JSONUtil.toJsonStr(fileConfig));
            }
        }


        // 2.获取文件信息
        log.debug("进入文件组件方法upload....");
        MultipartFile multipartFile = fileUploadVO.getFile();
        String fileName = multipartFile.getOriginalFilename();//文件名
        long fileSize = multipartFile.getSize();//文件大小
        String fileContentType = multipartFile.getContentType();//文件mime类型
        //文件后缀
        String fileSuffix = FileUtil.extName(fileName);
        log.debug("进入文件组件方法upload....文件名：" + fileName + ";大小：" + fileSize + ";文件类型：" + fileContentType + ";文件后缀：" + fileSuffix);

        // 查询字典配置，限制文件类型，未配置不限制
        DictQueryCondition dictQueryCondition = new DictQueryCondition();
        dictQueryCondition.setDomain(FSConstants.FILE_LIMIT_DOMAIN);
        dictQueryCondition.setDictTypeCode(FSConstants.FILE_LIMIT_DICT_TYPE_CODE);
        dictQueryCondition.setDictCode(FSConstants.FILE_LIMIT_DICT_CODE);
        R<DictionaryBO> dictResult = null;
        try {
            dictResult = this.dictionaryApi.getOne(dictQueryCondition);
        } catch (Exception e) {
            log.warn("限制文件类型，字典配置查询发生异常", e);
        }

        if (dictResult != null && dictResult.isSuccess()) {
            DictionaryBO dict = dictResult.getBody();
            if (dict != null && StrUtil.isNotBlank(dict.getDictValue())) {
                if (!Arrays.asList(dict.getDictValue().split(",")).contains(fileSuffix)) {
                    throw new FileSrvException(FileSrvCode.FILE_LIMITED,
                            StrUtil.format("限制上传，可上传文件类型[{}]", dict.getDictValue()));
                }
            } else {
                log.warn("限制文件类型的字典配置为空");
            }
        } else {
            log.warn("限制文件类型，字典配置查询失败：{}", dictResult != null ? dictResult.getHeader().getMessage() : "");
        }

        // 3.先存储一次,获取文件id
        FileEntity fileDto = new FileEntity();
        fileDto.setFileName(fileName);
        fileDto.setFileType(fileContentType);
        fileDto.setFileSuffix(fileSuffix);
        fileDto.setFileSize(fileSize);
        fileDto.setFileMd5(MD5FileUtil.getMd5OfFile(multipartFile));
        fileDto.setDelFlag(0);
        fileMapper.insert(fileDto);

        //设置对象存储路径
        String fileDirectory = fileConfig.getFileDirectory();
        fileDirectory = fileDirectory.endsWith("/") ? fileDirectory : fileDirectory + "/";
        if (StrUtil.isNotBlank(fileUploadVO.getBusinessId())) {
            fileDirectory = fileDirectory + fileUploadVO.getBusinessId() + "/";
        }

        //对象存储路径
        String storageName = fileDto.getId() + "." + fileDto.getFileSuffix();
        String objectKey = fileDirectory + storageName;
        String bucketName = fileConfig.getBucketName();
        log.debug("bucketName:objectKey={}:{}", bucketName, objectKey);


        UploadBo uploadBo = new UploadBo()
                .setMultipartFile(multipartFile)
                .setBucketName(bucketName)
                .setPath(objectKey);
        abstractFileControlsService.upload(uploadBo);


        //更新
        fileDto.setStorageName(storageName);
        fileDto.setDomainCode(fileConfig.getDomainCode());
        fileDto.setSubdomainCode(fileConfig.getSubdomainCode());
        fileDto.setStorageService(fileConfig.getStorageService());
        fileDto.setBusinessCode(fileConfig.getBusinessCode());
        fileDto.setBucketName(fileConfig.getBucketName());
        fileDto.setBucketDomain(fileConfig.getBucketDomain());
        fileDto.setFileDirectory(fileConfig.getFileDirectory());
        if (StrUtil.isNotBlank(fileUploadVO.getBusinessId())) {
            fileDto.setFileLeafDirectory(fileUploadVO.getBusinessId());
        }
        if (StrUtil.isNotBlank(fileUploadVO.getBusinessRemark())) {
            fileDto.setBusinessRemark(fileUploadVO.getBusinessRemark());
        }
        //将文件信息写入文件库中文件信息表t_file
        fileMapper.updateById(fileDto);
        return fileDto;
    }

    @Override
    public void download(DownloadCondition downloadCondition) {

        //1.生成华为obs客户端实例
        //ObsClient obsClient = hweiOBSConfig.getInstance();
        //2.根据文件id获取文件信息
        FileEntity fileDto = fileMapper.findFileById(downloadCondition.getFileId());
        if (fileDto == null) {
            throw new FileSrvException(FileSrvCode.FILE_NOT_FOUND);
        } else {
            //3.获取文件地址，从obs服务器获取文件
            String fileDirectory = fileDto.getFileDirectory();
            fileDirectory = fileDirectory.endsWith("/") ? fileDirectory : fileDirectory + "/";
            if (StrUtil.isNotBlank(fileDto.getFileLeafDirectory())) {
                fileDirectory = fileDirectory + fileDto.getFileLeafDirectory() + "/";
            }
            String objectKey = fileDirectory + fileDto.getStorageName();
            String bucketName = fileDto.getBucketName();
            log.debug("bucketName:objectKey={}:{}", bucketName, objectKey);

            //ObsObject obsObject = obsClient.getObject(bucketName, objectKey);
            //2.记录下载履历
            FileDownloadRecord fileDownloadRecord = new FileDownloadRecord();
            fileDownloadRecord.setFileId(fileDto.getId());
            fileDownloadRecord.setDownloadUserName(downloadCondition.getDownloadUserName());
            fileDownloadRecord.setDownloadUserId(downloadCondition.getDownloadUserId());
            fileDownloadRecord.setDownloadTime(new Date());
            fileDownloadRecord.setDelFlag(0);
            fileDownloadRecordMapper.insert(fileDownloadRecord);
            DownloadBo downloadBo = new DownloadBo()
                    .setFileName(fileDto.getFileName())
                    .setPath(objectKey)
                    .setBucketName(bucketName);
            abstractFileControlsService.download(downloadBo);

            //3.获取response
            /*hweiOBSConfig.destroy(obsClient);//销毁obs客户端实例
            if (StrUtil.isNotBlank(fileDto.getFileName())) {
                try {
                    obsObject.getMetadata().setContentDisposition("attachment; filename=" + URLEncoder.encode(fileDto.getFileName(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error("下载-文件名编码发生错误", e);
                }
            }
            return obsObject;*/
        }

    }

    @Override
    public DownloadWithByteBo downloadWithByte(DownloadCondition downloadCondition) {
        log.info("feign远程调用");
        FileEntity fileDto = fileMapper.findFileById(downloadCondition.getFileId());
        if (fileDto == null) {
            throw new FileSrvException(FileSrvCode.FILE_NOT_FOUND);
        } else {
            //3.获取文件地址，从obs服务器获取文件
            String fileDirectory = fileDto.getFileDirectory();
            fileDirectory = fileDirectory.endsWith("/") ? fileDirectory : fileDirectory + "/";
            if (StrUtil.isNotBlank(fileDto.getFileLeafDirectory())) {
                fileDirectory = fileDirectory + fileDto.getFileLeafDirectory() + "/";
            }
            String objectKey = fileDirectory + fileDto.getStorageName();
            String bucketName = fileDto.getBucketName();
            log.debug("bucketName:objectKey={}:{}", bucketName, objectKey);

            //ObsObject obsObject = obsClient.getObject(bucketName, objectKey);
            //2.记录下载履历
            FileDownloadRecord fileDownloadRecord = new FileDownloadRecord();
            fileDownloadRecord.setFileId(fileDto.getId());
            fileDownloadRecord.setDownloadUserName(downloadCondition.getDownloadUserName());
            fileDownloadRecord.setDownloadUserId(downloadCondition.getDownloadUserId());
            fileDownloadRecord.setDownloadTime(new Date());
            fileDownloadRecord.setDelFlag(0);
            fileDownloadRecordMapper.insert(fileDownloadRecord);
            DownloadBo downloadBo = new DownloadBo()
                    .setFileName(fileDto.getFileName())
                    .setPath(objectKey)
                    .setBucketName(bucketName);
            return abstractFileControlsService.downloadWithByte(downloadBo);
        }
    }

    @Override
    public FileEntity detail(Long fileId) {
        FileEntity fileDto = fileMapper.findFileById(fileId);
        if (fileDto == null) {
            throw new FileSrvException(FileSrvCode.FILE_NOT_FOUND);
        } else {
            return fileDto;
        }
    }

    @Override
    public void delete(Long fileId) {
        FileEntity fileDto = new FileEntity();
        fileDto.setId(fileId);
        fileMapper.updateDelFlagById(fileDto);
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public List<FileEntity> uploadBatch(FileUploadBatchVO fileUploadVO) {
        // 1.根据文件业务code查询对应存储介质、桶名称、桶路径、存储目录
        FileConfig fileConfigParam = new FileConfig();
        fileConfigParam.setDomainCode(fileUploadVO.getDomainCode());
        fileConfigParam.setSubdomainCode(fileUploadVO.getSubdomainCode());
        fileConfigParam.setBusinessCode(fileUploadVO.getBusinessCode());

        FileConfig fileConfig = fileConfigService.findFileConfigByBizCode(fileConfigParam);
        if (fileConfig == null) {
            throw new FileSrvException(FileSrvCode.FILE_CONFIG_NOT_FOUND);
        }

        if("OBS".equals(fileServiceProperties.getSwitchFileWay())){
            if (fileConfig.getStorageService() == null || StrUtil.hasBlank(fileConfig.getBucketName(), fileConfig.getFileDirectory())) {
                throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "OBS文件配置错误：" + JSONUtil.toJsonStr(fileConfig));
            }
        } else if ("FTP".equals(fileServiceProperties.getSwitchFileWay())) {
            if (fileConfig.getStorageService() == null || StrUtil.hasBlank(fileConfig.getFileDirectory())) {
                throw new FileSrvException(FileSrvCode.FILE_CONFIG_ERROR, "FTP文件配置错误：" + JSONUtil.toJsonStr(fileConfig));
            }
        }
        // 查询字典配置，限制文件类型，未配置不限制
        DictQueryCondition dictQueryCondition = new DictQueryCondition();
        dictQueryCondition.setDomain(FSConstants.FILE_LIMIT_DOMAIN);
        dictQueryCondition.setDictTypeCode(FSConstants.FILE_LIMIT_DICT_TYPE_CODE);
        dictQueryCondition.setDictCode(FSConstants.FILE_LIMIT_DICT_CODE);
        R<DictionaryBO> dictResult = null;
        try {
            dictResult = this.dictionaryApi.getOne(dictQueryCondition);
        } catch (Exception e) {
            log.warn("限制文件类型，字典配置查询发生异常", e);
        }
        List<String> dictValueList = null;
        if (dictResult != null && dictResult.isSuccess()) {
            DictionaryBO dict = dictResult.getBody();
            if (dict != null && StrUtil.isNotBlank(dict.getDictValue())) {
                dictValueList = Arrays.asList(dict.getDictValue().split(","));
            } else {
                log.warn("限制文件类型的字典配置为空");
            }
        } else {
            log.warn("限制文件类型，字典配置查询失败：{}", dictResult != null ? dictResult.getHeader().getMessage() : "");
        }


        // 2.获取文件信息
        log.debug("进入文件组件方法upload....");
        List<MultipartFile> multipartFileList = fileUploadVO.getFileList();
        List<UploadBo> uploadBoList = new ArrayList<>();
        List<FileEntity> fileEntityList = new ArrayList<>();
        for (MultipartFile multipartFile:multipartFileList) {

            String fileName = multipartFile.getOriginalFilename();//文件名
            long fileSize = multipartFile.getSize();//文件大小
            String fileContentType = multipartFile.getContentType();//文件mime类型
            //文件后缀
            String fileSuffix = FileUtil.extName(fileName);
            log.debug("进入文件组件方法upload....文件名：" + fileName + ";大小：" + fileSize + ";文件类型：" + fileContentType + ";文件后缀：" + fileSuffix);
            if(!CollectionUtils.isEmpty(dictValueList) && !dictValueList.contains(fileSuffix)){
                throw new FileSrvException(FileSrvCode.FILE_LIMITED,
                        StrUtil.format("限制上传，可上传文件类型[{}]", StringUtils.join(dictValueList,",")));
            }


            // 3.先存储一次,获取文件id
            FileEntity fileDto = new FileEntity();
            fileDto.setFileName(fileName);
            fileDto.setFileType(fileContentType);
            fileDto.setFileSuffix(fileSuffix);
            fileDto.setFileSize(fileSize);
            fileDto.setFileMd5(MD5FileUtil.getMd5OfFile(multipartFile));
            fileDto.setDelFlag(0);
            fileMapper.insert(fileDto);

            //设置对象存储路径
            String fileDirectory = fileConfig.getFileDirectory();
            fileDirectory = fileDirectory.endsWith("/") ? fileDirectory : fileDirectory + "/";
            if (StrUtil.isNotBlank(fileUploadVO.getBusinessId())) {
                fileDirectory = fileDirectory + fileUploadVO.getBusinessId() + "/";
            }
            //对象存储路径
            String storageName = fileDto.getId() + "." + fileDto.getFileSuffix();
            String objectKey = fileDirectory + storageName;
            String bucketName = fileConfig.getBucketName();
            log.debug("bucketName:objectKey={}:{}", bucketName, objectKey);
            fileDto.setStorageName(storageName);
            fileDto.setDomainCode(fileConfig.getDomainCode());
            fileDto.setSubdomainCode(fileConfig.getSubdomainCode());
            fileDto.setStorageService(fileConfig.getStorageService());
            fileDto.setBusinessCode(fileConfig.getBusinessCode());
            fileDto.setBucketName(fileConfig.getBucketName());
            fileDto.setBucketDomain(fileConfig.getBucketDomain());
            fileDto.setFileDirectory(fileConfig.getFileDirectory());
            if (StrUtil.isNotBlank(fileUploadVO.getBusinessId())) {
                fileDto.setFileLeafDirectory(fileUploadVO.getBusinessId());
            }
            if (StrUtil.isNotBlank(fileUploadVO.getBusinessRemark())) {
                fileDto.setBusinessRemark(fileUploadVO.getBusinessRemark());
            }
            //将文件信息写入文件库中文件信息表t_file
            fileMapper.updateById(fileDto);
            fileEntityList.add(fileDto);
            //上传文件封装
            uploadBoList.add(new UploadBo().setBucketName(bucketName).setMultipartFile(multipartFile).setPath(objectKey));
        }


        abstractFileControlsService.uploadBatch(uploadBoList);
        return fileEntityList;
    }

    @Override
    public List<FileConfig> getFileConfigList() {
        return fileConfigMapper.selectList(new LambdaQueryWrapper<FileConfig>().eq(FileConfig::getDelFlag,0));
    }

    @Override
    public void saveFileConfig(FileConfig fileConfig) {
        fileConfig.setDelFlag(0);
        fileConfig.setCreateTime(new Date());
        fileConfigMapper.insert(fileConfig);
    }

    @Override
    public void deleteFileConfig(String configId) {
        fileConfigMapper.updateDelFlagById(configId);
    }


}
