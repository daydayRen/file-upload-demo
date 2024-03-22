package cn.com.intasect.multiple.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 文件上传VO
 *
 * @author liuyuanqing
 * @since 2023/3/21
 **/
@Data
@ApiModel("文件上传VO")
public class FileUploadVO {

    @NotNull(message = "文件不能为空")
    @ApiModelProperty(value = "文件",required = true)
    private MultipartFile file;

    @NotBlank(message = "域code不能为空")
    @ApiModelProperty(value = "域code编码",required = true)
    private String domainCode;

    @NotBlank(message = "子域code不能为空")
    @ApiModelProperty(value = "子域code编码",required = true)
    private String subdomainCode;

    @NotBlank(message = "业务code不能为空")
    @ApiModelProperty(value = "业务code编码",required = true)
    private String businessCode;

    @ApiModelProperty(value = "业务id",required = false)
    private String businessId;

    @ApiModelProperty(value = "业务备注",required = false)
    private String businessRemark;


}
