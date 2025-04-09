package com.dbg.datawork.controller;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/2/23 17:05
 */

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbg.datawork.exception.BusinessException;
import com.dbg.datawork.exception.ThrowUtils;
import com.dbg.datawork.infra.common.BaseResponse;
import com.dbg.datawork.infra.common.DeleteRequest;
import com.dbg.datawork.infra.common.ErrorCode;
import com.dbg.datawork.infra.common.ResultUtils;
import com.dbg.datawork.model.dto.datasource.DatasourceRequest;
import com.dbg.datawork.model.dto.datasource.TableRequest;
import com.dbg.datawork.model.pojo.Datasource;
import com.dbg.datawork.model.pojo.Table;
import com.dbg.datawork.model.vo.DatasourceTableCountVo;
import com.dbg.datawork.service.TableService;
import com.dbg.datawork.service.UserService;
import com.dbg.datawork.service.impl.DatasourceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/datasource")
@Slf4j
public class DatasourceController {

    @Resource
    private DatasourceServiceImpl datasourceService;

    @Resource
    private TableService tableService;

    /**
     * @param datasourceRequest
     * @return
     * @author 15968
     * @date 2025/2/23 23:33
     * @version 1.0
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
        // User user = userService.getLoginUser(request);
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
    public BaseResponse<Datasource> addDatasource(@RequestBody DatasourceRequest.AddDatasourceDto datasourceRequest, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(datasourceRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 增加权限
        // User loginUser = userService.getLoginUser(request);
        Datasource datasource = datasourceService.addDatasource(datasourceRequest);

        return ResultUtils.success(datasource);
    }

    @PutMapping("/update")
    public BaseResponse<Datasource> updateDatasource(@RequestBody DatasourceRequest.UpdateDatasourceDto datasourceRequest, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(datasourceRequest)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 增加权限
        // User loginUser = userService.getLoginUser(request);
        Datasource datasource = datasourceService.updateDatasource(datasourceRequest);
        return ResultUtils.success(datasource);
    }

    /**
     * 表相关操作
     */
    @PostMapping("/table/add")
    public BaseResponse<Table> addTable(@RequestBody TableRequest.TableRequestDto tableRequestDto) {
        if (null != tableRequestDto && ObjUtil.isAllNotEmpty(tableRequestDto)) {
            Table table = tableService.addTable(tableRequestDto);
            return ResultUtils.success(table);
        }
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, "添加表失败，参数错误");
    }

    @GetMapping("/table/count")
    public BaseResponse<Long> countTable() {
        Long tableCount = tableService.countTables();
        return ResultUtils.success(ObjUtil.isNotNull(tableCount) ? tableCount : 0L);
    }

    @GetMapping("/table/count/{datasourceId}")
    public BaseResponse<List<DatasourceTableCountVo> > countTablesByDatasourceId(@PathVariable Long datasourceId) {
        List<DatasourceTableCountVo> datasourceToTables = tableService.countTablesByDatasourceId(datasourceId);
        return ResultUtils.success(datasourceToTables);
    }
}
