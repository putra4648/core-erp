package id.putra.core_erp.purchase_requisition.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SupplierContactDto {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String branch;
}
