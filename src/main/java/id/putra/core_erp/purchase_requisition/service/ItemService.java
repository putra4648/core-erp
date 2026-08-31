package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import id.putra.core_erp.purchase_requisition.dto.ItemDto;

public interface ItemService {
        void addItem(
                        ItemDto body,
                        MultipartFile itemImage,
                        List<MultipartFile> itemDocs) throws Exception;

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
                        MultipartFile itemImage,
                        List<MultipartFile> itemDocs) throws Exception;

        ItemDto getItemDetail(String id);

        Boolean isItemExist(String id);

        void importItems(MultipartFile file) throws Exception;

        Resource exportItems() throws Exception;
}
