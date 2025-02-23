package com.dbg.datawork.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbg.datawork.common.ErrorCode;
import com.dbg.datawork.constant.CommonConstant;
import com.dbg.datawork.exception.ThrowUtils;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.entity.Datasource;
import com.dbg.datawork.service.DatasourceService;
import com.dbg.datawork.mapper.DatasourceMapper;
import com.dbg.datawork.service.UserService;
import com.dbg.datawork.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 15968
 * @description 针对表【datasource(数据源)】的数据库操作Service实现
 * @createDate 2025-02-22 22:32:57
 */
@Service
public class DatasourceServiceImpl extends ServiceImpl<DatasourceMapper, Datasource>
        implements DatasourceService {

    @Resource
    private UserService userService;
    @Override
    public Page<Datasource> getDatasourcePage(DatasourceRequest datasourceRequest) {
        Page<Datasource> datasourcePage = new Page<>(datasourceRequest.getCurrent(), datasourceRequest.getPageSize());
        QueryWrapper<Datasource> queryWrapper = getQueryWrapper(datasourceRequest);
        return page(datasourcePage, queryWrapper);
    }

    @Override
    public Long addDatasource(DatasourceRequest datasourceRequest) {
        Datasource datasource = new Datasource();
        BeanUtils.copyProperties(datasourceRequest, datasource);

        if (StringUtils.isNotEmpty(datasourceRequest.getConfiguration())) {
            datasource.setConfiguration(datasourceRequest.getConfiguration());
        }
        if (StringUtils.isNotEmpty(datasourceRequest.getType())) {
            datasource.setType(datasourceRequest.getType());
        }
        if (StringUtils.isNotEmpty(datasourceRequest.getName())) {
            datasource.setName(datasourceRequest.getName());
        }
        // todo 增加数据源校验及身份校验
        boolean isAdd = save(datasource);
        ThrowUtils.throwIf(!isAdd, ErrorCode.OPERATION_ERROR);
        return datasource.getId();
    }

    public QueryWrapper<Datasource> getQueryWrapper(DatasourceRequest datasourceRequest) {

        QueryWrapper<Datasource> queryWrapper = new QueryWrapper<>();

        if (datasourceRequest == null) {
            return queryWrapper;
        }

        String sortField = datasourceRequest.getSortField();
        String sortOrder = datasourceRequest.getSortOrder();

        queryWrapper.like(StringUtils.isNotBlank(datasourceRequest.getType()), "type", datasourceRequest.getType());
        queryWrapper.like(StringUtils.isNotBlank(datasourceRequest.getName()), "name", datasourceRequest.getName());

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




