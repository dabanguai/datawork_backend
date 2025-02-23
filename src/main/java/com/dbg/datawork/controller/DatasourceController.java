package com.dbg.datawork.controller;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/23 17:05
 */

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbg.datawork.common.BaseResponse;
import com.dbg.datawork.common.DeleteRequest;
import com.dbg.datawork.common.ErrorCode;
import com.dbg.datawork.common.ResultUtils;
import com.dbg.datawork.exception.BusinessException;
import com.dbg.datawork.exception.ThrowUtils;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.entity.Datasource;
import com.dbg.datawork.model.entity.User;
import com.dbg.datawork.service.UserService;
import com.dbg.datawork.service.impl.DatasourceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/datasource")
@Slf4j
public class DatasourceController {

    @Resource
    private DatasourceServiceImpl datasourceService;

    @Resource
    private UserService userService;

    /**
     * @author 15968
     * @date 2025/2/23 23:33
     * @version 1.0
     * @param datasourceRequest
     * @return
     */
    @PostMapping("/page")
    public BaseResponse<Page<Datasource>> datasourcePage(@RequestBody DatasourceRequest datasourceRequest, HttpServletRequest request) {
        Page<Datasource> datasourcePage = datasourceService.getDatasourcePage(datasourceRequest);
        log.info("datasourcePage:{}", datasourcePage);
        return ResultUtils.success(datasourcePage);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDatasource(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Datasource dataSource = datasourceService.getById(id);
        ThrowUtils.throwIf(dataSource == null, ErrorCode.NOT_FOUND_ERROR);
        // todo 仅本人或管理员可删除
        // if (!dataSource.getCreatedBy().equals(user.getId()) && !userService.isAdmin(request)) {
        //     throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        // }
        boolean isDelete = datasourceService.removeById(id);
        return ResultUtils.success(isDelete);
    }

    @PostMapping("/add")
    public BaseResponse<Long> addDatasource(@RequestBody DatasourceRequest datasourceRequest, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(datasourceRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 增加权限
        User loginUser = userService.getLoginUser(request);
        Long datasourceId = datasourceService.addDatasource(datasourceRequest);

        return ResultUtils.success(datasourceId);
    }
}
