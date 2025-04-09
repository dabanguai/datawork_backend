package com.dbg.datawork.model.entity;

import lombok.Data;

/**
 * @author 15968
 * @version 1.0
 * @description: 存储DDL数据校验结果
 * @date 2025/4/2 9:01
 */
@Data
public class ColumnCompareResult {
    private String columnName;
    private ColumnMeta sourceColumn;
    private ColumnMeta targetColumn;
    private boolean isTypeMatch;
    private boolean isSourceMissing;
    private boolean isTargetMissing;

    private String mismatchReason;

    // 静态工厂方法
    public static ColumnCompareResult match(String columnName, String targetColumnName, ColumnMeta source, ColumnMeta target, boolean isMatched) {
        ColumnCompareResult result = new ColumnCompareResult();
        result.setColumnName(columnName);
        result.setSourceColumn(source);
        result.setTargetColumn(target);
        result.setTypeMatch(isMatched);
        return result;
    }

    public static ColumnCompareResult missingInTarget(String columnName, ColumnMeta source) {
        ColumnCompareResult result = new ColumnCompareResult();
        result.setColumnName(columnName);
        result.setSourceColumn(source);
        result.setTargetMissing(true);
        return result;
    }

    public static ColumnCompareResult missingInSource(String columnName, ColumnMeta target) {
        ColumnCompareResult result = new ColumnCompareResult();
        result.setColumnName(columnName);
        result.setTargetColumn(target);
        result.setSourceMissing(true);
        return result;
    }

    public static ColumnCompareResult typeMismatch(String columnName, ColumnMeta source, ColumnMeta target, String reason) {
        ColumnCompareResult result = new ColumnCompareResult();
        result.setColumnName(columnName);
        result.setSourceColumn(source);
        result.setTargetColumn(target);
        result.setTypeMatch(false);
        result.setMismatchReason(reason);
        return result;
    }

    // 业务方法
    public boolean isPerfectMatch() {
        return !isSourceMissing && !isTargetMissing && isTypeMatch;
    }

    public String getDiffDescription() {
        if (isSourceMissing) {
            return String.format("列 [%s] 在源表中缺失（目标表类型: %s）", columnName, targetColumn.getType());
        } else if (isTargetMissing) {
            return String.format("列 [%s] 在目标表中缺失（源表类型: %s）", columnName, sourceColumn.getType());
        } else if (!isTypeMatch) {
            return String.format("列 [%s] 类型不匹配: 源表类型=%s, 目标表类型=%s",
                    columnName, sourceColumn.getType(), targetColumn.getType());
        }
        return "列结构一致";
    }
}
