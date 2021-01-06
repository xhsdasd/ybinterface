package com.xh.ybinterface.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "xh.thread")
@Component
@Data
public class ThreadPoolConfigPropoties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
