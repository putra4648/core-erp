package id.putra.core_erp.purchase_requisition.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDetailDto {
    private String item;
    private String keyword;
    private Long count;
}
