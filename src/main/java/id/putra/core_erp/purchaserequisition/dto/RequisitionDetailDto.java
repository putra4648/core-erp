package id.putra.core_erp.purchaserequisition.dto;

import java.math.BigInteger;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RequisitionDetailDto {
    private String id;
    private String name;
    private String item_number;
    private String description;
    private String note;
    private List<AppFileDto> spec_files;
    private String uom;
    private String supplier_id;
    private List<SupplierDto> suppliers;

    private BigInteger qty;
    private BigInteger unit_price;
    private BigInteger subtotal;
}

