package com.dbg.datawork.dataset.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.pojo.TableStructTransferTask;
import org.springframework.stereotype.Service;

/**
 * @description: 表结构同步接口
 * @author 15968
 * @date 2025/2/24 16:33
 * @version 1.0
 */
@Service
public interface TableStructService {
    String getTableDDL(DatasourceRequest.GetSourceDDL datasourceRequest);

    String transferTable(DatasourceRequest.GetTransferDDL transferDDLRequest);

    @DS("mysql")
    void createTable(DatasourceRequest.UseDDL createRequest);

    TableStructTransferTask addTask(DatasourceRequest.AddTaskRequest addTaskRequest);
}
