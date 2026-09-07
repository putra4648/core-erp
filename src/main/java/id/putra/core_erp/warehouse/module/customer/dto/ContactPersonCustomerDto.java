package id.putra.core_erp.warehouse.module.customer.dto;

import id.putra.core_erp.shared.dto.BaseDto;
import id.putra.core_erp.shared.entity.ContactPersonCustomer;
import id.putra.core_erp.shared.entity.Customer;


public class ContactPersonCustomerDto extends BaseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Long customerId;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public static ContactPersonCustomerDto toDto(ContactPersonCustomer entity) {
        if (entity == null) {
            return null;
        }
        ContactPersonCustomerDto dto = new ContactPersonCustomerDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.email = entity.getEmail();
        dto.phone = entity.getPhone();
        dto.address = entity.getAddress();
        if (entity.getCustomer() != null) {
            dto.customerId = entity.getCustomer().getId();
        }
        return dto;
    }

    public ContactPersonCustomer toEntity() {
        ContactPersonCustomer entity = new ContactPersonCustomer();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setEmail(this.email);
        entity.setPhone(this.phone);
        entity.setAddress(this.address);
        if (this.customerId != null) {
            Customer customer = new Customer();
            customer.setId(this.customerId);
            entity.setCustomer(customer);
        }
        return entity;
    }
}
