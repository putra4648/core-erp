package id.putra.core_erp.purchaserequisition.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompanyDto {
    private Integer id;
    private String name;
    private String telp;
    private String npwp;
    private String logo_name;
    private String logo_image;
    private String accountBank;
    private String financialEndYear;
    private Boolean isRegisterApproval;
    private Boolean isAutoCloseOrder;
    private String curr1;
    private String curr2;
    private String curr3;
}

