package com.dbg.datawork.service;

import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.pojo.TableStructTransferTask;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
* @author 15968
* @description 针对表【table_struct_transfer_task】的数据库操作Service
* @createDate 2025-03-23 13:43:38
*/
@Service
public interface TableStructTransferTaskService extends IService<TableStructTransferTask> {
    TableStructTransferTask addTask(DatasourceRequest.AddTaskRequest addTaskRequest);

}
