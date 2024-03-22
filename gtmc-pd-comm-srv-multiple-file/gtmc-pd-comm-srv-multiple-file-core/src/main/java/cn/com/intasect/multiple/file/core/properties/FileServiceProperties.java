package cn.com.intasect.multiple.file.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件服务的配置项
 *
 * @author hed
 * @since 2022/7/13
 */
@Data
@ConfigurationProperties(prefix = "isct.common.fs")
public class FileServiceProperties {
    // 默认：common-multiple-fs-service
    private String serviceName = "common-multiple-fs-service";
    // 是否使用缓存
    private Boolean cacheEnable = true;
    // 是否使用ftp  默认使用OBS
    private String switchFileWay = "OBS";
}
