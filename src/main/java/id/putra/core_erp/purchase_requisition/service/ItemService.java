package id.putra.core_erp.purchase_requisition.service;

import java.util.List;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.purchase_requisition.model.Item;
import io.vertx.mutiny.core.file.AsyncFile;

public interface ItemService {
        void addItem(
                        Item body,
                        FileUpload itemImage,
                        List<FileUpload> itemDocs) throws Exception;

        List<Item> getItems(
                        String search,
                        String status,
                        String supplierId,
                        Integer page,
                        Integer perPage,
                        String sortBy,
                        String order);

        void updateItem(
                        Item dto,
                        FileUpload itemImage,
                        List<FileUpload> itemDocs) throws Exception;

        Item getItemDetail(Long id);

        Boolean isItemExist(String name);

        void importItems(FileUpload file) throws Exception;

        AsyncFile exportItems() throws Exception;
}
