package com.dbg.datawork.service;

import com.dbg.datawork.model.dto.datasource.TableRequest;
import com.dbg.datawork.model.pojo.Table;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbg.datawork.model.vo.DatasourceTableCountVo;

import java.util.List;

/**
* @author 15968
* @description 针对表【table(数据集表)】的数据库操作Service
* @createDate 2025-02-22 22:27:23
*/
public interface TableService extends IService<Table> {

    Table addTable(TableRequest.TableRequestDto tableRequestDto);

    List<DatasourceTableCountVo> countTablesByDatasourceId(Long datasourceId);

    List<Table> getTablesByDatasourceId(Long datasourceId);

    Long countTables();
}
