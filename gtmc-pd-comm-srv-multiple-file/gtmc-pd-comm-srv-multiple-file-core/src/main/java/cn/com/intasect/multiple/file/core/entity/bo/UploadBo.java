package cn.com.intasect.multiple.file.core.entity.bo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @Author: renyitian
 * @Date: 2024/3/15 16:55
 * @Description:
 */
@Data
@Accessors(chain = true)
public class UploadBo  implements Serializable {

    private MultipartFile multipartFile;

    private String path;

    private String bucketName;

}
