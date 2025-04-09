package com.dbg.datawork.service;

import com.dbg.datawork.datasource.JdbcProvider;
import com.dbg.datawork.model.dto.dataCheck.DataCheckDTO;
import com.dbg.datawork.model.dto.dataCheck.DataCheckRequest;
import com.dbg.datawork.model.entity.DataAmountCompareResult;
import com.dbg.datawork.service.Processor.DatasourceProcessor;
import com.dbg.datawork.service.Processor.DatasourceProcessorFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/4/8 11:37
 */
@Service
public class DataAmountComparator {

    @Resource
    private JdbcProvider jdbcProvider;

    @Resource
    private DatasourceProcessorFactory processorFactory;

    public List<DataAmountCompareResult> compareAmount(DataCheckRequest.CompareDataAmountRequest compareDataAmountRequest, List<DataCheckDTO> dataCheckDTOList) {

        // 获取源和目标处理器
        DatasourceProcessor sourceProcessor = processorFactory.getProcessor(compareDataAmountRequest.getSourceDataBaseType());
        DatasourceProcessor targetProcessor = processorFactory.getProcessor(compareDataAmountRequest.getTargetDataBaseType());

        JdbcTemplate sourceJdbc = jdbcProvider.getJdbcTemplate(sourceProcessor.getSupportedDataSourceType());
        JdbcTemplate targetJdbc = jdbcProvider.getJdbcTemplate(targetProcessor.getSupportedDataSourceType());

        return dataCheckDTOList.parallelStream() // 并行处理提升效率
                .map(pair -> {
                    try {

                        // 获取源表数据量
                        Long sourceAmount  = sourceProcessor.getTableData(pair.getSourceTable(), sourceJdbc);
                        // 获取目标表数据量
                        Long targetAmount  = targetProcessor.getTableData(pair.getTargetTable(), targetJdbc);

                        // 构建对比结果
                        return new DataAmountCompareResult(
                                pair.getSourceTable(),
                                pair.getTargetTable(),
                                sourceAmount,
                                targetAmount,
                                Objects.equals(sourceAmount, targetAmount),
                                Math.abs(sourceAmount - targetAmount)
                        );
                    } catch (DataAccessException e) {
                        // 处理查询异常（如表不存在）
                        return new DataAmountCompareResult(
                                pair.getSourceTable(),
                                pair.getTargetTable(),
                                null,
                                null,
                                false,
                                -1L
                        );
                    }
                })
                .collect(Collectors.toList());
    }
}
