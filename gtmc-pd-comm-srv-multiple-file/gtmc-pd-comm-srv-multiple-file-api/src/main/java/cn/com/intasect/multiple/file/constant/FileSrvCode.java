package cn.com.intasect.multiple.file.constant;

import cn.com.intasect.core.domain.constant.Code;

public enum FileSrvCode implements Code {
    FILE_CONFIG_NOT_FOUND(FILE_SRV_BASE_CODE + 1, "fs.file.config.notfound", "文件配置不存在"),
    FILE_CONFIG_ERROR(FILE_SRV_BASE_CODE + 2, "fs.file.config.error", "文件配置错误"),
    FILE_NOT_FOUND(FILE_SRV_BASE_CODE + 3, "fs.file.notfound", "文件不存在"),
    FILE_LIMITED(FILE_SRV_BASE_CODE + 4, "fs.file.limited", "当前文件限制上传"),

    FILE_DOWNLOAD_ERROR(FILE_SRV_BASE_CODE + 5, "fs.file.dowload", "文件下载错误"),

    FILE_UPLOAD_ERROR(FILE_SRV_BASE_CODE + 6, "fs.file.uploda", "文件上传错误");


    private final Integer code;
    private final String name;
    private final String message;

    private FileSrvCode(Integer code, String name, String msg) {
        this.code = code;
        this.name = name;
        this.message = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return String.valueOf(this.code);
    }
}
