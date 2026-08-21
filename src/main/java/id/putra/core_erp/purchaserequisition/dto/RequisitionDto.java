package id.putra.core_erp.purchaserequisition.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RequisitionDto {
    private String nrp;

    private String id;
    private DepartmentDto department;
    private String title;
    private String note;
    private String reject_note;
    private String currency;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date due_date;
    private String investment_id;
    private String budget_id;
    private String budget_detail_id;
    private List<RequisitionDetailDto> details;
    private List<ApprovalDto> approvals;
    private ApprovalDto purchaser;

    private String last_approved;
    private List<String> next_approvals;
    private BigInteger budget_remaining;

    // Search
    private String status;
    private List<String> deptIds;
    private String search;
    private String supplierId;
    private String supplierType;
    private String type;

    // File
    private String from;
    private String filename;

    /// From Transfer Request
    private List<String> request_no;

}

