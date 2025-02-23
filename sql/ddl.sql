-- 数据集表
CREATE TABLE `table`
(
    `id`             BIGINT      NOT NULL COMMENT 'ID',
    `datasource_id`  BIGINT,
    `group_id`       BIGINT      NOT NULL,
    `layer`          VARCHAR(64) NOT NULL,
    `name`           VARCHAR(64) NOT NULL COMMENT '名称',
    `remark`         VARCHAR(64),
    `extract_type`   VARCHAR(64) NOT NULL,
    `data_synced`    TINYINT(1) NOT NULL,
    `partitioned`    TINYINT(1) NOT NULL COMMENT '是否分区',
    `info`           TEXT        NOT NULL,
    `last_sync_time` DATETIME(6),
    `created_by`     VARCHAR(50) NOT NULL COMMENT '创建者',
    `created_at`     DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by`     VARCHAR(50) NOT NULL COMMENT '更新者',
    `updated_at`     DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`        TINYINT(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) COMMENT='数据集表';

-- 表字段信息
CREATE TABLE `table_field`
(
    `id`             BIGINT      NOT NULL COMMENT 'ID',
    `table_id`       BIGINT      NOT NULL COMMENT '表id',
    `origin_name`    VARCHAR(64),
    `name`           VARCHAR(64) NOT NULL COMMENT '名称',
    `remark`         VARCHAR(64),
    `type`           VARCHAR(64) NOT NULL COMMENT '类型',
    `partitioned`    TINYINT(1) NOT NULL COMMENT '是否为分区键',
    `primary_key`    TINYINT(1) NOT NULL COMMENT '是否为主键',
    `size`           INT,
    `column_index`   INT         NOT NULL,
    `last_sync_time` DATETIME(6),
    `checked`        TINYINT(1) NOT NULL,
    `created_by`     VARCHAR(50) NOT NULL COMMENT '创建者',
    `created_at`     DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by`     VARCHAR(50) NOT NULL COMMENT '更新者',
    `updated_at`     DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`        TINYINT(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) COMMENT='表字段信息';

-- 表SQL日志
CREATE TABLE `table_sql_log`
(
    `id`         BIGINT      NOT NULL COMMENT 'ID',
    `table_id`   BIGINT      NOT NULL COMMENT '表id',
    `action`     VARCHAR(50) NOT NULL COMMENT '行为',
    `before`     TEXT,
    `after`      TEXT        NOT NULL COMMENT '操作后数据',
    `created_by` VARCHAR(50) NOT NULL COMMENT '创建者',
    `created_at` DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by` VARCHAR(50) NOT NULL COMMENT '更新者',
    `updated_at` DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`    TINYINT(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) COMMENT='表SQL日志';

-- 定时任务
CREATE TABLE `task`
(
    `id`              BIGINT       NOT NULL COMMENT 'ID',
    `table_id`        BIGINT       NOT NULL COMMENT '表id(外键)',
    `layer`           VARCHAR(64)  NOT NULL,
    `status`          VARCHAR(64)  NOT NULL,
    `sync_type`       VARCHAR(64)  NOT NULL COMMENT '任务同步类型',
    `sync_time_range` VARCHAR(64)  NOT NULL,
    `sync_data_range` VARCHAR(64)  NOT NULL,
    `extract_type`    VARCHAR(64)  NOT NULL,
    `partitioned`     TINYINT(1) NOT NULL COMMENT '是否为分区表',
    `handler`         VARCHAR(64),
    `name`            VARCHAR(64)  NOT NULL COMMENT '名称',
    `remark`          VARCHAR(255) NOT NULL COMMENT '备注',
    `cron`            VARCHAR(64),
    `created_by`      VARCHAR(50)  NOT NULL COMMENT '创建者',
    `created_at`      DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by`      VARCHAR(50)  NOT NULL COMMENT '更新者',
    `updated_at`      DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`         TINYINT(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) COMMENT='定时任务';

-- 数据源
CREATE TABLE `datasource`
(
    `id`            BIGINT      NOT NULL COMMENT 'ID',
    `type`          VARCHAR(64) NOT NULL COMMENT '数据源类型',
    `name`          VARCHAR(64) NOT NULL COMMENT '名称',
    `status`        VARCHAR(64) NOT NULL COMMENT '状态',
    `remark`        VARCHAR(64) NOT NULL COMMENT '备注',
    `configuration` TEXT        NOT NULL COMMENT '配置信息',
    `created_by`    VARCHAR(50) NOT NULL COMMENT '创建者',
    `created_at`    DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by`    VARCHAR(50) NOT NULL COMMENT '更新者',
    `updated_at`    DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`       TINYINT(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) COMMENT='数据源';

-- 定时任务日志
CREATE TABLE `task_log`
(
    `id`              BIGINT      NOT NULL COMMENT 'ID',
    `table_id`        BIGINT      NOT NULL COMMENT '表id(外键)',
    `task_id`         BIGINT      NOT NULL COMMENT '任务id(外键)',
    `task_name`       VARCHAR(64) NOT NULL COMMENT '任务名称',
    `layer`           VARCHAR(64) NOT NULL,
    `status`          VARCHAR(64) NOT NULL COMMENT '状态',
    `message`         TEXT COMMENT '任务详细日志',
    `sync_time_range` VARCHAR(64),
    `sync_data_range` VARCHAR(64),
    `extract_type`    VARCHAR(64) NOT NULL,
    `partitioned`     TINYINT(1) NOT NULL COMMENT '是否为分区表',
    `start_time`      DATETIME(6) NOT NULL COMMENT '开始时间',
    `end_time`        DATETIME(6) COMMENT '结束时间',
    `created_by`      VARCHAR(50) NOT NULL COMMENT '创建者',
    `created_at`      DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by`      VARCHAR(50) NOT NULL COMMENT '更新者',
    `updated_at`      DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`         TINYINT(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) COMMENT='定时任务日志';

-- 数据库校验表
CREATE TABLE `table_check`
(
    `id`                   BIGINT       NOT NULL COMMENT 'ID',
    `source_datasource_id` BIGINT       NOT NULL COMMENT '原数据库',
    `target_datasource_id` BIGINT       NOT NULL COMMENT '目标数据库',
    `name`                 VARCHAR(64)  NOT NULL COMMENT '名称',
    `remark`               VARCHAR(512) COMMENT '备注',
    `time_range`           VARCHAR(64)  NOT NULL COMMENT '时间区间',
    `source_schema`        VARCHAR(255) NOT NULL COMMENT '源schema',
    `source_table`         VARCHAR(255) NOT NULL COMMENT '源表名',
    `source_time_field`    VARCHAR(255) NOT NULL COMMENT '源表时间字段',
    `target_schema`        VARCHAR(255) NOT NULL COMMENT '目标schema',
    `target_table`         VARCHAR(255) NOT NULL COMMENT '目标表名',
    `target_time_field`    VARCHAR(255) NOT NULL COMMENT '目标时间字段',
    `created_by`           VARCHAR(50)  NOT NULL COMMENT '创建者',
    `created_at`           DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by`           VARCHAR(50)  NOT NULL COMMENT '更新者',
    `updated_at`           DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`              TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) COMMENT='数据库校验';

-- 数据库校验日志表
CREATE TABLE `table_check_log`
(
    `id`               BIGINT      NOT NULL COMMENT 'ID',
    `table_check_id`   BIGINT      NOT NULL COMMENT 'tableCheck外键',
    `table_check_name` VARCHAR(64) NOT NULL COMMENT 'tableCheck名称',
    `status`           VARCHAR(64) NOT NULL COMMENT '状态',
    `source_row`       INT COMMENT '源表数据量',
    `target_row`       INT COMMENT '目标表数据量',
    `time_range`       VARCHAR(64) NOT NULL COMMENT '时间区间',
    `message`          TEXT COMMENT '详细消息',
    `source_sql`       TEXT        NOT NULL COMMENT '源数据SQL',
    `target_sql`       TEXT        NOT NULL COMMENT '目标数据库SQL',
    `begin_time`       DATETIME(6) NOT NULL COMMENT '开始时间',
    `end_time`         DATETIME(6) NOT NULL COMMENT '结束时间',
    `created_by`       VARCHAR(50) NOT NULL COMMENT '创建者',
    `created_at`       DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by`       VARCHAR(50) NOT NULL COMMENT '更新者',
    `updated_at`       DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`          TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) COMMENT='数据库校验日志';

-- 数据校验表
CREATE TABLE `quality_check`
(
    `id`                   BIGINT      NOT NULL COMMENT 'ID',
    `source_datasource_id` BIGINT COMMENT '源数据库id',
    `target_datasource_id` BIGINT COMMENT '目标数据库id',
    `name`                 VARCHAR(64) NOT NULL COMMENT '名称',
    `remark`               VARCHAR(64) NOT NULL COMMENT '备注',
    `type`                 VARCHAR(64) NOT NULL COMMENT '类型:DDL(DDL校验),ROWS(行数校验)',
    `rule`                 LONGTEXT    NOT NULL COMMENT '校验规则JSON',
    `created_by`           VARCHAR(50) NOT NULL COMMENT '创建者',
    `created_at`           DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by`           VARCHAR(50) NOT NULL COMMENT '更新者',
    `updated_at`           DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`              TINYINT(1) NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) COMMENT='数据校验';

-- 数据校验日志表
CREATE TABLE `quality_check_log`
(
    `id`                 BIGINT       NOT NULL COMMENT 'ID',
    `quality_check_id`   BIGINT       NOT NULL COMMENT '质量校验id',
    `quality_check_name` VARCHAR(255) NOT NULL COMMENT '质量校验名称',
    `key`                VARCHAR(64)  NOT NULL COMMENT 'key',
    `status`             VARCHAR(64)  NOT NULL COMMENT '状态',
    `content`            LONGTEXT COMMENT '校验信息',
    `message`            VARCHAR(2550) COMMENT '失败消息',
    `created_by`         VARCHAR(50)  NOT NULL COMMENT '创建者',
    `created_at`         DATETIME(6) NOT NULL COMMENT '创建时间',
    `updated_by`         VARCHAR(50)  NOT NULL COMMENT '更新者',
    `updated_at`         DATETIME(6) NOT NULL COMMENT '更新时间',
    `deleted`            TINYINT(1) NOT NULL COMMENT '逻辑删除',
    `quality_check_type` VARCHAR(255) NOT NULL DEFAULT 'DDL' COMMENT '质量校验类型',
    PRIMARY KEY (`id`)
) COMMENT='数据校验日志';