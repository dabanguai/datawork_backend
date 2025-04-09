package com.dbg.datawork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbg.datawork.model.dto.datasource.TableRequest;
import com.dbg.datawork.model.pojo.Datasource;
import com.dbg.datawork.model.pojo.Table;
import com.dbg.datawork.model.vo.DatasourceTableCountVo;
import com.dbg.datawork.service.DatasourceService;
import com.dbg.datawork.service.TableService;
import com.dbg.datawork.mapper.TableMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 15968
* @description 针对表【table(数据集表)】的数据库操作Service实现
* @createDate 2025-02-22 22:27:23
*/
@Service
public class TableServiceImpl extends ServiceImpl<TableMapper, Table>
    implements TableService{

    @Resource
    private DatasourceService datasourceService;

    @Override
    public Table addTable(TableRequest.TableRequestDto tableRequestDto) {
        Table table = new Table();
        Datasource datasource = datasourceService.getByInfo(tableRequestDto.getDatasourceType(), tableRequestDto.getDataSourceName());
        table.setDatasourceId(datasource.getId());
        table.setName(tableRequestDto.getTableName());
        this.save(table);
        return table;
    }

    @Override
    public List<DatasourceTableCountVo> countTablesByDatasourceId(Long datasourceId) {

        List<Table> tableList = null;

        // 获取所有数据源
        if (null != datasourceId) {
            tableList = this.getTablesByDatasourceId(datasourceId);
        } else {
            tableList = this.list();
        }

        if (CollectionUtils.isEmpty(tableList)) {
            return new ArrayList<>();
        }

        // 获取所有表并按数据源ID分组计数
        Map<Long, Long> tableCountMap = tableList.stream()
                .collect(Collectors.groupingBy(
                        Table::getDatasourceId,
                        Collectors.counting()
                ));

        // 构建结果DTO列表
        return tableList.stream()
                .map(d -> new DatasourceTableCountVo(
                        d.getName(),
                        tableCountMap.getOrDefault(d.getId(), 0L)
                ))
                .collect(Collectors.toList());
    }

    // todo 为了简洁，分层，移动到mapper中，直接与数据库交互
    @Override
    public List<Table> getTablesByDatasourceId(Long datasourceId) {
        LambdaQueryWrapper<Table> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Table::getDatasourceId, datasourceId);
        List<Table> list = list(queryWrapper);
        return list;
    }

    @Override
    public Long countTables() {
        return this.count();
    }
}




