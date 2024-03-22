package cn.com.intasect.multiple.file.core.service.impl;

import cn.com.intasect.multiple.file.constant.FSConstants;
import cn.com.intasect.multiple.file.core.entity.FileConfig;
import cn.com.intasect.multiple.file.core.mapper.FileConfigMapper;
import cn.com.intasect.multiple.file.core.service.FileConfigService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@DS(FSConstants.DS_NAME_FS)
@Service
public class FileConfigServiceImpl implements FileConfigService {

    @Resource
    private FileConfigMapper fileConfigMapper;
    @Resource
    @Qualifier("fileServiceCacheManager")
    private FileServiceCacheManager fileServiceCacheManager;

    @Override
    public FileConfig findFileConfigByBizCode(FileConfig fileConfig) {
        String key = "";
        boolean keyIntact = true;
        if (StrUtil.isBlank(fileConfig.getDomainCode())) {
            keyIntact = false;
        } else {
            key += fileConfig.getDomainCode();
        }

        if (StrUtil.isBlank(fileConfig.getSubdomainCode())) {
            keyIntact = false;
        } else {
            key += fileConfig.getSubdomainCode();
        }

        if (StrUtil.isBlank(fileConfig.getBusinessCode())) {
            keyIntact = false;
        } else {
            key += fileConfig.getBusinessCode();
        }

        FileConfig config;
        if (keyIntact) {
            config = this.fileServiceCacheManager.getFileConfig(key);
            if (config != null) {
                return config;
            }
        }

        config = fileConfigMapper.findFileConfigByBizCode(fileConfig);
        if (keyIntact) {
            this.fileServiceCacheManager.cacheFileConfig(key, config);
        }

        return config;
    }
}
