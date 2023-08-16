package com.mydevlog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Data
@Configuration
@ConfigurationProperties(prefix = "mydevlog")
public class Appconfig {
    private byte[] jwtKey;

    public void setJwtKey(String jwtKey) { // configuration 단계에서 setter로 들어옴
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }
}
