package cn.com.intasect.multiple.file.core.exception;

import cn.com.intasect.core.domain.constant.Code;
import lombok.Data;

/**
 * 文件共通自定义异常
 *
 * @author hed
 * @since 2023/3/22
 **/
@Data
public class FileSrvException extends RuntimeException {

    private static final long serialVersionUID = -5169308531913390186L;

    private Integer errorCode;

    private String errorMessage;

    private Code code;

    public static final String DEFAULT_CODE_NAME = "file.srv.comm_unknown";

    public FileSrvException(Code code) {
        super(code.getMessage());
        this.code = code;
        this.errorCode = code.getCode();
        this.errorMessage = code.getMessage();
    }

    public FileSrvException(Code code, String msg) {
        super(msg);
        this.code = code;
        this.errorCode = code.getCode();
        this.errorMessage = msg;
    }

    public FileSrvException(int code, String msg) {
        super(msg);
        this.code = Code.newInstance(code, DEFAULT_CODE_NAME, msg);
        this.errorCode = code;
        this.errorMessage = msg;
    }

    /**
     * 禁止调用原生的fillInStackTrace方法去爬堆栈
     * 提升性能，同时丢失异常上下文的堆栈信息
     * 便于将ServiceException用于业务流程控制
     *
     * @return java.lang.Throwable
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String toString() {
        return String.format("{ \"code\" :\"%s\", \"message\": \"%s\"}", errorCode, errorMessage);
    }

}
