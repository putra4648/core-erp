package id.putra.core_erp.purchase_requisition.dto;

import java.math.BigInteger;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventoryDto {
    private String po_number;
    private String part_number;
    private String part_name;
    private String description;
    private String area;
    private String shelf;
    private String supplier;
    private String unit;
    private BigInteger qty;
    private String item_area;
    private Date date;
    private String location;
    private String uom;

    /// Search
    private String search;

}
