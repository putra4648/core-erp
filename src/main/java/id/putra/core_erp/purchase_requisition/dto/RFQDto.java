package id.putra.core_erp.purchase_requisition.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RFQDto extends BaseModelDto {
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date due_date;
    private String note;
    private List<RequisitionDetailDto> details;
    private String status;
    private List<SupplierDto> suppliers;
    private String reject_note;
    private List<ApprovalDto> approvals;
    private String last_approved;
    private List<String> next_approvals;

    /// Search
    private String search;
    private String start_date;
    private String end_date;
    private BigInteger min_budget;
    private BigInteger max_budget;

    private String supplierId;

}
