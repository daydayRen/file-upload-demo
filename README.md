# file-upload-demo


### 存在问题：
	存在不同用户使用不同文件服务器，需要各自搭建不同的文件操作，例如OBS、FTP等等，但是功能却都是上传、下载、删除等操作!那么是不是可以使用一种通用得方式实现上传、下载、删除呢？答案是可以得！
### 解决:
	文件得操作我们统一路径，但是在具体得操作上，根据不同的配置去实现真正的操作，本此使用@ConditionalOnProperty注解(下面会讲到)；根据配置了哪种操作方式调用对用得方法！对应得类图如下

 <img src="https://github.com/daydayRen/file-upload-demo/blob/main/file/%E6%96%87%E4%BB%B6%E7%B3%BB%E7%BB%9F%E7%B1%BB%E5%9B%BE.png"></img>
	


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
