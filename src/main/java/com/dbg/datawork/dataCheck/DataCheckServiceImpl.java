package com.dbg.datawork.dataCheck;

import com.dbg.datawork.model.dto.dataCheck.DataCheckDTO;
import com.dbg.datawork.model.dto.dataCheck.DataCheckRequest;
import com.dbg.datawork.model.entity.ColumnCompareResult;
import com.dbg.datawork.model.entity.DataAmountCompareResult;
import com.dbg.datawork.service.ColumnComparator;
import com.dbg.datawork.service.DataAmountComparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/31 15:59
 */
@Slf4j
@Service
public class DataCheckServiceImpl implements DataCheckService {

    @Resource
    private ColumnComparator columnComparator;

    @Resource
    private DataAmountComparator amountComparator;


    @Override
    public List<ColumnCompareResult> ddlCheck(DataCheckRequest.CompareDDLRequest compareDDLRequest) {

        List<DataCheckDTO> dataCheckDTOList = compareDDLRequest.getDataCheckDTOList();
        List<ColumnCompareResult> columnCompareResults = null;
        for (DataCheckDTO dataCheckDTO : dataCheckDTOList) {
            columnCompareResults = columnComparator.compareColumns(dataCheckDTO, compareDDLRequest.getSourceDataBaseType(), compareDDLRequest.getTargetDataBaseType());
        }
        return columnCompareResults;
    }

    @Override
    public List<DataAmountCompareResult> amountCheck(DataCheckRequest.CompareDataAmountRequest compareDataAmountRequest) {
        List<DataCheckDTO> dataCheckDTOList = compareDataAmountRequest.getDataCheckDTOList();

        List<DataAmountCompareResult> amountCompareResultList = amountComparator.compareAmount(compareDataAmountRequest, dataCheckDTOList);
        return amountCompareResultList;
    }

}
