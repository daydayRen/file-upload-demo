package cn.com.intasect.multiple.file.core.service;

import cn.com.intasect.multiple.file.bo.DownloadCondition;
import cn.com.intasect.multiple.file.core.entity.FileConfig;
import cn.com.intasect.multiple.file.core.entity.FileEntity;
import cn.com.intasect.multiple.file.core.entity.bo.DownloadWithByteBo;
import cn.com.intasect.multiple.file.vo.FileUploadBatchVO;
import cn.com.intasect.multiple.file.vo.FileUploadVO;

import java.util.List;

public interface CommFileService {

    FileEntity upload(FileUploadVO fileUploadVO);

    void download(DownloadCondition downloadCondition);

    DownloadWithByteBo downloadWithByte(DownloadCondition downloadCondition);

    FileEntity detail(Long fileId);

    void delete(Long fileId);

    List<FileEntity> uploadBatch(FileUploadBatchVO fileUploadVO);

    List<FileConfig> getFileConfigList();

    void saveFileConfig(FileConfig fileConfig);

    void deleteFileConfig(String configId);
}
