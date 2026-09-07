package id.putra.core_erp.purchase_requisition.model;

import java.util.List;

import id.putra.core_erp.purchase_requisition.enums.RoleEnum;
import id.putra.core_erp.purchase_requisition.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Group getCompanyGroup() {
        return companyGroup;
    }

    public void setCompanyGroup(Group companyGroup) {
        this.companyGroup = companyGroup;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<String> getOtherDepts() {
        return otherDepts;
    }

    public void setOtherDepts(List<String> otherDepts) {
        this.otherDepts = otherDepts;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

}
