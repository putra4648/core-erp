package id.putra.core_erp.purchase_requisition.model;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "requisition_detail")
public class RequisitionDetail extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "note")
    private String note;

    // Only path
    @Column(name = "spec_files")
    private List<String> specFiles;

    @Column(name = "uom")
    private String uom;

    @Column(name = "qty")
    private BigInteger qty;

    @Column(name = "unit_price")
    private BigInteger unitPrice;

    @Column(name = "supplier")
    private String supplier;

    // @ManyToOne
    // @JoinColumn(name = "supplier_id", referencedColumnName = "acc_no")
    // private Supplier supplier;

    @Formula("qty * currency")
    @Column(name = "sub_total")
    private BigInteger subTotal;

    @ManyToOne
    @JoinColumn(name = "requisition_id", referencedColumnName = "id")
    private Requisition requisition;

    @ManyToOne
    @JoinColumn(name = "rfq_id", referencedColumnName = "id")
    private RFQ rfq;

}
