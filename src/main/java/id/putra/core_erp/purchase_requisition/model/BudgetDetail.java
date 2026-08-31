package id.putra.core_erp.purchase_requisition.model;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.purchase_requisition.dto.BudgetTypeDto;
import id.putra.core_erp.purchase_requisition.enums.BudgetEnum;
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
@Table(name = "budget_detail")
public class BudgetDetail extends BaseModel {

    @Id
    @Column(name = "budget_no", nullable = false, unique = true)
    private String budgetNo;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private BudgetEnum type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "budgets")
    private List<BudgetTypeDto> details;

    @Column(name = "note")
    private String note;

    @ManyToOne(optional = false)
    @JoinColumn(name = "budget_id", referencedColumnName = "id")
    private Budget budget;
}
