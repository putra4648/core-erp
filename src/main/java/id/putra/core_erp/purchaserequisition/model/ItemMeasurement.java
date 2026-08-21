package id.putra.core_erp.purchaserequisition.model;

import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items_measurement")
public class ItemMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "unit")
    private String unit;

    // @Column(name = "qty")
    // private BigInteger qty;

    @Column(name = "amount")
    private BigInteger amount;

    @Column(name = "notes")
    private String notes;

    @OneToOne(mappedBy = "measurement")
    private Item item;
}

