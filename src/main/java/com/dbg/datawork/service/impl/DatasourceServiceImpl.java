package com.dbg.datawork.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.dbg.datawork.infra.common.ErrorCode;
import com.dbg.datawork.infra.constant.CommonConstant;
import com.dbg.datawork.exception.BusinessException;
import com.dbg.datawork.exception.ThrowUtils;
import com.dbg.datawork.mapper.DatasourceMapper;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.pojo.Datasource;
import com.dbg.datawork.service.DatasourceService;
import com.dbg.datawork.service.UserService;
import com.dbg.datawork.infra.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 15968
 * @description 针对表【datasource(数据源)】的数据库操作Service实现
 * @createDate 2025-02-22 22:32:57
 */
@Service
@DS("mysql")
public class DatasourceServiceImpl extends ServiceImpl<DatasourceMapper, Datasource>
        implements DatasourceService {

    @Resource
    private UserService userService;

    // todo 划分 查询和业务逻辑
    @Override
    public Page<Datasource> getDatasourcePage(DatasourceRequest datasourceRequest) {
        Page<Datasource> datasourcePage = new Page<>(datasourceRequest.getCurrent(), datasourceRequest.getPageSize());
        QueryWrapper<Datasource> queryWrapper = getQueryWrapper(datasourceRequest);
        return page(datasourcePage, queryWrapper);
    }

    @Override
    public Datasource addDatasource(DatasourceRequest.AddDatasourceDto datasourceRequest) {
        Datasource datasource = new Datasource();
        BeanUtil.copyProperties(datasourceRequest, datasource, CopyOptions.create().setIgnoreNullValue(true));

        if (StringUtils.isEmpty(datasourceRequest.getConfiguration())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "配置信息不能为空");
            // datasource.setConfiguration(datasourceRequest.getConfiguration());
        }
        if (StringUtils.isEmpty(datasourceRequest.getType())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "数据源类型不能为空");
        }
        if (StringUtils.isEmpty(datasourceRequest.getName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "数据源名称不能为空");
        }
        // todo 增加数据源校验及身份校验
        boolean isAdd = save(datasource);
        ThrowUtils.throwIf(!isAdd, ErrorCode.OPERATION_ERROR);
        return datasource;
    }


    // todo 使用生成查询
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

    public Datasource getByInfo(String type, String databaseName) {
        LambdaQueryWrapper<Datasource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Datasource::getType, type);
        queryWrapper.eq(Datasource::getName, databaseName);
        Datasource datasource = getOne(queryWrapper);
        return datasource;
    }

    @Override
    public Datasource updateDatasource(DatasourceRequest.UpdateDatasourceDto datasourceRequest) {
        String id = datasourceRequest.getId();
        if (StrUtil.isEmpty(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未传入数据源");
        }

        Datasource datasource = getById(id);
        datasource.setType(datasourceRequest.getType());
        datasource.setName(datasourceRequest.getName());
        datasource.setConfiguration(datasourceRequest.getConfiguration());

        updateById(datasource);
        return datasource;
    }

}




