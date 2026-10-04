package id.putra.core_erp.modules.procurement.model;

import java.util.Date;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Purchase  extends BaseEntity{
   

    private Date date;

   
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "status_id", referencedColumnName = "status_id", nullable
    // = false)
    // private Status status;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "approval", referencedColumnName = "nrp")
    // private Users users;
}
