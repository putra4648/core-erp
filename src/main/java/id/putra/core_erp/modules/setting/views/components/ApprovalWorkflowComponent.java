package id.putra.core_erp.modules.setting.views.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;

import id.putra.core_erp.modules.approval.model.ApprovalStep;
import id.putra.core_erp.modules.approval.model.ApprovalWorkflow;
import id.putra.core_erp.modules.approval.service.ApprovalWorkflowService;
import id.putra.core_erp.ui.ViewWithDialog;
import io.quarkus.panache.common.Page;

public class ApprovalWorkflowComponent extends ViewWithDialog {
    private final Grid<ApprovalWorkflow> grid = new Grid<>();
    private final Grid<ApprovalStep> stepGrid = new Grid<>();

    private final BeanValidationBinder<ApprovalWorkflow> binder = new BeanValidationBinder<>(ApprovalWorkflow.class);
    private final BeanValidationBinder<ApprovalStep> stepBinder = new BeanValidationBinder<>(ApprovalStep.class);

    private final TextField workflowNameField = new TextField("Workflow Name");
    private final TextField requestTypeField = new TextField("Request Type");

    private final TextField roleField = new TextField();
    private final IntegerField stepOrderField = new IntegerField();

    private final TextField searchField = new TextField();

    private final ApprovalWorkflowService workflowService;

    private final Editor<ApprovalStep> stepEditor;
    private final List<ApprovalStep> stepList = new ArrayList<>();

    public ApprovalWorkflowComponent(ApprovalWorkflowService workflowService) {
        this.workflowService = workflowService;

        // Inisialisasi Editor Step Grid
        this.stepEditor = stepGrid.getEditor();
        this.stepEditor.setBuffered(true);
        this.stepEditor.setBinder(stepBinder); // Kunci: Binder harus terhubung ke Editor

        setSizeFull();
        setPadding(false);
        setSpacing(true);

        configureValidation();
        configureStepValidation();
        configureStepGridColumns(); // Konfigurasi struktur kolom Grid CUKUP SEKALI di Constructor
        addToolbar();
        configureGrid();
    }

    private void configureValidation() {
        binder.bind(workflowNameField, "workflowName");
        binder.bind(requestTypeField, "requestType");
    }

    private void configureStepValidation() {
        stepBinder.bind(roleField, "roleRequired");
        stepBinder.bind(stepOrderField, "stepOrder");
    }

    private void configureStepGridColumns() {
        stepGrid.setHeight("250px"); // Beri tinggi tetap agar scroller/grid stabil

        stepGrid.addColumn(s -> s.roleRequired)
                .setHeader("Role Required")
                .setEditorComponent(roleField)
                .setKey("roleRequired");

        stepGrid.addColumn(s -> s.stepOrder)
                .setHeader("Step Order")
                .setEditorComponent(stepOrderField)
                .setKey("stepOrder");

        stepGrid.addComponentColumn(param -> {
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addClickListener(e -> {
                if (stepEditor.isOpen())
                    stepEditor.cancel();
                stepGrid.getEditor().editItem(param);
            });
            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> {
                stepList.remove(param);
                stepGrid.getDataProvider().refreshAll();
            });
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            var layout = new HorizontalLayout(editButton, deleteBtn);

            return layout;
        }).setEditorComponent(param -> {
            Button saveBtn = new Button(VaadinIcon.DISC.create(), e -> {
                // stepEditor.save() mengembalikan true jika semua validasi Binder lolos
                if (stepEditor.save()) {
                    stepGrid.getDataProvider().refreshAll();
                    Notification.show("Step berhasil disimpan", 2000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification
                            .show("Mohon lengkapi field yang bermasalah pada Step", 3000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });
            Button cancelBtn = new Button(VaadinIcon.CLOSE.create(), e -> stepEditor.cancel());

            var layout = new HorizontalLayout(saveBtn, cancelBtn);

            return layout;
        }).setFrozenToEnd(true).setHeader("Actions").setAutoWidth(true);
    }

    @Override
    public void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(s -> s.workflowName != null ? s.workflowName : "-")
                .setHeader("Workflow Name")
                .setKey("workflowName")
                .setSortable(true)
                .setAutoWidth(true);

        grid.addColumn(s -> s.requestType != null ? s.requestType : "-")
                .setHeader("Request Type")
                .setKey("requestType")
                .setSortable(true)
                .setAutoWidth(true);

        grid.addComponentColumn(param -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create(), e -> openDialog(param));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(param));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Actions").setAutoWidth(true);

        refreshData();
        add(grid);
    }

    @Override
    public void addToolbar() {
        searchField.setPlaceholder("Search parameters...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("280px");
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> refreshData());

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create(), e -> openDialog(new ApprovalWorkflow()));
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
            searchField.clear();
            refreshData();
        });

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        add(toolbar);
    }

    @Override
    public void refreshData() {
        ApprovalWorkflow entity = new ApprovalWorkflow();
        if (!searchField.isEmpty()) {
            entity.workflowName = "ILIKE %" + searchField.getValue() + "%";
            entity.requestType = "ILIKE %" + searchField.getValue() + "%";
        }
        List<ApprovalWorkflow> data = workflowService.findAll(entity, Page.of(0, 100));
        grid.setItems(data);
    }

    @Override
    protected void buildAndShowDialog(Object entity) throws Exception {
        ApprovalWorkflow workflow = (ApprovalWorkflow) entity;
        boolean isEdit = workflow.id != null;

        // 1. Reset state Binder & List
        binder.readBean(workflow);
        stepList.clear();
        if (isEdit && workflow.steps != null) {
            stepList.addAll(workflow.steps);
        }
        stepGrid.setItems(stepList);

        // 2. Buat Dialog & Layout
        Dialog dialog = new Dialog(isEdit ? "Edit Approval Workflow" : "Add Approval Workflow");

        FormLayout form = new FormLayout();
        form.add(workflowNameField, requestTypeField);

        Button addStepBtn = new Button("Add Step", e -> {
            stepList.add(new ApprovalStep());
            stepGrid.getDataProvider().refreshAll();
        });
        form.add(addStepBtn);
        form.add(stepGrid);

        dialog.add(form);

        Button saveBtn = new Button("Save", e -> {
            try {
                // Validasi 1: Pastikan tidak ada row grid step yang sedang dalam mode edit
                if (stepEditor.isOpen()) {
                    Notification
                            .show("Selesaikan atau batalkan edit baris Step terlebih dahulu", 3000,
                                    Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                // Validasi 2: Minimal harus ada 1 step
                if (stepList.isEmpty()) {
                    Notification
                            .show("Workflow harus memiliki minimal 1 Approval Step", 3000,
                                    Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                // Validasi 3: Validasi binder form utama
                if (binder.writeBeanIfValid(workflow)) {
                    workflow.steps = new ArrayList<>(stepList.stream().map(step -> {
                        step.workflow = workflow;
                        return step;
                    }).toList());

                    if (isEdit) {
                        workflowService.update(workflow);
                    } else {
                        workflowService.save(workflow);
                    }
                    dialog.close();
                    refreshData();
                }
            } catch (Exception ex) {
                Notification
                        .show("Gagal menyimpan Workflow: " + ex.getMessage(), 4000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", ev -> dialog.close());

        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    @Override
    public void addHeader() {
        // Implementasi header
    }

    @Override
    public void confirmDelete(Object entity) {
        // Implementasi konfirmasi hapus
    }
}