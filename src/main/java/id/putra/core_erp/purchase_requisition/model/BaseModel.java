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
    private String createBy;

    @Column(name = "created_date")
    @CreationTimestamp(source = SourceType.DB)
    private Instant createDate;

    @Column(name = "updated_by")
    private String updateBy;

    @Column(name = "updated_date")
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updateDate;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String userCreateBy) {
        this.createBy = userCreateBy;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createdDate) {
        this.createDate = createdDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String userUpdateBy) {
        this.updateBy = userUpdateBy;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updatedDate) {
        this.updateDate = updatedDate;
    }

}
