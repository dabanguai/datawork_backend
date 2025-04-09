package com.dbg.datawork.model.dto.dataCheck;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/31 18:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataCheckDTO {

    private String sourceTable;

    private String targetTable;

}
