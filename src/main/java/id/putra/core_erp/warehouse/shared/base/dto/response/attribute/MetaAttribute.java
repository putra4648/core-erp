package id.putra.core_erp.warehouse.shared.base.dto.response.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaAttribute {

	@JsonProperty("page")
	private int page;

	@JsonProperty("size")
	private int size;

	@JsonProperty("total")
	private long total;

	@JsonProperty("last_page")
	private long lastPage;

}
