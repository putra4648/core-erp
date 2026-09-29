package id.putra.core_erp.modules.procurement.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchase")
public class Purchase {
	private Date date;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public Date getDate() {
		return date;
	}

	public Integer getId() {
		return id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	// @OneToOne(cascade = CascadeType.ALL)
	// @JoinColumn(name = "status_id", referencedColumnName = "status_id", nullable
	// = false)
	// private Status status;

	// @OneToOne(cascade = CascadeType.ALL)
	// @JoinColumn(name = "approval", referencedColumnName = "nrp")
	// private Users users;
}
