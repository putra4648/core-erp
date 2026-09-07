package id.putra.core_erp.purchase_requisition.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseModel {

    @Column(name = "created_by")
    private String userCreateBy;

    @Column(name = "created_date")
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdDate;

    @Column(name = "updated_by")
    private String userUpdateBy;

    @Column(name = "updated_date")
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedDate;

    public String getUserCreateBy() {
        return userCreateBy;
    }

    public void setUserCreateBy(String userCreateBy) {
        this.userCreateBy = userCreateBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserUpdateBy() {
        return userUpdateBy;
    }

    public void setUserUpdateBy(String userUpdateBy) {
        this.userUpdateBy = userUpdateBy;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

}
