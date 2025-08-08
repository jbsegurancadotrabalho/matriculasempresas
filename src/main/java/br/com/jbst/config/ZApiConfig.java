package br.com.jbst.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "zapi")
@Data
public class ZApiConfig {
    private String instanceId;
    private String token;
}
