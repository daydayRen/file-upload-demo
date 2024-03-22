package cn.com.intasect.multiple.file.core.entity;

import cn.com.intasect.core.data.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_file_config")
public class FileConfig extends BaseEntity {

    /** 域code */
    private String domainCode;

    /** 域名称 */
    private String domainName;

    /** 子域code */
    private String subdomainCode;

    /** 子域名称 */
    private String subdomainName;

    /** 文件业务code */
    private String businessCode;

    /** 文件业务名称 */
    private String businessName;

    /** 存储介质 */
    private Integer storageService;

    /** 桶名称 */
    private String bucketName;

    /** 桶路径 */
    private String bucketDomain;

    /** 文件存储目录 */
    private String fileDirectory;

    /** 禁用状态 */
    private Integer disabled;

    /** 有效开始时间 */
    private Data validStartTime;

    /** 有效结束时间 */
    private Data validEndTime;

}
