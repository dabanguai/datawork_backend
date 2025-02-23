package com.dbg.datawork.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbg.datawork.model.entity.Task;
import com.dbg.datawork.service.TaskService;
import com.dbg.datawork.mapper.TaskMapper;
import org.springframework.stereotype.Service;

/**
* @author 15968
* @description 针对表【task(定时任务)】的数据库操作Service实现
* @createDate 2025-02-22 22:27:46
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
    implements TaskService{

}




