package id.putra.core_erp.support;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import id.putra.core_erp.enums.ApprovalStatus;
import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.enums.BudgetType;
import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.approval.model.ApprovalStep;
import id.putra.core_erp.modules.approval.model.ApprovalWorkflow;
import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.customer.model.Customer;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.procurement.model.Requisition;
import id.putra.core_erp.modules.procurement.model.RequisitionItem;
import id.putra.core_erp.modules.warehouse.model.Rack;
import id.putra.core_erp.modules.warehouse.model.Warehouse;
import id.putra.core_erp.modules.warehouse.model.Zone;

/**
 * Entity builders for tests.
 *
 * <p>Every builder returns an <em>unsaved</em> instance — persisting is the caller's
 * job, because whether a fixture is persisted, flushed or cleared depends on what the
 * test is asserting.
 *
 * <p>Required non-null columns are filled with unique-ish values derived from the
 * argument, so two fixtures created with different arguments never collide on a unique
 * constraint. Nothing here reads the database.
 */
public final class TestFixtures {

	private TestFixtures() {
	}

	public static Customer customer(String name) {
		Customer c = new Customer();
		c.name = name;
		c.email = name.toLowerCase() + "@example.test";
		c.phone = "0800000000";
		return c;
	}

	public static Department department(String code) {
		Department d = new Department();
		d.code = code;
		d.name = "Dept " + code;
		d.deptManager = "MGR-" + code;
		d.deptPurchaser = "PUR-" + code;
		d.approvalPresident = "PRES-" + code;
		return d;
	}

	public static Budget budget(Department dept, String name) {
		Budget b = new Budget();
		b.department = dept;
		b.name = name;
		b.type = BudgetType.EXPENSE;
		b.fiscalYear = 2026;
		b.allocated = new BigDecimal("100000.00");
		return b;
	}

	public static Budget budget(Department dept, String name, String allocated) {
		Budget b = budget(dept, name);
		b.allocated = new BigDecimal(allocated);
		return b;
	}

	public static Product product(String sku) {
		Product p = new Product();
		p.sku = sku;
		p.name = "Product " + sku;
		p.description = "Test product " + sku;
		p.quantity = 100;
		p.minStock = 10;
		p.maxStock = 500;
		p.price = new BigDecimal("12.5000");
		return p;
	}

	public static Requisition requisition(String title, Department dept, Budget budget) {
		Requisition r = new Requisition();
		r.title = title;
		r.referenceId = "REF-" + title;
		r.dueDate = LocalDate.now().plusDays(7);
		r.status = ApprovalStatus.SUBMIT;
		r.department = dept;
		r.budget = budget;
		return r;
	}

	public static RequisitionItem requisitionItem(Requisition req, String name, int qty, String unitPrice) {
		RequisitionItem item = new RequisitionItem();
		item.name = name;
		item.quantity = qty;
		item.unitPrice = new BigDecimal(unitPrice);
		item.requisition = req;
		return item;
	}

	public static Warehouse warehouse(String name) {
		Warehouse w = new Warehouse();
		w.name = name;
		w.location = "Test Location";
		return w;
	}

	public static Zone zone(String code, Warehouse warehouse) {
		Zone z = new Zone();
		z.code = code;
		z.name = "Zone " + code;
		z.warehouse = warehouse;
		return z;
	}

	public static Rack rack(String name, Zone zone) {
		Rack r = new Rack();
		r.name = name;
		r.rows = 1;
		r.cols = 1;
		r.zone = zone;
		return r;
	}

	public static ApprovalWorkflow workflow(String name, ApprovalType type) {
		ApprovalWorkflow w = new ApprovalWorkflow();
		w.workflowName = name;
		w.requestType = type;
		return w;
	}

	public static ApprovalStep step(ApprovalWorkflow workflow, int stepOrder, String roleRequired) {
		ApprovalStep s = new ApprovalStep();
		s.workflow = workflow;
		s.stepOrder = stepOrder;
		s.roleRequired = roleRequired;
		return s;
	}

	public static Receiving receiving(String receivingNumber, OrderStatus status) {
		Receiving r = new Receiving();
		r.receivingNumber = receivingNumber;
		r.receivedDate = LocalDateTime.now();
		r.status = status;
		return r;
	}

	public static ReceivingLine receivingLine(int qty, String qtyReceived, Receiving receiving) {
		ReceivingLine line = new ReceivingLine();
		line.qty = BigDecimal.valueOf(qty);
		line.qtyReceived = new BigDecimal(qtyReceived);
		line.status = "PENDING_QC";
		line.receivedDate = LocalDateTime.now();
		line.receiving = receiving;
		return line;
	}
}
