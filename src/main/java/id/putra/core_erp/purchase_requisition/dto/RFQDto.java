package id.putra.core_erp.purchase_requisition.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<RequisitionDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<RequisitionDetailDto> details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SupplierDto> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SupplierDto> suppliers) {
        this.suppliers = suppliers;
    }

    public String getReject_note() {
        return reject_note;
    }

    public void setReject_note(String reject_note) {
        this.reject_note = reject_note;
    }

    public List<ApprovalDto> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApprovalDto> approvals) {
        this.approvals = approvals;
    }

    public String getLast_approved() {
        return last_approved;
    }

    public void setLast_approved(String last_approved) {
        this.last_approved = last_approved;
    }

    public List<String> getNext_approvals() {
        return next_approvals;
    }

    public void setNext_approvals(List<String> next_approvals) {
        this.next_approvals = next_approvals;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public BigInteger getMin_budget() {
        return min_budget;
    }

    public void setMin_budget(BigInteger min_budget) {
        this.min_budget = min_budget;
    }

    public BigInteger getMax_budget() {
        return max_budget;
    }

    public void setMax_budget(BigInteger max_budget) {
        this.max_budget = max_budget;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

}
