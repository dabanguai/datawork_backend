package com.dbg.datawork.controller;

import com.dbg.datawork.dataCheck.DataCheckService;
import com.dbg.datawork.infra.common.BaseResponse;
import com.dbg.datawork.infra.common.ResultUtils;
import com.dbg.datawork.model.dto.dataCheck.DataCheckRequest;
import com.dbg.datawork.model.entity.ColumnCompareResult;
import com.dbg.datawork.model.entity.DataAmountCompareResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/31 10:57
 */
@Slf4j
@RestController
@RequestMapping("/check")
public class DataCheckController {

    @Resource
    private DataCheckService  dataCheckService;

    @PostMapping("/ddl")
    public BaseResponse<List<ColumnCompareResult>> checkDDL(@RequestBody DataCheckRequest.CompareDDLRequest compareDDLRequest) {

        List<ColumnCompareResult> columnCompareResults = dataCheckService.ddlCheck(compareDDLRequest);
        return ResultUtils.success(columnCompareResults);

    }

    @PostMapping("/amount")
    public BaseResponse<List<DataAmountCompareResult>> checkDataAmount(@RequestBody DataCheckRequest.CompareDataAmountRequest compareDataAmountRequest) {
        List<DataAmountCompareResult> amountCompareResultList = dataCheckService.amountCheck(compareDataAmountRequest);
        return ResultUtils.success(amountCompareResultList);
    }

}
