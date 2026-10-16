package id.putra.core_erp.modules.inbound.service;

import java.util.List;

import id.putra.core_erp.modules.inbound.model.QualityInspection;
import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import id.putra.core_erp.modules.inbound.repository.QualityInspectionRepository;
import id.putra.core_erp.modules.inbound.repository.ReceivingLineRepository;
import id.putra.core_erp.service.BaseService;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class InspectionService implements BaseService<QualityInspection> {
	private final QualityInspectionRepository qualityInspectionRepository;
	private final ReceivingLineRepository receivingLineRepository;

	public InspectionService(QualityInspectionRepository qualityInspectionRepository,
			ReceivingLineRepository receivingLineRepository) {
		this.qualityInspectionRepository = qualityInspectionRepository;
		this.receivingLineRepository = receivingLineRepository;
	}

	@Override
	public void save(QualityInspection entity) {
		qualityInspectionRepository.persist(entity);
	}

	@Override
	public void update(QualityInspection dto) {
		qualityInspectionRepository.getEntityManager().merge(dto);
	}

	@Override
	public void delete(Long id) {
		qualityInspectionRepository.deleteById(id);
	}

	@Override
	public QualityInspection findById(Long id) {
		return qualityInspectionRepository.findById(id);
	}

	@Override
	public List<QualityInspection> findAll(QualityInspection qi, Page page) {
		return qualityInspectionRepository.findAll().list();
	}

	/**
	 * Records a quality inspection result for a receiving line and advances the
	 * line status accordingly (P2P step: "Quality Inspection").
	 * <p>
	 * On {@code PASSED} the receiving line becomes eligible for putaway; on
	 * {@code REJECTED} the line is flagged for return-to-vendor handling.
	 *
	 * @param receivingLineId the dock receiving line inspected
	 * @param result          the QC verdict (e.g. {@code PASSED} / {@code REJECTED})
	 * @param remarks         optional inspector notes
	 */
	public void inspect(Long receivingLineId, String result, String remarks) {
		ReceivingLine line = receivingLineRepository.findById(receivingLineId);
		if (line == null) {
			throw new RuntimeException("Receiving line not found");
		}

		QualityInspection qi = new QualityInspection();
		qi.receivingLine = line;
		qi.result = result;
		qi.remarks = remarks;
		qualityInspectionRepository.persist(qi);

		line.status = "PASSED".equalsIgnoreCase(result) ? "PASSED" : "REJECTED";
		receivingLineRepository.getEntityManager().merge(line);
	}

}

