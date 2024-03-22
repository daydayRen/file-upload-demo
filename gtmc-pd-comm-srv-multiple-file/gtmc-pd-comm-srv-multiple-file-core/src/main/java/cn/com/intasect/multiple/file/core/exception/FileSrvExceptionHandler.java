package cn.com.intasect.multiple.file.core.exception;

import cn.com.intasect.core.domain.constant.Code;
import cn.com.intasect.core.domain.response.R;
import cn.com.intasect.core.domain.response.RHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 文件共通自定义异常处理器
 *
 * @author hed
 * @since 2023/3/22
 **/
@Slf4j
@RestControllerAdvice
@ConditionalOnClass(value = {FileSrvException.class})
public class FileSrvExceptionHandler {

    @ExceptionHandler(FileSrvException.class)
    public ResponseEntity todoServiceException(FileSrvException ex) {
        log.warn(":::文件服务接口出现异常::: {}", ex.getMessage());
        Code code = Code.newInstance(ex.getCode().getCode(), ex.getCode().getName(), ex.getMessage());
        RHeader header = new RHeader(code);
        R result = R.build(header);
        return ResponseEntity.ok(result);
    }

}
