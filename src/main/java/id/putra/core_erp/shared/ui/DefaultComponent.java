package id.putra.core_erp.shared.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultComponent extends VerticalLayout implements SaveDialogFunction, CloseDialogFunction {
    private final FormLayout formLayout;
    private final Dialog dialog;

    public DefaultComponent(String title) {
        setSizeFull();

        this.formLayout = new FormLayout();
        this.dialog = new Dialog();
        this.dialog.setHeaderTitle(title);

        this.dialog.add(formLayout);

        Button close = new Button("Close", e -> dialog.close());
        Button save = new Button("Save", e -> saveAction());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.getFooter().add(save, close);
    }

    public void openDialog() {
        this.dialog.open();
    }

    public void closeDialog() {
        this.dialog.close();
    }

    public void addFormComponent(VerticalLayout formLayout) {
        this.formLayout.add(formLayout);
    }

}
