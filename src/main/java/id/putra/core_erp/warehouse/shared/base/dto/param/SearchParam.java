package id.putra.core_erp.warehouse.shared.base.dto.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchParam {
    private FilterParam filter;
    private SortParam sort;
}

