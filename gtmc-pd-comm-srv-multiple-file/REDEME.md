### 文件服务配置说明

#### maven
服务集成
```xml
<!-- 文件服务 -->
<dependency>
    <groupId>cn.com.intasect</groupId>
    <artifactId>gtmc-pd-comm-srv-multiple-file-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
调用方
```xml
<!-- 文件服务 -->
<dependency>
    <groupId>cn.com.intasect</groupId>
    <artifactId>gtmc-pd-comm-srv-multiple-file-api</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
启动类配置，需扫描包：cn.com.intasect.multiple.file.api

#### 配置文件

- 服务名&缓存
- 配置switchFileWay选择对应文件服务！！！参数：OBS 选择华为OBS,则OBS信息必须配置；FTP 选择FTP模式，则ftp信息必须配置
```yaml
isct:
  common:
    fs:
      # 默认：common-multiple-fs-service
      service-name: common-multiple-fs-service
      # 是否使用缓存
      cache-enable: true
      switchFileWay: "OBS"
    ftp:
      config:
        host: 192.168.139.116
        username: root
        password: root
        win-ftp: true
        work-directory: /
      pool:
        pool-min-idle: 2

```
- 多数据源配置：必须配置
```yaml
isct:
  datasource:
    comm-fs: 数据源名称
```
- 选择OBS模式，华为OBS配置：必须配置
```yaml
huawei:
  obs:
    ak: 8A96QLS1IB2L5S06YRFJ
    sk: u79oXUK4I5qfALWE1WlOZCPQUcXE9RFz8AtgcLvc
    endPoint: https://obs.cn-north-4.myhuaweicloud.com
```

#### 字典配置
字段配置不存在则不限制文件类型!!!!

限制文件类型，多个文件后缀用英文逗号分隔，限制文件后缀类型存入字典值[dictValue]中，参考值：png,txt

查询文件限制类型通过参数：域[]、字典分类code[dictTypeCode]、字典编码[dictCode]

参数“字典分类code”、“字典编码”、“域”的值分别为：
file、fileTypeLimit、PD


### TODO
