package com.dbg.datawork.datasource;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.dbg.datawork.exception.DatasetExecuteException;
import com.dbg.datawork.infra.common.ErrorCode;
import com.dbg.datawork.infra.constants.DataTypeMapping;
import com.dbg.datawork.infra.constants.DatasourceType;
import com.dbg.datawork.infra.constants.DefaultValueMapping;
import com.dbg.datawork.exception.DataSourceConnectionException;
import com.dbg.datawork.infra.database.DynamicDataSourceRegisterHelper;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.pojo.Datasource;
import com.dbg.datawork.service.ColumnComparator;
import com.dbg.datawork.service.impl.DatasourceServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 2:03
 */
@Slf4j
@Service("jdbc")
public class JdbcProvider {

    @Resource
    private DatasourceServiceImpl datasourceService;

    @Resource
    private JdbcTemplate mysqlJdbcTemplate;

    @Resource
    private JdbcTemplate postgresJdbcTemplate;

    @Resource
    private DynamicDataSourceRegisterHelper dynamicHelper;

    public JdbcTemplate getJdbcTemplate(String dataBaseType) {
        switch (dataBaseType) {
            case "postgresql": return postgresJdbcTemplate;
            default: return mysqlJdbcTemplate;
        }
    }

    public Connection getConnection(DatasourceRequest.GetConnection datasourceRequest) {

        String username = null;
        String password = null;
        String driver = null;
        String url = null;
        DatasourceType datasourceType = DatasourceType.valueOf(datasourceRequest.getType());

        try {
            switch (datasourceType) {
                case mysql:
                    MysqlConfiguration mysqlConfiguration = new ObjectMapper().readValue(datasourceRequest.getConfiguration(), MysqlConfiguration.class);
                    username = mysqlConfiguration.getUserName();
                    password = mysqlConfiguration.getPassword();
                    driver = mysqlConfiguration.getDriver();
                    url = mysqlConfiguration.spliceUrl();
                    break;
                case postgresql:
                    PgConfiguration pgConfiguration = new ObjectMapper().readValue(datasourceRequest.getConfiguration(), PgConfiguration.class);
                    username = pgConfiguration.getUserName();
                    password = pgConfiguration.getPassword();
                    driver = pgConfiguration.getDriver();
                    url = pgConfiguration.spliceUrl();
                    break;
                default:
                    break;
            }
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;

        } catch (JsonProcessingException | ClassNotFoundException | SQLException e) {
            // 封装为自定义运行时异常
            throw new DataSourceConnectionException(ErrorCode.DATASOURCE_CONNECTION_ERROR, e.getMessage());
        }
    }



    /**
     * @description: 获取建表语句
     * @author 15968
     * @date 2025/2/24 16:36
     * @version 1.0
     */
    @DS("mysql")
    public String getMysqlDDL(DatasourceRequest.GetSourceDDL datasourceRequest) throws SQLException {

        Statement statement;
        String mysqlDDL = null;

        Datasource datasource = datasourceService.getByInfo(datasourceRequest.getDatabaseType(), datasourceRequest.getDatabaseName());;
        // todo 异常处理

        DataSource ds = dynamicHelper.registerAndGet(new DatasourceRequest.GetConnection(datasource.getType(), datasource.getName(), datasource.getConfiguration()));
        Connection conn = ds.getConnection();

        // DatasourceRequest.GetSourceDDL request = new DatasourceRequest.GetSourceDDL(new DatasourceRequest.GetConnection(datasource.getType(), datasource.getName(), datasource.getConfiguration()), datasourceRequest.getTableName());
        try {
            statement = conn.createStatement();
            String sql = "SHOW CREATE TABLE " + datasourceRequest.getTableName();
            ResultSet rs = statement.executeQuery(sql );
            while (rs.next() && ObjUtil.isEmpty(mysqlDDL)) {
                mysqlDDL = rs.getString(2);
                log.info("获取建表语句：{}", rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        statement.close();
        return mysqlDDL;
    }

    public String transferTable(DatasourceRequest.GetTransferDDL transferDDLRequest) {
        String originalDDL = transferDDLRequest.getOriginalDDL().replaceAll("`", "");
        StringBuilder totalSql = new StringBuilder();
        try {
            Statements statements = CCJSqlParserUtil.parseStatements(originalDDL);
            for (net.sf.jsqlparser.statement.Statement statement : statements.getStatements()) {
                String sql = this.process((CreateTable) statement);
                totalSql.append(sql).append("\n");
            }
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        return totalSql.toString();
    }

    public String process(CreateTable createTable) {
        String tableFullyQualifiedName = createTable.getTable().getFullyQualifiedName();
        List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();

        List<String> tableOptionsStrings = createTable.getTableOptionsStrings();
        String tableCommentSql = null;
        int commentIndex = tableOptionsStrings.indexOf("COMMENT");
        if (commentIndex != -1) {
            tableCommentSql = String.format("COMMENT ON TABLE %s IS %s;", tableFullyQualifiedName,
                    tableOptionsStrings.get(commentIndex + 2));
        }

        /**
         * 生成目标sql：列注释
         */
        List<String> columnComments = extractColumnCommentSql(tableFullyQualifiedName, columnDefinitions);

        /**
         * 获取主键
         */
        Index primaryKey = createTable.getIndexes().stream()
                .filter((Index index) -> Objects.equals("PRIMARY KEY", index.getType()))
                .findFirst().orElse(null);
        if (primaryKey == null) {
            throw new RuntimeException("Primary key not found");
        }

        /**
         * 生成目标sql：第一行的建表语句
         */
        String createTableFirstLine = String.format("CREATE TABLE %s (", tableFullyQualifiedName);

        /**
         * 生成目标sql：主键
         */
        String primaryKeyColumnSql = generatePrimaryKeySql(columnDefinitions, primaryKey);
        /**
         * 生成目标sql：除了主键之外的其他列
         */
        List<String> otherColumnSqlList = generateOtherColumnSql(columnDefinitions, primaryKey);


        String fullSql = generateFullSql(createTableFirstLine, primaryKeyColumnSql, otherColumnSqlList,
                tableCommentSql, columnComments);

        return fullSql;
    }


    private static String generatePrimaryKeySql(List<ColumnDefinition> columnDefinitions, Index primaryKey) {
        // 仅支持单列主键，不支持多列联合主键
        String primaryKeyColumnName = primaryKey.getColumnsNames().get(0);

        ColumnDefinition primaryKeyColumnDefinition = columnDefinitions.stream()
                .filter((ColumnDefinition column) -> column.getColumnName().equals(primaryKeyColumnName))
                .findFirst().orElse(null);
        if (primaryKeyColumnDefinition == null) {
            throw new RuntimeException();
        }
        String primaryKeyType = null;
        String dataType = primaryKeyColumnDefinition.getColDataType().getDataType();
        if (Objects.equals("bigint", dataType)) {
            primaryKeyType = "bigserial";
        } else if (Objects.equals("int", dataType)) {
            primaryKeyType = "serial";
        } else if (Objects.equals("varchar", dataType)) {
            primaryKeyType = primaryKeyColumnDefinition.getColDataType().toString();
        }

        String sql = String.format("%s %s PRIMARY KEY", primaryKeyColumnName, primaryKeyType);

        return sql;
    }

    private List<String> extractColumnCommentSql(String tableFullyQualifiedName, List<ColumnDefinition> columnDefinitions) {
        List<String> columnComments = new ArrayList<>();
        columnDefinitions
                .forEach((ColumnDefinition columnDefinition) -> {
                    List<String> columnSpecStrings = columnDefinition.getColumnSpecs();

                    int commentIndex = getCommentIndex(columnSpecStrings);
                    if (commentIndex != -1) {
                        int commentStringIndex = commentIndex + 1;
                        String commentString = columnSpecStrings.get(commentStringIndex);

                        String commentSql = genCommentSql(tableFullyQualifiedName, camelToUnderscore(columnDefinition.getColumnName()), commentString);
                        columnComments.add(commentSql);

                        columnSpecStrings.remove(commentStringIndex);
                        columnSpecStrings.remove(commentIndex);
                    }
                });

        return columnComments;
    }

    public static int getCommentIndex(List<String> columnSpecStrings) {
        for (int i = 0; i < columnSpecStrings.size(); i++) {
            if ("COMMENT".equalsIgnoreCase(columnSpecStrings.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static String genCommentSql(String table, String column, String commentValue) {
        return String.format("COMMENT ON COLUMN %s.%s IS %s;", table, column, commentValue);
    }

    /**
     * 获取表的列信息
     */
    public List<String> getTableColumns(String tableName, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query("SHOW COLUMNS FROM " + tableName, (rs, rowNum) -> rs.getString("Field"));
    }

    /**
     * 获取表的列信息 _ mysql
     */
    @DS("mysql")
    public List<String> getMysqlTableColumns(String tableName, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query("SHOW COLUMNS FROM " + tableName, (rs, rowNum) -> rs.getString("Field"));
    }

    /**
     * 获取表的列信息 _ pgsql
     */
    @DS("postgresql")
    public List<String> getPgTableColumns(String tableName, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query("SHOW COLUMNS FROM " + tableName, (rs, rowNum) -> rs.getString("Field"));
    }

    /**
     * 构造 SELECT 语句
     */
    public String buildSelectSql(String tableName, List<String> columns) {
        return "SELECT " + String.join(", ", columns) + " FROM " + tableName;
    }

    public String buildSelectSql(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    /**
     * 构造 INSERT 语句
     */
    public String buildInsertSql(String tableName, List<String> columns) {
        String placeholders = String.join(", ", Collections.nCopies(columns.size(), "?"));
        return "INSERT INTO " + tableName + " (" + String.join(", ", columns) + ") VALUES (" + placeholders + ")";
    }


    private List<String> generateOtherColumnSql(List<ColumnDefinition> columnDefinitions, Index primaryKey) {
        String primaryKeyColumnName = primaryKey.getColumnsNames().get(0);

        List<ColumnDefinition> columnDefinitionList = columnDefinitions.stream()
                .filter((ColumnDefinition column) -> !Objects.equals(column.getColumnName(), primaryKeyColumnName))
                .collect(Collectors.toList());

        List<String> sqlList = new ArrayList<String>();
        for (ColumnDefinition columnDefinition : columnDefinitionList) {
            // 列名
            String columnName = columnDefinition.getColumnName();
            columnName = camelToUnderscore(columnName);
            // 类型
            String dataType = columnDefinition.getColDataType().getDataType();
            String postgreDataType = DataTypeMapping.MYSQL_TYPE_TO_POSTGRE_TYPE.get(dataType);
            if (postgreDataType == null) {
                System.out.println(columnDefinition.getColDataType().getArgumentsStringList());
                throw new UnsupportedOperationException("mysql dataType not supported yet. " + dataType);
            }
            // 获取类型后的参数，如varchar(512)中，将获取到512
            List<String> argumentsStringList = columnDefinition.getColDataType().getArgumentsStringList();
            String argument = null;
            if (argumentsStringList != null && argumentsStringList.size() != 0) {
                if (argumentsStringList.size() == 1) {
                    argument = argumentsStringList.get(0);
                } else if (argumentsStringList.size() == 2) {
                    argument = argumentsStringList.get(0) + "," + argumentsStringList.get(1);
                }
            }
            if (argument != null && argument.trim().length() != 0) {
                if (postgreDataType.equalsIgnoreCase("bigint")
                        || postgreDataType.equalsIgnoreCase("smallint")
                        || postgreDataType.equalsIgnoreCase("int")
                ) {
                    postgreDataType = postgreDataType;
                } else {
                    postgreDataType = postgreDataType + "(" + argument + ")";
                }
            }

            // 处理默认值，将mysql中的默认值转为pg中的默认值，如mysql的CURRENT_TIMESTAMP转为
            List<String> specs = columnDefinition.getColumnSpecs();
            int indexOfDefaultItem = specs.indexOf("DEFAULT");
            if (indexOfDefaultItem != -1) {
                String mysqlDefault = specs.get(indexOfDefaultItem + 1);
                // 是字符串的情况下，内容可能是数字，也可能不是
                if (mysqlDefault.startsWith("'") && mysqlDefault.endsWith("'")) {
                    mysqlDefault = mysqlDefault.replaceAll("'", "");
                } else {
                    // 不是字符串的话，一般就是mysql中的函数，此时要查找对应的pg函数
                    String postgreDefault = DefaultValueMapping.MYSQL_DEFAULT_TO_POSTGRE_DEFAULT.get(mysqlDefault);
                    if (postgreDefault == null) {
                        throw new UnsupportedOperationException("not supported mysql default:" + mysqlDefault);
                    }
                    specs.set(indexOfDefaultItem + 1, postgreDefault);
                }
            }

            String sourceSpec = String.join(" ", specs);
            String targetSpecAboutNull = null;
            if (sourceSpec.contains("DEFAULT NULL")) {
                targetSpecAboutNull = "NULL";
                sourceSpec = sourceSpec.replaceAll("DEFAULT NULL", "");
            } else if (sourceSpec.contains("NOT NULL")) {
                targetSpecAboutNull = "NOT NULL";
                sourceSpec = sourceSpec.replaceAll("NOT NULL", "");
            }

            // postgre不支持unsigned
            sourceSpec = sourceSpec.replaceAll("unsigned", "");
            // postgre不支持ON UPDATE CURRENT_TIMESTAMP
            sourceSpec = sourceSpec.replaceAll("ON UPDATE CURRENT_TIMESTAMP", "");


            String sql;
            if (sourceSpec.trim().length() != 0) {
                sql = String.format("%s %s %s %s", columnName, postgreDataType, targetSpecAboutNull, sourceSpec.trim());
            } else {
                sql = String.format("%s %s %s", columnName, postgreDataType, targetSpecAboutNull);
            }
            sqlList.add(sql);
        }
        return sqlList;
    }

    private static String generateFullSql(String createTableFirstLine, String primaryKeyColumnSql,
                                          List<String> otherColumnSqlList,
                                          String tableCommentSql, List<String> columnComments) {
        StringBuilder builder = new StringBuilder();
        // 建表语句首行
        builder.append(createTableFirstLine)
                .append("\n");
        // 主键 须缩进
        builder.append("    ")
                .append(primaryKeyColumnSql)
                .append(",\n");

        // 每一列 缩进
        for (int i = 0; i < otherColumnSqlList.size(); i++) {
            if (i != otherColumnSqlList.size() - 1) {
                builder.append("    ").append(otherColumnSqlList.get(i)).append(",\n");
            } else {
                builder.append("    ").append(otherColumnSqlList.get(i)).append("\n");
            }
        }
        builder.append(");\n");

        // 表的注释
        if (tableCommentSql != null) {
            builder.append("\n" + tableCommentSql + "\n");
        }

        // 列的注释
        for (String columnComment : columnComments) {
            builder.append(columnComment).append("\n");
        }

        String sql = builder.toString();
        return sql;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    // todo 将通用流程抽象化，分别创建PG及其他数据库处理流程
    /** 
     * @description: 在PG数据库中生成表
     * @param: null 
     * @return:  
     * @author 15968
     * @date: 2025/3/18 22:46
     */ 
    public void generateTableInPg(DatasourceRequest.UseDDL generateDDLRequest) throws SQLException {

        Datasource targetDatasource = datasourceService.getByInfo(generateDDLRequest.getTargetType(), generateDDLRequest.getTargetDatasource());

        // todo 增加参数构造
        // todo 表名冲突 --> 强制创建
        DataSource ds = dynamicHelper.registerAndGet(new DatasourceRequest.GetConnection(targetDatasource.getType(), targetDatasource.getName(), targetDatasource.getConfiguration()));
        Connection conn = ds.getConnection();

        try (Statement statement = conn.createStatement()) {
            // 使用 executeUpdate 执行 DDL
            int rowsAffected = statement.executeUpdate(generateDDLRequest.getDdl());
            log.info("DDL executed successfully. Rows affected: {}", rowsAffected);
        } catch (SQLException e) {
            throw new DatasetExecuteException(ErrorCode.DATASET_GENERATE_ERROR, e.getMessage());
        }

    }

    public String camelToUnderscore(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
