package com.cetc.pacong.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {
        "${application_properties_location}",
        "${server_sys_properties_location}"
}, ignoreResourceNotFound = false)
public class InformalEnvPropertiesConfig {

}