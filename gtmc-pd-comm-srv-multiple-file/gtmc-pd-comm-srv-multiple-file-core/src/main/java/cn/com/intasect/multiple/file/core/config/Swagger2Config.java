package cn.com.intasect.multiple.file.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


@Configuration
@EnableSwagger2WebMvc
public class Swagger2Config {
    //    http://localhost:8088/swagger-ui.html     原路径（swagger自带的ui）
    //    http://localhost:8088/doc.html     原路径


    // 配置swagger2核心配置 docket
    @Bean
    public Docket createRestApi(){
        return  new Docket(DocumentationType.SWAGGER_2)//制定api类型为swagger2类型
                .apiInfo(apiInfo()) //定义api文档汇总信息
                .select().apis(RequestHandlerSelectors.basePackage("cn.com.intasect.multiple.file"))//扫描哪个包下面 //RequestHandlerSelectors.withClassAnnotation(Api.class)存在注解的都都会拿取到
                .paths(PathSelectors.any())//所有接口
                .build();
    }


    private ApiInfo apiInfo() {


        return new ApiInfoBuilder()
                .title("文件操作Api")  //标题
                .contact(new Contact("renyitian","http://www.intasect.com.cn/","renyitian@intasect.com.cn"))//联系方式
                .description("文件操作Api")//描述
                .version("0.0.1")//版本
                .termsOfServiceUrl("http://www.intasect.com.cn/")//网址
                .build();


    }


}
