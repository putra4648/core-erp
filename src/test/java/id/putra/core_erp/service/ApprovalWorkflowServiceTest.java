package id.putra.core_erp.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.putra.core_erp.enums.ApprovalType;
import id.putra.core_erp.modules.approval.model.ApprovalWorkflow;
import id.putra.core_erp.modules.approval.repository.ApprovalWorkflowRepo;
import id.putra.core_erp.modules.approval.service.ApprovalWorkflowService;
import id.putra.core_erp.support.ServiceDelegationAssertions;
import id.putra.core_erp.support.TestFixtures;

/**
 * Plain JUnit + Mockito: {@link ApprovalWorkflowService} carries no business
 * rules, so there is no CDI,
 * transaction or database involved.
 */
@ExtendWith(MockitoExtension.class)
class ApprovalWorkflowServiceTest {

	@Mock
	ApprovalWorkflowRepo approvalWorkflowRepo;

	@InjectMocks
	ApprovalWorkflowService approvalWorkflowService;

	@Test
	@DisplayName("save persists the entity")
	void savePersists() {
		ApprovalWorkflow entity = TestFixtures.workflow("WF-1", ApprovalType.REQUISITION);

		approvalWorkflowService.save(entity);

		verify(approvalWorkflowRepo).persist(entity);
	}

	@Test
	@DisplayName("delete soft-deletes, never hard-deletes")
	void deleteSoftDeletes() {
		approvalWorkflowService.delete(7L);

		ServiceDelegationAssertions.assertSoftDeleteUsed(approvalWorkflowRepo, 7L);
	}

	@Test
	@DisplayName("findById delegates to the repository")
	void findByIdDelegates() {
		ApprovalWorkflow entity = TestFixtures.workflow("WF-1", ApprovalType.REQUISITION);
		given(approvalWorkflowRepo.findById(3L)).willReturn(entity);

		assertSame(entity, approvalWorkflowService.findById(3L));
	}

	@Test
	@DisplayName("count delegates and does not load rows")
	void countDelegates() {
		given(approvalWorkflowRepo.count()).willReturn(42L);

		assertSame(42L, approvalWorkflowService.count());
		ServiceDelegationAssertions.assertCountNotLoadAll(approvalWorkflowRepo);
	}
}
