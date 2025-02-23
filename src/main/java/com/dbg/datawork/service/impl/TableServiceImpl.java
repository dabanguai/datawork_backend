package com.dbg.datawork.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbg.datawork.model.entity.Table;
import com.dbg.datawork.service.TableService;
import com.dbg.datawork.mapper.TableMapper;
import org.springframework.stereotype.Service;

/**
* @author 15968
* @description 针对表【table(数据集表)】的数据库操作Service实现
* @createDate 2025-02-22 22:27:23
*/
@Service
public class TableServiceImpl extends ServiceImpl<TableMapper, Table>
    implements TableService{

}




