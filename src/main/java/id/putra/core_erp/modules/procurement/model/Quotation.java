package id.putra.core_erp.modules.procurement.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import id.putra.core_erp.entity.BaseEntity;
import id.putra.core_erp.modules.supplier.model.Supplier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
public class Quotation extends BaseEntity {

	@Column(name = "due_date")
	public Date dueDate;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "suppliers", columnDefinition = "jsonb")
	public List<Supplier> suppliers;

	@Column(name = "note")
	public String note;

}
