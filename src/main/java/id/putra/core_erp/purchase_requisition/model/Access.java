package id.putra.core_erp.purchase_requisition.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mst_access")
public class Access {
    @Id
    @Column(name = "access_number", nullable = false)
    private String accessNumber;

    @Column(name = "access_item")
    private String accessItem;

    @Column(name = "access_supplier")
    private String accessSupplier;

    @Column(name = "access_budget")
    private String accessBudget;

    @Column(name = "access_requisition")
    private String accessRequisition;

    @Column(name = "access_rfq")
    private String accessRfq;

    @Column(name = "access_transferrqst")
    private String accessTrfRqst;

    @Column(name = "access_inventorycsmb")
    private String accessInvtrCsmb;

    @OneToOne(mappedBy = "accessCode")
    private Group group;

    public String getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber) {
        this.accessNumber = accessNumber;
    }

    public String getAccessItem() {
        return accessItem;
    }

    public void setAccessItem(String accessItem) {
        this.accessItem = accessItem;
    }

    public String getAccessSupplier() {
        return accessSupplier;
    }

    public void setAccessSupplier(String accessSupplier) {
        this.accessSupplier = accessSupplier;
    }

    public String getAccessBudget() {
        return accessBudget;
    }

    public void setAccessBudget(String accessBudget) {
        this.accessBudget = accessBudget;
    }

    public String getAccessRequisition() {
        return accessRequisition;
    }

    public void setAccessRequisition(String accessRequisition) {
        this.accessRequisition = accessRequisition;
    }

    public String getAccessRfq() {
        return accessRfq;
    }

    public void setAccessRfq(String accessRfq) {
        this.accessRfq = accessRfq;
    }

    public String getAccessTrfRqst() {
        return accessTrfRqst;
    }

    public void setAccessTrfRqst(String accessTrfRqst) {
        this.accessTrfRqst = accessTrfRqst;
    }

    public String getAccessInvtrCsmb() {
        return accessInvtrCsmb;
    }

    public void setAccessInvtrCsmb(String accessInvtrCsmb) {
        this.accessInvtrCsmb = accessInvtrCsmb;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
