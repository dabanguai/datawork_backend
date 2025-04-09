package com.dbg.datawork.dataset.service;

import com.dbg.datawork.model.dto.dataset.DatasetRequest;
import com.dbg.datawork.model.pojo.DataMigrationTask;

import java.sql.SQLException;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/17 18:42
 */
public interface DatasetService {


    /** 
     * @description: 迁移数据 
     * @param: transformDataRequest 
     * @return: void 
     * @author 15968
     * @date: 2025/3/17 19:00
     */ 
    void transformData(DatasetRequest.TransformDataRequest transformDataRequest) throws SQLException;

    DataMigrationTask addDataMigrationTask(DatasetRequest.AddDataMigrationTaskRequest addDataMigrationTaskRequest);
}
