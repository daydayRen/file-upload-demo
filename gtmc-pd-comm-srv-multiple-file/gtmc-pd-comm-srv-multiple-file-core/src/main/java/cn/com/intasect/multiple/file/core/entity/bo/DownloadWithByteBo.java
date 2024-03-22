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
public class DownloadWithByteBo implements Serializable {

    //字节
    private byte[] bytes;
    //文件名
    private String contentDisposition;
    //大小
    private Long contentLength;

}
