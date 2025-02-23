package com.dbg.datawork.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.entity.Datasource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 15968
* @description 针对表【datasource(数据源)】的数据库操作Service
* @createDate 2025-02-22 22:32:57
*/
public interface DatasourceService extends IService<Datasource> {

    Page<Datasource> getDatasourcePage(DatasourceRequest datasourceRequest);

    QueryWrapper<Datasource> getQueryWrapper(DatasourceRequest datasourceRequest);

    Long addDatasource(DatasourceRequest dasourceRequest);
}
