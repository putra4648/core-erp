package id.putra.core_erp.purchaserequisition.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppDto<T> {
    private T params;

    private Integer page;
    private Integer size;
}

