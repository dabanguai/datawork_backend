package com.dbg.datawork.service;

import cn.hutool.core.util.StrUtil;
import com.dbg.datawork.datasource.JdbcProvider;
import com.dbg.datawork.model.dto.dataCheck.DataCheckDTO;
import com.dbg.datawork.model.entity.ColumnCompareResult;
import com.dbg.datawork.model.entity.ColumnMeta;
import com.dbg.datawork.service.Processor.DatasourceProcessor;
import com.dbg.datawork.service.Processor.DatasourceProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/4/2 8:42
 */
@Service
@RequiredArgsConstructor
public class ColumnComparator {

    @Resource
    private JdbcProvider jdbcProvider;

    private final DatasourceProcessorFactory processorFactory;

    public List<ColumnCompareResult> compareColumns(DataCheckDTO dataCheckDTO, String sourceType, String targetType) {
        // 获取源和目标处理器
        DatasourceProcessor sourceProcessor = processorFactory.getProcessor(sourceType);
        DatasourceProcessor targetProcessor = processorFactory.getProcessor(targetType);

        // 获取元数据 todo 扩展，多表处理
        List<ColumnMeta> sourceColumns = sourceProcessor.getTableColumns(dataCheckDTO.getSourceTable(), jdbcProvider.getJdbcTemplate(sourceProcessor.getSupportedDataSourceType()));
        List<ColumnMeta> targetColumns = targetProcessor.getTableColumns(dataCheckDTO.getTargetTable(), jdbcProvider.getJdbcTemplate(targetProcessor.getSupportedDataSourceType()));

        return doCompare(sourceColumns, targetColumns, sourceProcessor.getTypeMappings());
    }

    private List<ColumnCompareResult> doCompare(
            List<ColumnMeta> sourceCols,
            List<ColumnMeta> targetCols,
            Map<String, Set<String>> typeMappings
    ) {
        // 辅助方法：生成标准化名称到原始字段的映射（解决驼峰式与下划线式名称匹配问题）
        Map<String, ColumnMeta> sourceNormalizedMap = toNormalizedMap(sourceCols);
        Map<String, ColumnMeta> targetNormalizedMap = toNormalizedMap(targetCols);

        List<ColumnCompareResult> results = new ArrayList<>();

        // 第一阶段：匹配字段并检查类型兼容性
        sourceNormalizedMap.forEach((normalizedName, srcMeta) -> {
            if (targetNormalizedMap.containsKey(normalizedName)) {
                ColumnMeta tgtMeta = targetNormalizedMap.get(normalizedName);
                // 类型兼容性检查（基于原始类型）
                boolean isTypeCompatible = false;
                if (StrUtil.equalsIgnoreCase(srcMeta.getType(), tgtMeta.getType())) {
                    isTypeCompatible = true;
                } else {
                    isTypeCompatible = typeMappings.getOrDefault(srcMeta.getType(), Set.of())
                            .contains(tgtMeta.getType());
                }
                results.add(ColumnCompareResult.match(
                        srcMeta.getName(),   // 保留原始字段名
                        tgtMeta.getName(),   // 保留原始字段名
                        srcMeta,
                        tgtMeta,
                        isTypeCompatible
                ));
            }
        });

        // 第二阶段：处理缺失字段（基于标准化名称）
        Set<String> allNormalizedNames = new HashSet<>();
        allNormalizedNames.addAll(sourceNormalizedMap.keySet());
        allNormalizedNames.addAll(targetNormalizedMap.keySet());

        for (String normalizedName : allNormalizedNames) {
            boolean inSource = sourceNormalizedMap.containsKey(normalizedName);
            boolean inTarget = targetNormalizedMap.containsKey(normalizedName);

            if (!inSource) {
                // 目标存在但源缺失
                ColumnMeta tgtMeta = targetNormalizedMap.get(normalizedName);
                results.add(ColumnCompareResult.missingInSource(
                        tgtMeta.getName(),  // 使用原始字段名
                        tgtMeta
                ));
            } else if (!inTarget) {
                // 源存在但目标缺失
                ColumnMeta srcMeta = sourceNormalizedMap.get(normalizedName);
                results.add(ColumnCompareResult.missingInTarget(
                        srcMeta.getName(),  // 使用原始字段名
                        srcMeta
                ));
            }
        }

        return results;
    }

    /**
     * @description: 将字段列表转换为标准化名称（下划线式）到原始字段的映射
     */
    private Map<String, ColumnMeta> toNormalizedMap(List<ColumnMeta> columns) {
        return columns.stream()
                .collect(Collectors.toMap(
                        meta -> camelToUnderscore(meta.getName()), // 驼峰式转下划线式
                        Function.identity(),
                        (existing, replacement) -> existing // 处理重复键（保留第一个）
                ));
    }

    /**
     * @description: 驼峰式转下划线式（用于名称标准化）
     */
    private String camelToUnderscore(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

}
