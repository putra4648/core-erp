package id.putra.core_erp.warehouse.shared.base.dto.param;

import lombok.Data;

@Data
public class FilterParam {

    private String field;
    private String type;
    private Object value;

}

