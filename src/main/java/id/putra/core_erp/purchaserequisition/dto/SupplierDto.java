package id.putra.core_erp.purchaserequisition.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import id.putra.core_erp.purchaserequisition.enums.StatusEnum;
import id.putra.core_erp.shared.enums.TypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SupplierDto {
    private String id;
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
}


