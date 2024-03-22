package cn.com.intasect.multiple.file.core.service.impl;

import cn.com.intasect.multiple.file.core.properties.FileServiceProperties;
import cn.com.intasect.multiple.file.core.entity.FileConfig;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component("fileServiceCacheManager")
public class FileServiceCacheManager {
    // 文件配置数据缓存key
    private static final String COMM_FILE_CONFIG_CACHE_KEY = "comm_file_config_cache_key";
    // 缓存有效期（10分钟），单位：秒
    private static final long period = 600;

    private final FileServiceProperties fileServiceProperties;
    private final HazelcastInstance hazelcastInstance;

    FileServiceCacheManager(FileServiceProperties fileServiceProperties,
                            @Nullable HazelcastInstance hazelcastInstance) {
        this.fileServiceProperties = fileServiceProperties;
        this.hazelcastInstance = hazelcastInstance;
    }

    /**
     * 缓存文件配置数据
     */
    public void cacheFileConfig(String key, FileConfig fileConfig) {
        if (this.canCache(key, fileConfig)) {
            this.hazelcastInstance.<String, FileConfig>getMap(COMM_FILE_CONFIG_CACHE_KEY)
                    .put(key, fileConfig, period, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取文件配置数据
     */
    public FileConfig getFileConfig(String key) {
        if (this.canGet(key)) {
            return this.hazelcastInstance.<String, FileConfig>getMap(COMM_FILE_CONFIG_CACHE_KEY).get(key);
        }
        return null;
    }

    private boolean canCache(String key, List<?> list) {
        return this.hazelcastInstance != null && this.fileServiceProperties.getCacheEnable() &&
                StrUtil.isNotBlank(key) && CollectionUtil.isNotEmpty(list);
    }

    private boolean canCache(String key, Object obj) {
        return this.hazelcastInstance != null && this.fileServiceProperties.getCacheEnable() &&
                StrUtil.isNotBlank(key) && obj != null;
    }

    private boolean canGet(String key) {
        return this.hazelcastInstance != null && this.fileServiceProperties.getCacheEnable() && StrUtil.isNotBlank(key);
    }
}
