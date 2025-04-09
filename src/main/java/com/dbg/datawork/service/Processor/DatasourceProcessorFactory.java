package com.dbg.datawork.service.Processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/4/2 8:37
 */
@Component
public class DatasourceProcessorFactory {

    private final Map<String, DatasourceProcessor> processorGroup = new ConcurrentHashMap<>();

    // 自动注册所有实现 Bean
    @Autowired
    public void DataSourceProcessorFactory(List<DatasourceProcessor> processorList) {
        processorList.forEach(proc ->
                processorGroup.put(proc.getSupportedDataSourceType().toLowerCase(), proc)
        );
    }

    // 手动注册扩展处理器
    public void registerProcessor(DatasourceProcessor processor) {
        processorGroup.put(processor.getSupportedDataSourceType().toLowerCase(), processor);
    }

    public DatasourceProcessor getProcessor(String dataSourceType) {
        return Optional.ofNullable(processorGroup.get(dataSourceType.toLowerCase()))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported data source: " + dataSourceType));
    }

}
