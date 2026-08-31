package id.putra.core_erp.purchase_requisition.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity()
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
}
