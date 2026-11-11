package id.putra.core_erp.modules.report.excel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.product.repository.ProductRepository;
import id.putra.core_erp.service.excel.ExcelColumn;
import id.putra.core_erp.service.excel.ExcelColumn.Type;
import id.putra.core_erp.service.excel.ExcelReport;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Inventory stock status report — product master data with a computed stock classification.
 *
 * <p>Deliberately named <em>status</em>, not valuation: {@link Product} carries no price
 * field, so there is no monetary value to report. Summing quantity alone into a
 * "total value" column would be meaningless, so the total row sums quantities only.
 *
 * <p>Not date ranged — it reflects the current state of the product master.
 */
@ApplicationScoped
public class InventoryReportSheet {

	private static final List<ExcelColumn> COLUMNS = List.of(
			ExcelColumn.of("No.", 6, Type.INTEGER, HorizontalAlignment.CENTER),
			ExcelColumn.of("SKU", 18, Type.TEXT),
			ExcelColumn.of("Product Name", 38, Type.TEXT),
			ExcelColumn.of("Quantity", 12, Type.INTEGER),
			ExcelColumn.of("Min Stock", 12, Type.INTEGER),
			ExcelColumn.of("Max Stock", 12, Type.INTEGER),
			ExcelColumn.of("Stock Status", 16, Type.STATUS));

	private final ProductRepository productRepository;

	public InventoryReportSheet(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public ExcelReport build() {
		List<Product> products = productRepository.listAll();
		List<List<Object>> rows = new ArrayList<>(products.size());
		int index = 1;
		for (Product p : products) {
			rows.add(List.of(
					index++,
					blankIfNull(p.sku),
					blankIfNull(p.name),
					p.quantity,
					p.minStock,
					p.maxStock,
					stockStatus(p)));
		}
		return new ExcelReport("Inventory Stock Status Report",
				"Snapshot: " + LocalDate.now(), COLUMNS, rows);
	}

	/**
	 * Classifies stock. Returns {@code UNKNOWN} when the thresholds are absent rather than
	 * guessing a status — an incomplete master record should be visible as such.
	 */
	private static String stockStatus(Product p) {
		if (p.quantity == null || p.minStock == null) {
			return "UNKNOWN";
		}
		if (p.quantity < p.minStock) {
			return "LOW STOCK";
		}
		if (p.maxStock != null && p.quantity > p.maxStock) {
			return "OVERSTOCK";
		}
		return "OK";
	}

	private static String blankIfNull(String value) {
		return value == null ? null : value;
	}
}
