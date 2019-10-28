package com.mykid.platform.common.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author MrBird
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:platform.properties"})
@ConfigurationProperties(prefix = "platform")
public class PlatformProperties {

    private ShiroProperties shiro = new ShiroProperties();
    private boolean openAopLog = true;
    private boolean autoOpenBrowser = true;
    private String[] autoOpenBrowserEnv = {};
    private SwaggerProperties swagger = new SwaggerProperties();
}
