package com.dbg.datawork.model.dto.datasource;

import com.dbg.datawork.common.PageRequest;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/23 17:18
 */
public class DatasourceRequest extends PageRequest {

    private String id;
    private String type;

    private String name;

    private String configuration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    // 自定义 setter 方法：将 JSON 对象转换为字符串
    @JsonSetter("configuration")
    public void setConfiguration(Object config) {
        try {
            this.configuration = new ObjectMapper().writeValueAsString(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize configuration", e);
        }
    }

    // 自定义 getter 方法：将字符串转换为 JSON 对象
    @JsonGetter("configuration")
    public Object getConfigurationObject() {
        try {
            return new ObjectMapper().readValue(configuration, Object.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize configuration", e);
        }
    }
}
