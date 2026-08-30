package id.putra.core_erp.purchase_requisition.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ItemDto {
    private String item_number;
    private String name;
    private BigInteger price;
    private String description;
    private String status;
    private String category;
    private SupplierDto supplier;
    private String supplier_id;
    private String gl_acc;
    private String item_image;
    private List<String> item_docs;
    private String label;
    private BigInteger amount;
    private String unit;
    private String note;
    private Date updated_date;

    /// Search

    /// File
}
