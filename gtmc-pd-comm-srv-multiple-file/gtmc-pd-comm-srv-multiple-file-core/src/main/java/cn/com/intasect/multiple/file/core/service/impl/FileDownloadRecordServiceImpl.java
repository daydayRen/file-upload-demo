package cn.com.intasect.multiple.file.core.service.impl;

import cn.com.intasect.multiple.file.constant.FSConstants;
import cn.com.intasect.multiple.file.core.mapper.FileDownloadRecordMapper;
import cn.com.intasect.multiple.file.core.service.FileDownloadRecordService;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@DS(FSConstants.DS_NAME_FS)
@Service
public class FileDownloadRecordServiceImpl implements FileDownloadRecordService {

    @Resource
    FileDownloadRecordMapper fileDownloadRecordMapper;


}
