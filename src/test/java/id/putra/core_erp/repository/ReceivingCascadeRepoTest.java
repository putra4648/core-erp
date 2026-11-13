package id.putra.core_erp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import id.putra.core_erp.enums.OrderStatus;
import id.putra.core_erp.modules.inbound.model.Receiving;
import id.putra.core_erp.modules.inbound.model.ReceivingLine;
import id.putra.core_erp.modules.inbound.repository.ReceivingLineRepository;
import id.putra.core_erp.modules.inbound.repository.ReceivingRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Covers the {@link ReceivingRepository} → {@link ReceivingLine} cascade.
 *
 * <p>
 * {@code Receiving} is the aggregate root: its lines have no meaning once the
 * header is
 * gone. If the cascade is lost, a soft-deleted receipt disappears while its
 * lines stay live
 * and queryable, and any line-level reporting starts counting orphaned rows.
 */
@QuarkusTest
class ReceivingCascadeRepoTest {

	@Inject
	ReceivingRepository receivingRepository;

	@Inject
	ReceivingLineRepository receivingLineRepository;

	@Inject
	EntityManager em;

	private Receiving newReceiving(String number) {
		Receiving r = new Receiving();
		r.receivingNumber = number;
		r.status = OrderStatus.PENDING;
		r.receivedDate = java.time.LocalDateTime.now();
		receivingRepository.persist(r);
		return r;
	}

	private ReceivingLine newLine(Receiving receiving, String qtyReceived) {
		ReceivingLine line = new ReceivingLine();
		line.receiving = receiving;
		line.qty = new BigDecimal("10");
		line.qtyReceived = new BigDecimal(qtyReceived);
		line.status = "PENDING_QC";
		line.lotBatch = "LOT-" + receiving.receivingNumber;
		return line;
	}

	@Test
	@TestTransaction
	@DisplayName("soft-deleting a receiving cascades to its lines")
	void softDeleteCascadesToLines() {
		Receiving receiving = newReceiving("REC-CASCADE");
		ReceivingLine first = newLine(receiving, "5");
		receivingLineRepository.persist(first);
		ReceivingLine second = newLine(receiving, "5");
		receivingLineRepository.persist(second);
		em.flush();

		Long receivingId = receiving.id;
		Long firstId = first.id;
		Long secondId = second.id;
		em.clear();

		receivingRepository.softDeleteById(receivingId);
		em.flush();
		em.clear();

		assertNull(receivingRepository.findById(receivingId), "the receipt must be hidden");
		assertTrue(receivingLineRepository.findByIdOptional(firstId).isEmpty(),
				"the first line must cascade to soft-deleted");
		assertTrue(receivingLineRepository.findByIdOptional(secondId).isEmpty(),
				"the second line must cascade to soft-deleted");

		// The rows must physically remain, flagged — not be hard-deleted.
		// Table name: ReceivingLine has a bare @Table, so Hibernate derives it from the
		// entity name — snake_case and pluralised for a compound name.
		assertEquals(Boolean.TRUE, em.createNativeQuery("select deleted from receiving_line where id = ?1")
				.setParameter(1, firstId).getSingleResult());
	}

	@Test
	@TestTransaction
	@DisplayName("a receiving with no lines deletes cleanly")
	void receivingWithoutLinesDeletesCleanly() {
		Receiving receiving = newReceiving("REC-EMPTY");
		em.flush();

		Long id = receiving.id;
		receivingRepository.softDeleteById(id);
		em.flush();
		em.clear();

		assertNull(receivingRepository.findById(id));
		assertEquals(1, receivingRepository.restore(id), "and must be restorable");
	}

	@Test
	@TestTransaction
	@DisplayName("the cascade is scoped to one receiving")
	void cascadeDoesNotTouchOtherReceivings() {
		Receiving target = newReceiving("REC-TARGET");
		ReceivingLine targetLine = newLine(target, "5");
		receivingLineRepository.persist(targetLine);

		Receiving other = newReceiving("REC-OTHER");
		ReceivingLine otherLine = newLine(other, "5");
		receivingLineRepository.persist(otherLine);
		em.flush();

		Long targetId = target.id;
		Long otherLineId = otherLine.id;
		em.clear();

		receivingRepository.softDeleteById(targetId);
		em.flush();
		em.clear();

		assertTrue(receivingLineRepository.findByIdOptional(otherLineId).isPresent(),
				"a line of another receipt must NOT be touched — the cascade must not overshoot");
	}
}
