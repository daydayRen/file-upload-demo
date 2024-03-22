package cn.com.intasect.multiple.file.core.service;

import cn.com.intasect.multiple.file.core.entity.bo.DownloadBo;
import cn.com.intasect.multiple.file.core.entity.bo.DownloadWithByteBo;
import cn.com.intasect.multiple.file.core.entity.bo.UploadBo;

import java.util.List;

/**
 * @Author: renyitian
 * @Date: 2024/3/15 16:08
 * @Description: 文件操作
 */
public interface AbstractFileControlsService {

    /**
      * @Author: renyitian
      * @Description: 上传
      * @Date: 17:26 2024/3/15
      * @Param:
      * @Return:
      */
    Boolean upload(UploadBo uploadBoh);

    /**
      * @Author: renyitian
      * @Description:  直接下载
      * @Date: 9:36 2024/3/18
      * @Param:  * 参数 downloadBo
      */
    void download(DownloadBo downloadBo);

    /**
      * @Author: renyitian
      * @Description: feign调用
      * @Date: 9:55 2024/3/18
      * @Param:  * 参数 downloadBo
      */
    DownloadWithByteBo downloadWithByte(DownloadBo downloadBo);

    /**
      * @Author: renyitian
      * @Description: 批量上传
      * @Date: 13:53 2024/3/21
      * @Param:  * 参数 uploadBoList
      */
    void uploadBatch(List<UploadBo> uploadBoList);
}
