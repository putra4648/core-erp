package id.putra.core_erp.purchase_requisition.model;

import java.math.BigInteger;
import java.util.List;

public class TransferDetail {
    private String id;
    private String item_name;
    private String item_number;
    private String item_description;
    private String item_note;
    private String item_uom;
    private String supplier;
    private String item_req_no;
    private String status;
    private Long item_pr_no;
    private List<AppFile> spec_files;

    private BigInteger item_qty;
    private BigInteger item_price;
    private BigInteger item_subtotal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_note() {
        return item_note;
    }

    public void setItem_note(String item_note) {
        this.item_note = item_note;
    }

    public String getItem_uom() {
        return item_uom;
    }

    public void setItem_uom(String item_uom) {
        this.item_uom = item_uom;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getItem_req_no() {
        return item_req_no;
    }

    public void setItem_req_no(String item_req_no) {
        this.item_req_no = item_req_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getItem_pr_no() {
        return item_pr_no;
    }

    public void setItem_pr_no(Long item_pr_no) {
        this.item_pr_no = item_pr_no;
    }

    public List<AppFile> getSpec_files() {
        return spec_files;
    }

    public void setSpec_files(List<AppFile> spec_files) {
        this.spec_files = spec_files;
    }

    public BigInteger getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(BigInteger item_qty) {
        this.item_qty = item_qty;
    }

    public BigInteger getItem_price() {
        return item_price;
    }

    public void setItem_price(BigInteger item_price) {
        this.item_price = item_price;
    }

    public BigInteger getItem_subtotal() {
        return item_subtotal;
    }

    public void setItem_subtotal(BigInteger item_subtotal) {
        this.item_subtotal = item_subtotal;
    }
}
