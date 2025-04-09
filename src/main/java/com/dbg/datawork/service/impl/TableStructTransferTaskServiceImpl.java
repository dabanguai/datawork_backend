package com.dbg.datawork.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbg.datawork.mapper.TableStructTransferTaskMapper;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.pojo.Datasource;
import com.dbg.datawork.model.pojo.TableStructTransferTask;
import com.dbg.datawork.service.DatasourceService;
import com.dbg.datawork.service.TableStructTransferTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 15968
* @description 针对表【table_struct_transfer_task】的数据库操作Service实现
* @createDate 2025-03-23 13:43:38
*/
@Service
public class TableStructTransferTaskServiceImpl extends ServiceImpl<TableStructTransferTaskMapper, TableStructTransferTask>
    implements TableStructTransferTaskService {

    @Resource
    private DatasourceService datasourceService;

    @Override
    public TableStructTransferTask addTask(DatasourceRequest.AddTaskRequest addTaskRequest) {
        TableStructTransferTask task = new TableStructTransferTask();
        Datasource src = datasourceService.getByInfo(addTaskRequest.getOriginalDatabaseType(), addTaskRequest.getOriginalDatabaseName());
        Datasource tgt = datasourceService.getByInfo(addTaskRequest.getTargetDatabaseType(), addTaskRequest.getTargetDatabaseName());

        task.setOriginalDatabaseId(src.getId());
        task.setOriginalDatabaseName(src.getName());
        task.setOriginalTableName(addTaskRequest.getOriginalTableName());
        task.setTargetDatabaseId(tgt.getId());
        task.setTargetDatabaseName(tgt.getName());
        task.setSchema(ObjUtil.isNotNull(addTaskRequest.getSchema()) ? addTaskRequest.getSchema() : "public");
        task.setTargetTableName(addTaskRequest.getTargetTableName());
        task.setTargetDDL(addTaskRequest.getTargetDDL());
        save(task);
        return task;
    }
}




