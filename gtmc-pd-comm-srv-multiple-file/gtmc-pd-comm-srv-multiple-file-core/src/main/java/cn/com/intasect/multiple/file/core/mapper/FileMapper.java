package cn.com.intasect.multiple.file.core.mapper;

import cn.com.intasect.multiple.file.core.entity.FileEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface FileMapper extends BaseMapper<FileEntity> {

    FileEntity findFileById(@Param("fileId") Long fileId);

    void updateDelFlagById(@Param("fileDto") FileEntity fileDto);

}
