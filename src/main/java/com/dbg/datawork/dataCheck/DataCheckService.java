package com.dbg.datawork.dataCheck;

import com.dbg.datawork.model.dto.dataCheck.DataCheckRequest;
import com.dbg.datawork.model.entity.ColumnCompareResult;
import com.dbg.datawork.model.entity.DataAmountCompareResult;

import java.util.List;

public interface DataCheckService {
    List<ColumnCompareResult> ddlCheck(DataCheckRequest.CompareDDLRequest compareDDLRequest);

    List<DataAmountCompareResult> amountCheck(DataCheckRequest.CompareDataAmountRequest compareDataAmountRequest);
}
