package com.knowledge.graph.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(GraphConfig.PREFIX)
public class GraphConfig {

    public static final String PREFIX = "graph";

    private String projPath;

}
