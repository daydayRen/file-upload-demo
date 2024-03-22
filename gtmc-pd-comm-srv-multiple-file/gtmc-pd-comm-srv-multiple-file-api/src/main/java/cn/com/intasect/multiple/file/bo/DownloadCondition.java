package cn.com.intasect.multiple.file.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 文件下载条件
 */
@Data
@ApiModel("文件下载条件")
public class DownloadCondition implements Serializable {

    /**
     * 文件ID
     */
    @NotNull(message = "[文件ID]不能为空")
    @ApiModelProperty("文件ID")
    private Long fileId;
    
    /**
     * 导出人ID
     */
    @ApiModelProperty("下载人ID")
    @NotNull(message = "[下载人ID]不能为空")
    private Long downloadUserId;

    /**
     * 下载人姓名
     */
    @ApiModelProperty("下载人姓名")
    @NotNull(message = "[下载人姓名]不能为空")
    private String downloadUserName;

}
