package cn.com.intasect.multiple.file.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: renyitian
 * @Date: 2024/3/21 14:14
 * @Description: 文件配置
 */
@Data
@ApiModel("配置文件信息")
public class FileConfigVo implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long Id;
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
/*
    *//** 禁用状态 *//*
    private Integer disabled;

    *//** 有效开始时间 *//*
    private Data validStartTime;

    *//** 有效结束时间 *//*
    private Data validEndTime;*/
}
