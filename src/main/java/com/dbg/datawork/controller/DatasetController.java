package com.dbg.datawork.controller;



import com.dbg.datawork.dataset.service.TableStructService;
import com.dbg.datawork.dataset.service.impl.DatasetServiceImpl;

import com.dbg.datawork.dataset.service.impl.TableStructServiceImpl;
import com.dbg.datawork.infra.common.BaseResponse;
import com.dbg.datawork.infra.common.ErrorCode;
import com.dbg.datawork.infra.common.ResultUtils;
import com.dbg.datawork.model.dto.dataset.DatasetRequest;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.pojo.DataMigrationTask;
import com.dbg.datawork.model.pojo.TableStructTransferTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/24 2:42
 */
@Slf4j
@RestController
@RequestMapping("/dataset")
public class DatasetController {

    @Resource
    private TableStructService tableStructService;

    @Resource
    private DatasetServiceImpl datasetService;



    /**
     * @description: 获取Mysql数据源的DDL
     * @author 15968
     * @date 2025/3/17 18:34
     * @version 1.0
     */
    @PostMapping("/ddl/get")
    public BaseResponse<String> getMysqlDDL(@RequestBody DatasourceRequest.GetSourceDDL datasourceRequest) {
        String tableDDL = tableStructService.getTableDDL(datasourceRequest);
        return ResultUtils.success(tableDDL);
    }

    @PostMapping("/ddl/transfer")
    public BaseResponse<String> transferDDL(@RequestBody DatasourceRequest.GetTransferDDL transferDDLRequest) {
        String generateTable = tableStructService.transferTable(transferDDLRequest);
        return ResultUtils.success(generateTable);
    }

    @PostMapping("/ddl/generate")
    public BaseResponse<Boolean> generateDDL(@RequestBody DatasourceRequest.UseDDL generateTargetDDL) {
        tableStructService.createTable(generateTargetDDL);
        return ResultUtils.success(true);
    }

    @PostMapping("/ddl/task/add")
    public BaseResponse<TableStructTransferTask> addTask(@RequestBody DatasourceRequest.AddTaskRequest addTaskRequest) {
        TableStructTransferTask task = tableStructService.addTask(addTaskRequest);
        return ResultUtils.success(task);
    }
    // public BaseResponse<Page<TableStructTransferVo>> pageTableStructTransferVo(@RequestBody DatasetRequest.GetTableStructTransfer getTableStructTransfer) {
    //
    //     tableStructService.
    //
    // }

    // public BaseResponse<Boolean>

    // @PostMapping("ddl/transfer")
    // public BaseResponse<Boolean> transferTable(@RequestBody String tableName) {
    //     try {
    //         tableStructService.migrateTable(tableName);
    //     } catch (Exception e) {
    //         log.error(e.getMessage());
    //         return ResultUtils.error(ErrorCode.OPERATION_ERROR);
    //     }
    //     return ResultUtils.success(true);
    // }

    // todo 优化数据迁移 使用 ETL工具（Spring Batch） /  Debezium + Kafka
    @PostMapping("/data/transform")
    public BaseResponse<Boolean> transform(@RequestBody DatasetRequest.TransformDataRequest transformDataRequest) {

        try {
            datasetService.transformData(transformDataRequest);
            return ResultUtils.success(true);
        } catch (SQLException e) {
            return ResultUtils.error(ErrorCode.DATASET_INSERT_ERROR, e.getMessage());
        }
    }

    @PostMapping("/data/task/add")
    public BaseResponse<DataMigrationTask> addDataMigrationTask(@RequestBody DatasetRequest.AddDataMigrationTaskRequest addDataMigrationTaskRequest) {
        DataMigrationTask dataMigrationTask = datasetService.addDataMigrationTask(addDataMigrationTaskRequest);
        return ResultUtils.success(dataMigrationTask);
    }

}
