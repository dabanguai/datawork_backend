package com.dbg.datawork.model.dto.dataCheck;

import lombok.Data;

import java.util.List;

/**
 * @author 15968
 * @version 1.0
 * @description: TODO
 * @date 2025/3/31 15:49
 */
@Data
public class DataCheckRequest {

    @Data
    public static class CompareDDLRequest {

        private String sourceDataBaseType;

        private String sourceDataBaseName;

        private String targetDataBaseType;

        private String targetDataBaseName;

        private List<DataCheckDTO> dataCheckDTOList;

    }

    @Data
    public static class CompareDataAmountRequest {
        private String sourceDataBaseType;

        private String sourceDataBaseName;

        private String targetDataBaseType;

        private String targetDataBaseName;

        private List<DataCheckDTO> dataCheckDTOList;
    }
}
