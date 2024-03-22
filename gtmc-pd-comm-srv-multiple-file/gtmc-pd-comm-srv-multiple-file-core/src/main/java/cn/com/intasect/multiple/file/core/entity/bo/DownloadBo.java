package cn.com.intasect.multiple.file.core.entity.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: renyitian
 * @Date: 2024/3/15 16:55
 * @Description:
 */
@Data
@Accessors(chain = true)
public class DownloadBo implements Serializable {

    private String path;


    private String bucketName;

    /** 原始文件名 */
    private String fileName;

}
