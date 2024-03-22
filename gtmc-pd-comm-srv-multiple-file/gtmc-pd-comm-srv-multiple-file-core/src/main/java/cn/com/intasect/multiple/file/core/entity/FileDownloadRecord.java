package cn.com.intasect.multiple.file.core.entity;

import cn.com.intasect.core.data.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName("t_file_download_record")
public class FileDownloadRecord extends BaseEntity {

    /** 文件id */
    private Long fileId;

    /** 文件下载人姓名 */
    private String downloadUserName;

    /** 文件下载人id */
    private Long downloadUserId;

    /** 文件下载时间 */
    private Date downloadTime;

}
