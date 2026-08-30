package id.putra.core_erp.purchase_requisition.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date date;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "status_id", referencedColumnName = "status_id", nullable
    // = false)
    // private Status status;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "approval", referencedColumnName = "nrp")
    // private Users users;
}
