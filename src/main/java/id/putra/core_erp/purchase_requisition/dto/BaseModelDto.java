package id.putra.core_erp.purchase_requisition.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseModelDto {
    private String created_by;
    private String updated_by;
    private Date created_date;
    private Date updated_date;
}
