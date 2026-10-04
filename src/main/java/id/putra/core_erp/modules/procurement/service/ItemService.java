package id.putra.core_erp.modules.procurement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import id.putra.core_erp.modules.procurement.model.Item;
import id.putra.core_erp.modules.procurement.repository.ItemRepo;
import id.putra.core_erp.modules.procurement.repository.SupplierRepo;
import id.putra.core_erp.modules.supplier.model.Supplier;
import id.putra.core_erp.service.BaseService;
import id.putra.core_erp.constant.MessageConstant;
import id.putra.core_erp.util.AppFileUtil;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ItemService implements BaseService<Item>{

    private final ItemRepo itemRepo;
    private final SupplierRepo supplierRepo;

    public ItemService(ItemRepo itemRepo, SupplierRepo supplierRepo) {
        this.itemRepo = itemRepo;
        this.supplierRepo = supplierRepo;
    }

	@Override
	public void save(Item entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Item findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Item entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<Item> findAll(Item entity, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

   
}
