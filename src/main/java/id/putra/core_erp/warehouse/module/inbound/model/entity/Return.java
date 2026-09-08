package id.putra.core_erp.warehouse.module.inbound.model.entity;

import com.vaadin.flow.component.template.Id;

import id.putra.core_erp.purchase_requisition.model.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
public class Return extends BaseModel {
    @Id
    private Long id;
    private String name;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
