package id.putra.core_erp.purchaserequisition.ui.views.access;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import id.putra.core_erp.purchaserequisition.dto.DepartmentDto;
import id.putra.core_erp.purchaserequisition.dto.UsersDto;
import id.putra.core_erp.purchaserequisition.enums.StatusEnum;
import id.putra.core_erp.purchaserequisition.model.Department;
import id.putra.core_erp.purchaserequisition.model.Group;
import id.putra.core_erp.purchaserequisition.model.Users;
import id.putra.core_erp.purchaserequisition.repository.DepartmentRepo;
import id.putra.core_erp.purchaserequisition.repository.GroupRepo;
import id.putra.core_erp.purchaserequisition.repository.UserRepo;
import id.putra.core_erp.purchaserequisition.service.DepartmentsService;
import id.putra.core_erp.shared.ui.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users & Departments | PR System")
@PermitAll
public class UserManagementView extends VerticalLayout {

    // private final UserService userService;
    private final DepartmentsService departmentsService;
    private final DepartmentRepo departmentRepo;
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;

    private final Tab usersTab = new Tab(VaadinIcon.USERS.create(), new Span("Users"));
    private final Tab deptsTab = new Tab(VaadinIcon.BUILDING.create(), new Span("Departments"));
    private final Tab groupsTab = new Tab(VaadinIcon.KEY.create(), new Span("Groups & Roles"));
    private final Tabs tabs = new Tabs(usersTab, deptsTab, groupsTab);
    private final VerticalLayout contentArea = new VerticalLayout();

    // Users components
    private final TextField userSearchField = new TextField();
    private final Grid<UsersDto> usersGrid = new Grid<>();
    private final List<UsersDto> usersList = new ArrayList<>();

    // Department components
    private final Grid<Department> deptGrid = new Grid<>();
    private final List<Department> deptList = new ArrayList<>();

    // Group components
    private final Grid<Group> groupGrid = new Grid<>();
    private final List<Group> groupList = new ArrayList<>();

    public UserManagementView(
            DepartmentsService departmentsService,
            DepartmentRepo departmentRepo,
            GroupRepo groupRepo,
            UserRepo userRepo) {
        this.departmentsService = departmentsService;
        this.departmentRepo = departmentRepo;
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 header = new H2("Organization & Access Management");
        header.addClassNames(LumoUtility.FontSize.XLARGE);

        tabs.addSelectedChangeListener(e -> updateContent(e.getSelectedTab()));

        contentArea.setSizeFull();
        contentArea.setPadding(false);

        add(header, tabs, contentArea);

        configureUsersGrid();
        configureDeptGrid();
        configureGroupGrid();

        updateContent(usersTab);
    }

    private void updateContent(Tab selectedTab) {
        contentArea.removeAll();
        if (selectedTab.equals(usersTab)) {
            contentArea.add(createUsersSection());
            refreshUsers();
        } else if (selectedTab.equals(deptsTab)) {
            contentArea.add(createDeptsSection());
            refreshDepts();
        } else {
            contentArea.add(createGroupsSection());
            refreshGroups();
        }
    }

    private Component createUsersSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);

        Button addBtn = new Button("Add User", VaadinIcon.PLUS.create(), e -> openAddUserDialog());
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        userSearchField.setPlaceholder("Search users by name...");
        userSearchField.setClearButtonVisible(true);
        userSearchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        userSearchField.setWidth("280px");
        userSearchField.addValueChangeListener(e -> refreshUsers());

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshUsers());

        HorizontalLayout toolbar = new HorizontalLayout(userSearchField, addBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(toolbar, usersGrid);
        return layout;
    }

    private void configureUsersGrid() {
        usersGrid.setSizeFull();
        usersGrid.addColumn(u -> u.getNrp() != null ? u.getNrp() : "-").setHeader("NRP").setAutoWidth(true);
        usersGrid.addColumn(u -> u.getFullname() != null ? u.getFullname() : "-").setHeader("Full Name")
                .setFlexGrow(2);
        usersGrid.addColumn(u -> u.getUsername() != null ? u.getUsername() : "-").setHeader("Username").setFlexGrow(1);
        usersGrid.addColumn(u -> u.getRole() != null ? u.getRole() : "-").setHeader("Role").setFlexGrow(1);
        usersGrid.addColumn(u -> u.getGroups() != null ? u.getGroups().getName() : "-").setHeader("Group")
                .setFlexGrow(1);

        usersGrid.addComponentColumn(u -> {
            Span badge = new Span(u.getStatus() != null ? u.getStatus().name() : "ACTIVE");
            if (u.getStatus() == StatusEnum.ACTIVE || u.getStatus() == null) {
                badge.getElement().getThemeList().add("badge success");
            } else {
                badge.getElement().getThemeList().add("badge error");
            }
            return badge;
        }).setHeader("Status").setAutoWidth(true);

        usersGrid.addComponentColumn(u -> {
            Button toggleBtn = new Button(u.getStatus() == StatusEnum.ACTIVE ? "Deactivate" : "Activate",
                    e -> toggleUserStatus(u));
            toggleBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            return toggleBtn;
        }).setHeader("Action").setAutoWidth(true);
    }

    private void refreshUsers() {
        try {
            String search = userSearchField.getValue() != null ? userSearchField.getValue() : "";
            // List<UsersDto> users = userService.getUsers(search, "ACTIVE", 1, 100,
            // "fullname", "asc");
            // usersList.clear();
            // if (users != null) {
            // usersList.addAll(users);
            // }
            usersGrid.setItems(usersList);
        } catch (Exception ex) {
            Notification.show("Error loading users: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void toggleUserStatus(UsersDto u) {
        try {
            // userService.updateCompanyUsersStatus(u.getNrp());
            Notification.show("User status updated", 2000, Notification.Position.TOP_CENTER);
            refreshUsers();
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private Component createDeptsSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);

        Button addDeptBtn = new Button("Add Department", VaadinIcon.PLUS.create(), e -> openDeptDialog(null));
        addDeptBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshDepts());

        HorizontalLayout toolbar = new HorizontalLayout(addDeptBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(toolbar, deptGrid);
        return layout;
    }

    private void configureDeptGrid() {
        deptGrid.setSizeFull();
        deptGrid.addColumn(d -> d.getDeptNumber() != null ? d.getDeptNumber() : "-").setHeader("Dept Code")
                .setAutoWidth(true);
        deptGrid.addColumn(d -> d.getDeptName() != null ? d.getDeptName() : "-").setHeader("Department Name")
                .setFlexGrow(2);
        deptGrid.addColumn(d -> d.getDeptManager() != null ? d.getDeptManager().getFullname() : "-")
                .setHeader("Manager").setFlexGrow(1);
        deptGrid.addColumn(d -> d.getDeptPurchaser() != null ? d.getDeptPurchaser().getFullname() : "-")
                .setHeader("Purchaser").setFlexGrow(1);

        deptGrid.addComponentColumn(d -> {
            Button editBtn = new Button("Edit", VaadinIcon.EDIT.create(), e -> openDeptDialog(d));
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            return editBtn;
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void refreshDepts() {
        deptList.clear();
        departmentRepo.findAll().forEach(deptList::add);
        deptGrid.setItems(deptList);
    }

    private Component createGroupsSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);

        Button addGroupBtn = new Button("Add Group", VaadinIcon.PLUS.create(), e -> openGroupDialog());
        addGroupBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refreshBtn = new Button(VaadinIcon.REFRESH.create(), e -> refreshGroups());

        HorizontalLayout toolbar = new HorizontalLayout(addGroupBtn, refreshBtn);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(toolbar, groupGrid);
        return layout;
    }

    private void configureGroupGrid() {
        groupGrid.setSizeFull();
        groupGrid.addColumn(g -> g.getCode() != null ? g.getCode() : "-").setHeader("Group Code").setAutoWidth(true);
        groupGrid.addColumn(g -> g.getName() != null ? g.getName() : "-").setHeader("Group Name").setFlexGrow(2);
        groupGrid.addColumn(g -> g.getCompanyUsers() != null ? g.getCompanyUsers().size() + " users" : "0 users")
                .setHeader("Total Members").setAutoWidth(true);
    }

    private void refreshGroups() {
        groupList.clear();
        groupRepo.findAll().forEach(groupList::add);
        groupGrid.setItems(groupList);
    }

    private void openDeptDialog(Department editDept) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(editDept == null ? "Add Department" : "Edit Department");
        dialog.setWidth("500px");

        FormLayout form = new FormLayout();
        TextField codeField = new TextField("Department Code / Number");
        codeField.setRequired(true);
        TextField nameField = new TextField("Department Name");
        nameField.setRequired(true);

        List<Users> allUsers = new ArrayList<>();
        userRepo.findAll().forEach(allUsers::add);

        ComboBox<Users> managerCombo = new ComboBox<>("Department Manager");
        managerCombo.setItemLabelGenerator(u -> u.getFullname() != null ? u.getFullname() : "-");
        managerCombo.setItems(allUsers);
        managerCombo.setClearButtonVisible(true);

        ComboBox<Users> purchaserCombo = new ComboBox<>("Purchaser Specialist");
        purchaserCombo.setItemLabelGenerator(u -> u.getFullname() != null ? u.getFullname() : "-");
        purchaserCombo.setItems(allUsers);
        purchaserCombo.setClearButtonVisible(true);

        if (editDept != null) {
            codeField.setValue(editDept.getDeptNumber() != null ? editDept.getDeptNumber() : "");
            codeField.setReadOnly(true);
            nameField.setValue(editDept.getDeptName() != null ? editDept.getDeptName() : "");
            if (editDept.getDeptManager() != null) {
                managerCombo.setValue(editDept.getDeptManager());
            }
            if (editDept.getDeptPurchaser() != null) {
                purchaserCombo.setValue(editDept.getDeptPurchaser());
            }
        }

        form.add(codeField, nameField, managerCombo, purchaserCombo);

        Button saveBtn = new Button("Save", e -> {
            if (codeField.getValue().isBlank() || nameField.getValue().isBlank()) {
                Notification.show("Code and Name are required", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            DepartmentDto dto = new DepartmentDto();
            dto.setDept_number(codeField.getValue());
            dto.setDept_name(nameField.getValue());

            if (managerCombo.getValue() != null) {
                UsersDto mDto = new UsersDto();
                mDto.setNrp(managerCombo.getValue().getNrp());
                mDto.setFullname(managerCombo.getValue().getFullname());
                dto.setDept_manager(mDto);
            }

            if (purchaserCombo.getValue() != null) {
                UsersDto pDto = new UsersDto();
                pDto.setNrp(purchaserCombo.getValue().getNrp());
                pDto.setFullname(purchaserCombo.getValue().getFullname());
                dto.setDept_purchaser(pDto);
            }

            try {
                if (editDept == null) {
                    departmentsService.addDepartment(dto);
                    Notification.show("Department created", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    departmentsService.updateDepartment(dto);
                    Notification.show("Department updated", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                dialog.close();
                refreshDepts();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", ev -> dialog.close());

        dialog.add(form);
        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    private void openGroupDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add Group");
        dialog.setWidth("450px");

        FormLayout form = new FormLayout();
        TextField codeField = new TextField("Group Code");
        codeField.setRequired(true);
        TextField nameField = new TextField("Group Name");
        nameField.setRequired(true);

        form.add(codeField, nameField);

        Button saveBtn = new Button("Save", e -> {
            if (codeField.getValue().isBlank() || nameField.getValue().isBlank()) {
                Notification.show("Code and Name are required", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            Group g = new Group();
            g.setCode(codeField.getValue());
            g.setName(nameField.getValue());

            try {
                groupRepo.save(g);
                Notification.show("Group saved", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialog.close();
                refreshGroups();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", ev -> dialog.close());

        dialog.add(form);
        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    private void openAddUserDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add User");
        dialog.setWidth("500px");

        FormLayout form = new FormLayout();
        TextField nrpField = new TextField("NRP");
        nrpField.setRequired(true);
        TextField fullnameField = new TextField("Full Name");
        fullnameField.setRequired(true);
        TextField usernameField = new TextField("Username");
        usernameField.setRequired(true);
        TextField emailField = new TextField("Email");

        List<Department> dList = new ArrayList<>();
        departmentRepo.findAll().forEach(dList::add);

        ComboBox<Department> deptCombo = new ComboBox<>("Department");
        deptCombo.setItemLabelGenerator(d -> d.getDeptNumber() + " - " + d.getDeptName());
        deptCombo.setItems(dList);

        List<Group> gList = new ArrayList<>();
        groupRepo.findAll().forEach(gList::add);

        ComboBox<Group> groupCombo = new ComboBox<>("Group / Role");
        groupCombo.setItemLabelGenerator(g -> g.getName());
        groupCombo.setItems(gList);

        form.add(nrpField, fullnameField, usernameField, emailField, deptCombo, groupCombo);

        Button saveBtn = new Button("Save", e -> {
            if (nrpField.getValue().isBlank() || fullnameField.getValue().isBlank()
                    || usernameField.getValue().isBlank()) {
                Notification.show("NRP, Full Name, and Username are required", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            UsersDto dto = new UsersDto();
            dto.setNrp(nrpField.getValue());
            dto.setFullname(fullnameField.getValue());
            dto.setUsername(usernameField.getValue());
            dto.setEmail(emailField.getValue());
            if (deptCombo.getValue() != null) {
                dto.setDept_id(deptCombo.getValue().getDeptNumber());
            }
            if (groupCombo.getValue() != null) {
                dto.setGroup_code(groupCombo.getValue().getCode());
            }
            dto.setStatus(StatusEnum.ACTIVE);

            try {
                // userService.addDepartmentUsers(null, dto);
                Notification.show("User added successfully", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialog.close();
                refreshUsers();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", ev -> dialog.close());

        dialog.add(form);
        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }
}
