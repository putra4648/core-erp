package id.putra.core_erp.warehouse.shared.exceptions;

import id.putra.core_erp.warehouse.shared.enums.ResponseEnum;
import lombok.Getter;

@Getter
public class ModuleException extends RuntimeException {

  private final ResponseEnum responseEnum;

  public ModuleException(ResponseEnum responseEnum) {
    super(responseEnum.getResponseCode());
    this.responseEnum = responseEnum;
  }
}

