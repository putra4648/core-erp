package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.purchase_requisition.dto.ItemDto;
import jakarta.annotation.Resource;

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

        ItemDto getItemDetail(String id);

        Boolean isItemExist(String id);

        void importItems(FileUpload file) throws Exception;

        Resource exportItems() throws Exception;
}
