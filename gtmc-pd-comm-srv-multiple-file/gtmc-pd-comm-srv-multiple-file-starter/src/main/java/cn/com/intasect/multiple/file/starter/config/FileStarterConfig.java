package cn.com.intasect.multiple.file.starter.config;

import cn.com.intasect.config.hazelcast.HazelcastConfig;
import cn.com.intasect.multiple.file.core.properties.FileServiceProperties;
import com.hazelcast.config.Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * FileStarterConfig 文件组件启动类配置
 *
 * @author liuyuanqing
 * @since 2023/3/20
 **/
@Configuration
@ComponentScan(basePackages = {
        "cn.com.intasect.multiple.file.core",
        "cn.com.intasect.multiple.file.api"
})
@MapperScan({"cn.com.intasect.multiple.file.core.**.mapper*"})
@EnableConfigurationProperties(FileServiceProperties.class)
public class FileStarterConfig extends HazelcastConfig {

    @Bean
    @ConditionalOnMissingBean(name = "hzConfig")
    @ConditionalOnProperty(prefix = "isct.common", name = "fs.cache-enable", havingValue = "true", matchIfMissing = true)
    public Config hzConfig() {
        return this.getHZConfig();
    }
}
