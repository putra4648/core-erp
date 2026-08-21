package id.putra.core_erp.warehouse.module.customer.dto;

import id.putra.core_erp.warehouse.shared.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class ContactPersonCustomerDto extends BaseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Long customerId;
    // Add other fields as needed
}

