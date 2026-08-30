package id.putra.core_erp.purchase_requisition.model;

import java.math.BigInteger;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory extends BaseModel {

    @Column(name = "po_number")
    private String poNumber;

    @Id
    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "part_name")
    private String partName;

    @Column(name = "description")
    private String description;

    @Column(name = "area")
    private String area;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "unit")
    private String unit;

    @Column(name = "qty")
    private BigInteger qty;

    @Column(name = "item_area")
    private String itemArea;

    @Column(name = "date")
    private Date date;

    @Column(name = "location")
    private String location;

    @Column(name = "uom")
    private String uom;

}
