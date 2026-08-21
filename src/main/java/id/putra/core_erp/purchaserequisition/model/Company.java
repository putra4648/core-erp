package id.putra.core_erp.purchaserequisition.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
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
@Table(name = "company")
public class Company extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comp_id", nullable = false)
    private Integer compId;

    @Column(name = "comp_name", nullable = false, unique = true)
    private String compName;

    @Column(name = "comp_telp")
    private String compTelp;

    @Column(name = "comp_npwp")
    private String compNPWP;

    @Column(name = "comp_logoname")
    private String compLogopath;

    // @Lob
    // @Column(name = "comp_logoblob")
    // private byte[] compLogoblob;

    @Column(name = "comp_accbank")
    private String compAccbank;

    @Column(name = "comp_endyear")
    private Date compEndyear;

    @Column(name = "comp_currencies")
    private List<String> compCurrencies;

    @Column(name = "comp_needRegisterApproval")
    private Boolean compNeedRegisterApproval;

    @Column(name = "comp_needAutoCloseOrder")
    private Boolean compNeedAutoCloseOrder;

}

