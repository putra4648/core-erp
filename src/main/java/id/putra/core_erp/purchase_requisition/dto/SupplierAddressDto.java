package id.putra.core_erp.purchase_requisition.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SupplierAddressDto {
    private Integer id;
    private String address;
    private String city;
    private String province;
    private String zip;
    private String country;
}
