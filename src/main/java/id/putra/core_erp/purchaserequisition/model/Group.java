package id.putra.core_erp.purchaserequisition.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity()
@Table(name = "mst_users_groups")
public class Group {

    @Id
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "companyGroup", cascade = CascadeType.ALL)
    private List<Users> companyUsers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "access_code", referencedColumnName = "access_number")
    private Access accessCode;
}

