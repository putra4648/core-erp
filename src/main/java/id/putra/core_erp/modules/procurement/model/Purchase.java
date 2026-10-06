package id.putra.core_erp.modules.procurement.model;

import java.util.Date;

import id.putra.core_erp.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
<<<<<<< HEAD
@Table(name = "purchase")
public class Purchase {
	private Date date;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
=======
@Table
public class Purchase extends BaseEntity {

	private Date date;
>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9

	public Date getDate() {
		return date;
	}

<<<<<<< HEAD
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

=======
	public void setDate(Date date) {
		this.date = date;
	}

	// @OneToOne(cascade = CascadeType.ALL)
	// @JoinColumn(name = "status_id", referencedColumnName = "status_id", nullable
	// = false)
	// private Status status;

>>>>>>> 345b3544f6af463202f967db0e990de1c0726cd9
	// @OneToOne(cascade = CascadeType.ALL)
	// @JoinColumn(name = "approval", referencedColumnName = "nrp")
	// private Users users;
}
