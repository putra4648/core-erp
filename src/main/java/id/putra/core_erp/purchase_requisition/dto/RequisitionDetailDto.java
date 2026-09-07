package id.putra.core_erp.purchase_requisition.dto;

import java.math.BigInteger;
import java.util.List;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<AppFileDto> getSpec_files() {
        return spec_files;
    }

    public void setSpec_files(List<AppFileDto> spec_files) {
        this.spec_files = spec_files;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public List<SupplierDto> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SupplierDto> suppliers) {
        this.suppliers = suppliers;
    }

    public BigInteger getQty() {
        return qty;
    }

    public void setQty(BigInteger qty) {
        this.qty = qty;
    }

    public BigInteger getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigInteger unit_price) {
        this.unit_price = unit_price;
    }

    public BigInteger getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigInteger subtotal) {
        this.subtotal = subtotal;
    }
}
