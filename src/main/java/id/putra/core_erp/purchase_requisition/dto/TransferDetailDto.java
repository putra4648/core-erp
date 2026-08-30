package id.putra.core_erp.purchase_requisition.dto;

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
public class TransferDetailDto {
    private String id;
    private String item_name;
    private String item_number;
    private String item_description;
    private String item_note;
    private String item_uom;
    private String supplier;
    private String item_req_no;
    private String status;
    private String item_pr_no;
    private List<AppFileDto> spec_files;

    private BigInteger item_qty;
    private BigInteger item_price;
    private BigInteger item_subtotal;
}
