package id.putra.core_erp.ui;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class ViewWithDialog extends VerticalLayout {

    public ViewWithDialog() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

    }

    public abstract void addHeader();

    public abstract void addToolbar();

    public abstract void refreshData();

    public abstract void configureGrid();

    public void openDialog(Object entity) {
        try {
            buildAndShowDialog(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
            Notification.show("Terjadi kesalahan: " + ex.getMessage(), 5000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    protected abstract void buildAndShowDialog(Object entity) throws Exception;

    public abstract void confirmDelete(Object entity);
}
