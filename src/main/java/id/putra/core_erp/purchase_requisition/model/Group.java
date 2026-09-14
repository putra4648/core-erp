package id.putra.core_erp.purchase_requisition.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Users> getCompanyUsers() {
        return companyUsers;
    }

    public void setCompanyUsers(List<Users> companyUsers) {
        this.companyUsers = companyUsers;
    }

    public Access getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(Access accessCode) {
        this.accessCode = accessCode;
    }
}
