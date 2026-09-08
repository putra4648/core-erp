package id.putra.core_erp.purchase_requisition.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import id.putra.core_erp.shared.enums.TypeEnum;

public class SupplierDto {
    private Long id;
    private String name;
    private String acc_no;
    private String website;
    private StatusEnum status;
    private TypeEnum type;
    private String phone;
    private String fax;
    private String currency;
    private String npwp;
    private String acc_bank;
    private String notes;
    private String logo_image;
    private String logo_name;
    private Date modified_date;
    private SupplierAddressDto po_address;
    private SupplierAddressDto address;
    private List<SupplierContactDto> contacts;

    private BigInteger price;
    private AppFileDto file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(String acc_no) {
        this.acc_no = acc_no;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    public String getAcc_bank() {
        return acc_bank;
    }

    public void setAcc_bank(String acc_bank) {
        this.acc_bank = acc_bank;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLogo_image() {
        return logo_image;
    }

    public void setLogo_image(String logo_image) {
        this.logo_image = logo_image;
    }

    public String getLogo_name() {
        return logo_name;
    }

    public void setLogo_name(String logo_name) {
        this.logo_name = logo_name;
    }

    public Date getModified_date() {
        return modified_date;
    }

    public void setModified_date(Date modified_date) {
        this.modified_date = modified_date;
    }

    public SupplierAddressDto getPo_address() {
        return po_address;
    }

    public void setPo_address(SupplierAddressDto po_address) {
        this.po_address = po_address;
    }

    public SupplierAddressDto getAddress() {
        return address;
    }

    public void setAddress(SupplierAddressDto address) {
        this.address = address;
    }

    public List<SupplierContactDto> getContacts() {
        return contacts;
    }

    public void setContacts(List<SupplierContactDto> contacts) {
        this.contacts = contacts;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public AppFileDto getFile() {
        return file;
    }

    public void setFile(AppFileDto file) {
        this.file = file;
    }
}
