package com.ais.udep.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/23
 * @Description:
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.ais.udep")
public class UdepConfig {
}
