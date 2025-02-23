package com.dbg.datawork.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbg.datawork.model.entity.TaskLog;
import com.dbg.datawork.service.TaskLogService;
import com.dbg.datawork.mapper.TaskLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 15968
* @description 针对表【task_log(定时任务日志)】的数据库操作Service实现
* @createDate 2025-02-22 22:27:50
*/
@Service
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog>
    implements TaskLogService{

}




