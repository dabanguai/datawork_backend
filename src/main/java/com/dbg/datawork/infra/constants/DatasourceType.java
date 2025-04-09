package com.dbg.datawork.infra.constants;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 2:17
 */
public enum DatasourceType {
    mysql("mysql", "mysql", "com.mysql.jdbc.Driver", "`", "`", "'", "'"),
    postgresql("postgresql", "postgresql", "org.postgresql.Driver", "\"", "\"", "\"", "\"");

    private String feature;
    private String description;
    private String driver;
    private String keywordPrefix;
    private String keywordSuffix;
    private String aliasPrefix;
    private String aliasSuffix;

    DatasourceType(String feature, String description, String driver, String keywordPrefix, String keywordSuffix, String aliasPrefix, String aliasSuffix) {
        this.feature = feature;
        this.description = description;
        this.driver = driver;
        this.keywordPrefix = keywordPrefix;
        this.keywordSuffix = keywordSuffix;
        this.aliasPrefix = aliasPrefix;
        this.aliasSuffix = aliasSuffix;
    }

    public String getFeature() {
        return feature;
    }

    public String getDescription() {
        return description;
    }

    public String getDriver() {
        return driver;
    }

    public String getKeywordPrefix() {
        return keywordPrefix;
    }

    public String getKeywordSuffix() {
        return keywordSuffix;
    }

    public String getAliasPrefix() {
        return aliasPrefix;
    }

    public String getAliasSuffix() {
        return aliasSuffix;
    }
    }
