package cn.com.intasect.multiple.file.core.mapper;

import cn.com.intasect.multiple.file.core.entity.FileConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface FileConfigMapper extends BaseMapper<FileConfig> {


    FileConfig findFileConfigByBizCode(@Param("fileConfig") FileConfig fileConfig);

    void updateDelFlagById(@Param("configId") String configId);

    void save(FileConfig fileConfig);
}
