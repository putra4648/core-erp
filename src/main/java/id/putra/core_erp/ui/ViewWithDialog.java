package id.putra.core_erp.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class ViewWithDialog extends VerticalLayout {

    public ViewWithDialog() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

    }

    public void openDialog() {

    }

    public void closeDialog() {

    }

    public abstract void addHeader();

    public abstract void addToolbar();

    public abstract void refreshData();

    public abstract void configureGrid();

    public abstract void openDialog(Object entity);

    public abstract void confirmDelete(Object entity);
}
