package cn.com.intasect.multiple.file.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileDto implements Serializable {

    private Long id;

    /** 原始文件名 */
    private String fileName;

    /** 存储文件名 */
    private String storageName;

    /** 文件大小 */
    private Long fileSize;

    /** 文件类型 */
    private String fileType;

    /** 文件后缀 */
    private String fileSuffix;

    /** 存储介质 */
    private Integer storageService;

    /** 域code */
    private String domainCode;

    /** 子域code */
    private String subdomainCode;

    /** 文件业务code */
    private String businessCode;

    /** 桶名称 */
    private String bucketName;

    /** 桶路径 */
    private String bucketDomain;

    /** 文件存储目录 */
    private String fileDirectory;

    /** 文件叶子目录 */
    private String fileLeafDirectory;

    /** 业务备注 */
    private String businessRemark;
}
