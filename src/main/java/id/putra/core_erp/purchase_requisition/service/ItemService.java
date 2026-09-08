package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.purchase_requisition.dto.ItemDto;
import io.vertx.mutiny.core.file.AsyncFile;

public interface ItemService {
        void addItem(
                        ItemDto body,
                        FileUpload itemImage,
                        List<FileUpload> itemDocs) throws Exception;

        List<ItemDto> getItems(
                        String search,
                        String status,
                        String supplierId,
                        Integer page,
                        Integer perPage,
                        String sortBy,
                        String order);

        void updateItem(
                        ItemDto dto,
                        FileUpload itemImage,
                        List<FileUpload> itemDocs) throws Exception;

        ItemDto getItemDetail(Long id);

        Boolean isItemExist(String name);

        void importItems(FileUpload file) throws Exception;

        AsyncFile exportItems() throws Exception;
}
