package id.putra.core_erp.warehouse.shared.base.dto.param;

import lombok.Data;

@Data
public class SortParam {
    private String column;
    private String dir;
    private String field;
}

