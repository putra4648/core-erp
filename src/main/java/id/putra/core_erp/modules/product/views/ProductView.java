package id.putra.core_erp.modules.product.views;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import id.putra.core_erp.modules.product.model.Product;
import id.putra.core_erp.modules.product.service.ProductService;
import id.putra.core_erp.ui.MainLayout;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;
import io.quarkus.security.Authenticated;

@Route(value = "master/products", layout = MainLayout.class)
@PageTitle("Products")
@Authenticated
public class ProductView extends ViewWithDialog {

	private final ProductService productService;
	private final Grid<Product> grid = new Grid<>();
	private final TextField searchField = new TextField();

	private BeanValidationBinder<@NonNull Product> binder = new BeanValidationBinder<>(Product.class);
	private TextField sku = new TextField("SKU");
	private TextField name = new TextField("Product Name");
	private TextArea description = new TextArea("Description");
	private IntegerField quantity = new IntegerField("Quantity");
	private IntegerField minStock = new IntegerField("Min Stock");
	private IntegerField maxStock = new IntegerField("Max Stock");

	public ProductView(ProductService productService) {
		super();
		this.productService = productService;

		configureValidation();

		addHeader();
		addToolbar();
		configureGrid();

	}

	private void configureValidation() {
		binder.bind(sku, "sku");
		binder.bind(name, "name");
		binder.bind(description, "description");
		binder.bind(quantity, "quantity");
		binder.bind(minStock, "minStock");
		binder.bind(maxStock, "maxStock");
	}

	@Override
	public void addHeader() {
		H2 title = new H2("Product Catalog");
		title.addClassNames("m-0", "text-xl", "font-bold");
		Paragraph desc = new Paragraph("Manage product items, SKU details, and stock thresholds.");
		desc.addClassNames("text-secondary", "m-0");
		add(title, desc);
	}

	@Override
	public void addToolbar() {
		searchField.setPlaceholder("Search products by name or SKU...");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
		searchField.addValueChangeListener(e -> refreshData());

		Button addButton = new Button("Add Product", VaadinIcon.PLUS.create(), e -> openDialog(new Product()));
		addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button refreshButton = new Button(VaadinIcon.REFRESH.create(), e -> refreshData());

		HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton, refreshButton);
		toolbar.setWidthFull();
		toolbar.setFlexGrow(1, searchField);

		add(toolbar);
	}

	@Override
	public void configureGrid() {
		grid.setSizeFull();
		grid.addColumn(p -> p.sku != null ? p.sku : "").setHeader("SKU").setAutoWidth(true).setSortable(true);
		grid.addColumn(p -> p.name != null ? p.name : "").setHeader("Name").setFlexGrow(1)
				.setSortable(true);
		grid.addColumn(p -> p.quantity != null ? p.quantity : 0).setHeader("Total Stock").setAutoWidth(true);
		grid.addColumn(p -> p.minStock != null ? p.minStock : 0).setHeader("Min Stock").setAutoWidth(true);
		grid.addColumn(p -> p.maxStock != null ? p.maxStock : 0).setHeader("Max Stock").setAutoWidth(true);

		grid.addComponentColumn(product -> {
			Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(product));
			editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(product));
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

			return new HorizontalLayout(editBtn, deleteBtn);
		}).setHeader("Actions").setAutoWidth(true);

		add(grid);

		refreshData();
	}

	@Override
	public void refreshData() {
		Product entity = new Product();
		entity.name = searchField.getValue();
		entity.sku = searchField.getValue();
		grid.setItems(productService.findAll(entity, Page.ofSize(100)));
	}

	@Override
	protected void buildAndShowDialog(@NonNull Object entity) throws Exception {
		Product product = (Product) entity;
		binder.readBean(product);

		Dialog dialog = new Dialog(product.id == null ? "Add Product" : "Edit Product");
		dialog.setWidth("600px");

		FormLayout formLayout = new FormLayout(sku, name, quantity, minStock, maxStock, description);
		formLayout.setColspan(description, 2);
		dialog.add(formLayout);

		Button saveBtn = new Button("Save", e -> {
			if (binder.writeBeanIfValid(product)) {
				if (product.id != null) {
					productService.update(product);
					Notification.show("Product updated successfully", 3000,
							Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				} else {
					productService.save(product);
					Notification.show("Product created successfully", 3000,
							Notification.Position.TOP_END)
							.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				}
				dialog.close();
				refreshData();

			} else {
				Notification.show("Periksa kembali inputan Anda", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button cancelBtn = new Button("Cancel", e -> dialog.close());

		dialog.getFooter().add(cancelBtn, saveBtn);
		dialog.open();
	}

	@Override
	public void confirmDelete(Object entity) {
		Product product = (Product) entity;
		Dialog confirmDialog = new Dialog("Delete Product");
		confirmDialog.add(new Paragraph("Are you sure you want to delete product '" + product.name + "'?"));

		Button deleteBtn = new Button("Delete", e -> {
			try {
				productService.delete(product.id);
				Notification.show("Product deleted successfully", 3000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				confirmDialog.close();
				refreshData();
			} catch (Exception ex) {
				Notification.show("Failed to delete product: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
						.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});
		deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

		Button cancelBtn = new Button("Cancel", e -> confirmDialog.close());

		confirmDialog.getFooter().add(cancelBtn, deleteBtn);
		confirmDialog.open();
	}
}
