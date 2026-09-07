package id.putra.core_erp.purchase_requisition.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TransferDto extends BaseModelDto {
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
    private List<TransferDetailDto> details;
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

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReject_note() {
        return reject_note;
    }

    public void setReject_note(String reject_note) {
        this.reject_note = reject_note;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public String getInvestment_id() {
        return investment_id;
    }

    public void setInvestment_id(String investment_id) {
        this.investment_id = investment_id;
    }

    public String getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(String budget_id) {
        this.budget_id = budget_id;
    }

    public String getBudget_detail_id() {
        return budget_detail_id;
    }

    public void setBudget_detail_id(String budget_detail_id) {
        this.budget_detail_id = budget_detail_id;
    }

    public List<TransferDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<TransferDetailDto> details) {
        this.details = details;
    }

    public List<ApprovalDto> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApprovalDto> approvals) {
        this.approvals = approvals;
    }

    public ApprovalDto getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(ApprovalDto purchaser) {
        this.purchaser = purchaser;
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

    public BigInteger getBudget_remaining() {
        return budget_remaining;
    }

    public void setBudget_remaining(BigInteger budget_remaining) {
        this.budget_remaining = budget_remaining;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<String> deptIds) {
        this.deptIds = deptIds;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
