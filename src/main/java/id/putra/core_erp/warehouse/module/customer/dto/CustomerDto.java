package id.putra.core_erp.warehouse.module.customer.dto;

import id.putra.core_erp.shared.dto.BaseDto;
import id.putra.core_erp.shared.entity.Customer;


public class CustomerDto extends BaseDto {
    private Long id;
    private String code;
    private String name;
    private Boolean isActive;
    private ContactPersonCustomerDto contactPerson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ContactPersonCustomerDto getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPersonCustomerDto contactPerson) {
        this.contactPerson = contactPerson;
    }

    public static CustomerDto toDto(Customer entity) {
        if (entity == null) {
            return null;
        }
        CustomerDto dto = new CustomerDto();
        dto.id = entity.getId();
        dto.code = entity.getCode();
        dto.name = entity.getName();
        dto.isActive = entity.getIsActive();
        if (entity.getContactPerson() != null) {
            dto.contactPerson = ContactPersonCustomerDto.toDto(entity.getContactPerson());
        }
        return dto;
    }

    public Customer toEntity() {
        Customer entity = new Customer();
        entity.setId(this.id);
        entity.setCode(this.code);
        entity.setName(this.name);
        entity.setIsActive(this.isActive);
        if (this.contactPerson != null) {
            entity.setContactPerson(this.contactPerson.toEntity());
        }
        return entity;
    }
}
