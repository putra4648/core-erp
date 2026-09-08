package id.putra.core_erp.purchase_requisition.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class ItemDto {
    private String item_number;
    private String name;
    private BigInteger price;
    private String description;
    private String status;
    private String category;
    private SupplierDto supplier;
    private Long supplier_id;
    private String gl_acc;
    private String item_image;
    private List<String> item_docs;
    private String label;
    private BigInteger amount;
    private String unit;
    private String note;
    private Date updated_date;

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public SupplierDto getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDto supplier) {
        this.supplier = supplier;
    }

    public Long getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Long supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getGl_acc() {
        return gl_acc;
    }

    public void setGl_acc(String gl_acc) {
        this.gl_acc = gl_acc;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public List<String> getItem_docs() {
        return item_docs;
    }

    public void setItem_docs(List<String> item_docs) {
        this.item_docs = item_docs;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Date updated_date) {
        this.updated_date = updated_date;
    }

}
