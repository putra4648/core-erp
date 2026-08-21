package id.putra.core_erp.purchaserequisition.service;

import java.util.List;
import id.putra.core_erp.purchaserequisition.dto.AppDto;

public interface AppData<S> {
     /*
      * Used to handle data like create, add, update, delete data
      * return result message or status
      */
     String handleData(AppDto<S> dto);

     /* Return all data */
     List<S> getAll(AppDto<S> dto);

     /* Return single data */
     S getSingleData(AppDto<S> dto);

     /* Return total of data */
     Long countAll(AppDto<S> dto);

}

