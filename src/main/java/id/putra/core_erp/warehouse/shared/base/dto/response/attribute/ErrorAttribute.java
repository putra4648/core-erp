package id.putra.core_erp.warehouse.shared.base.dto.response.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorAttribute {

  @JsonProperty("field")
  private String field;

  @JsonProperty("message")
  private String message;

}

