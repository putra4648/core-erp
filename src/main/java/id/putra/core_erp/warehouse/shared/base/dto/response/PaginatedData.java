package id.putra.core_erp.warehouse.shared.base.dto.response;

import java.util.List;
import id.putra.core_erp.warehouse.shared.base.dto.response.attribute.MetaAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedData<T> {

    @JsonProperty("meta")
    private MetaAttribute meta;

    @JsonProperty("data")
    private List<T> data;
}

