package id.putra.core_erp.purchaserequisition.model;

import java.util.List;

import id.putra.core_erp.purchaserequisition.enums.RoleEnum;
import id.putra.core_erp.purchaserequisition.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "mst_users")
public class Users extends BaseModel {
    @Id
    @Column(name = "nrp", nullable = false)
    private String nrp;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "status", nullable = false, precision = 1)
    private StatusEnum status;

    @Column(name = "profile_path")
    private String profilePath;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "group_code", referencedColumnName = "code")
    private Group companyGroup;

    @ManyToOne
    @JoinColumn(name = "dept_number", referencedColumnName = "dept_number")
    private Department department;

    @Column(name = "other_depts")
    private List<String> otherDepts;

    @Column(name = "role")
    private RoleEnum role;

}

