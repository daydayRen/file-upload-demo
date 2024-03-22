package cn.com.intasect.multiple.file.core.controller.v2;

import cn.com.intasect.multiple.file.bo.DownloadCondition;
import cn.com.intasect.multiple.file.core.controller.FileApiController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 特殊场景：不同用户角色权限控制
 */
@Validated
@RestController
@RequestMapping("/v2/api/common/file")
public class FileApiV2Controller {

    @Resource
    FileApiController fileApiController;

    @RequestMapping(value = "download", method = RequestMethod.POST)
    public void download(@Valid @NotNull(message = "参数不能为空") @RequestBody DownloadCondition downloadCondition) {
        this.fileApiController.download(downloadCondition);
    }


}
