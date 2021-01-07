package com.xh.ybinterface.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "xh.config")
@Data
@ToString
public class ConfigVo {
    private String dataCatrg;
    private String authPhacCodg;
    private String rtalPhacCodg;
    private String rtalPhacName;
}
