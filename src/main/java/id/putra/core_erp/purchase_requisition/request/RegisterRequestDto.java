package id.putra.core_erp.purchase_requisition.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestDto {
    private String nrp;
    private String fullname;
    private String username;
    private String email;
    private String password;

    @JsonIgnore
    private StatusEnum status;
}
