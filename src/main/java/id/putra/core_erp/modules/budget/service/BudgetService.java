package id.putra.core_erp.modules.budget.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import id.putra.core_erp.modules.budget.model.Budget;
import id.putra.core_erp.modules.budget.model.BudgetDetail;
import id.putra.core_erp.modules.budget.model.BudgetAllocation;
import id.putra.core_erp.modules.budget.repository.BudgetDetailRepo;
import id.putra.core_erp.modules.budget.repository.BudgetRepo;
import id.putra.core_erp.modules.department.model.Department;
import id.putra.core_erp.modules.department.repo.DepartmentRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional(rollbackOn = { ParseException.class })
public class BudgetService {

    private final BudgetRepo budgetRepo;
    private final DepartmentRepo departmentRepo;
    private final BudgetDetailRepo budgetDetailRepo;

    public BudgetService(BudgetRepo budgetRepo, DepartmentRepo departmentRepo, BudgetDetailRepo budgetDetailRepo) {
        this.budgetRepo = budgetRepo;
        this.departmentRepo = departmentRepo;
        this.budgetDetailRepo = budgetDetailRepo;
    }

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

    public Map<String, Object> getBudgets(List<String> deptId) throws ParseException {
        List<Budget> current = new ArrayList<>();
        List<Budget> past = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);

        Date year = simpleDateFormat.parse(new SimpleDateFormat("yyyy").format(calendar.getTime()));

        List<Department> dept = departmentRepo.findAll().list();
        Map<String, Object> result = new HashMap<>();

        budgetRepo.findByDepartmentInAndFiscalYearAfter(dept, year).forEach(budget -> {
            Budget dto = new Budget();

            if (budget.getDepartment() != null) {
                Department Department = new Department();
                Department.setName(budget.getDepartment().getName());
                Department.setCode(budget.getDepartment().getCode());
                dto.setDepartment(Department);
            }

            dto.setId(budget.getId());
            if (budget.getFiscalYear() != null) {
                dto.setFiscalYear(budget.getFiscalYear());
            }
            dto.setCreateBy(budget.getCreateBy());
            dto.setUpdateBy(budget.getUpdateBy());
            if (budget.getCreateAt() != null) {
                dto.setCreateAt(Instant.from(budget.getCreateAt()));
            }
            if (budget.getUpdateAt() != null) {
                dto.setUpdateAt(Instant.from(budget.getUpdateAt()));
            }

            current.add(dto);
        });
        result.put("current", current);

        budgetRepo.findByDepartmentInAndFiscalYearBefore(dept, year).forEach(budget -> {
            Budget dto = new Budget();

            if (budget.getDepartment() != null) {
                Department Department = new Department();
                Department.setName(budget.getDepartment().getName());
                Department.setCode(budget.getDepartment().getCode());
                dto.setDepartment(Department);
            }

            dto.setId(budget.getId());
            if (budget.getFiscalYear() != null) {
                dto.setFiscalYear(budget.getFiscalYear());
            }
            dto.setCreateBy(budget.getCreateBy());
            dto.setUpdateBy(budget.getUpdateBy());
            if (budget.getCreateAt() != null) {
                dto.setCreateAt(Instant.from(budget.getCreateAt()));
            }
            if (budget.getUpdateAt() != null) {
                dto.setUpdateAt(Instant.from(budget.getUpdateAt()));
            }

            past.add(dto);
        });
        result.put("past", past);
        return result;
    }

    public void addOrUpdateBudget(Budget body) throws ParseException {
        Long id = Long.parseLong(body.getId());
        if (budgetRepo.findByIdOptional(id).isPresent()) {
            Budget entity = budgetRepo.findById(id);
            entity.setFiscalYear(body.getFiscalYear());

            List<BudgetDetail> details = entity.getDetails();

            details.clear();
            if (body.getDetails() != null) {
                body.getDetails().forEach(b -> {
                    BudgetDetail budgetDetail = new BudgetDetail();
                    budgetDetail.setName(b.getName());
                    budgetDetail.setId(b.getId());
                    budgetDetail.setType(b.getType());
                    budgetDetail.setNote(b.getNote());

                    if (b.getAllocations() != null) {
                        List<BudgetAllocation> detailDtos = b.getAllocations().stream().map(d -> {
                            BudgetAllocation dtlDto = new BudgetAllocation();
                            dtlDto.setDate(d.getDate());
                            dtlDto.setPlan(d.getPlan());
                            dtlDto.setActual(d.getActual());
                            return dtlDto;
                        }).toList();
                        budgetDetail.setAllocations(detailDtos);
                    }

                    budgetDetail.setBudget(entity);
                    details.add(budgetDetail);
                });
            }
            entity.setDetails(details);
            budgetRepo.persist(entity);
        } else {
            Budget entity = new Budget();
            entity.setId(body.getId());

            if (body.getDepartment() != null && body.getDepartment().getCode() != null) {
                Optional<Department> deptOpt = departmentRepo
                        .findByDeptNumber(body.getDepartment().getCode());
                deptOpt.ifPresent(entity::setDepartment);
            }

            if (body.getFiscalYear() != null) {
                entity.setFiscalYear(body.getFiscalYear());
            }

            List<BudgetDetail> details = new ArrayList<>();
            if (body.getDetails() != null) {
                body.getDetails().forEach(b -> {
                    BudgetDetail budgetDetail = new BudgetDetail();
                    budgetDetail.setName(b.getName());
                    budgetDetail.setId(b.getId());
                    budgetDetail.setType(b.getType());
                    budgetDetail.setNote(b.getNote());

                    if (b.getAllocations() != null) {
                        List<BudgetAllocation> detailDtos = b.getAllocations().stream().map(d -> {
                            BudgetAllocation dtlDto = new BudgetAllocation();
                            dtlDto.setDate(d.getDate());
                            dtlDto.setPlan(d.getPlan());
                            dtlDto.setActual(d.getActual());
                            return dtlDto;
                        }).toList();
                        budgetDetail.setAllocations(detailDtos);
                    }

                    budgetDetail.setBudget(entity);
                    details.add(budgetDetail);
                });
            }
            entity.setDetails(details);
            budgetRepo.persist(entity);
        }
    }

    public List<BudgetDetail> getBudgetsDetail(String deptId, String budgetId, String filter) {
        List<BudgetDetail> dtos = new ArrayList<>();
        budgetDetailRepo.findAllBudgetDetailsByDeptIdAndBudgetIdAndType(deptId, budgetId, filter).forEach(d -> {
            BudgetDetail detailDto = new BudgetDetail();
            detailDto.setId(d.getId());
            detailDto.setName(d.getName());
            detailDto.setType(d.getType());
            detailDto.setNote(d.getNote());

            List<BudgetAllocation> plans = new ArrayList<>();
            if (d.getAllocations() != null) {
                d.getAllocations().forEach(plans::add);
            }
            detailDto.setAllocations(plans);

            dtos.add(detailDto);
        });

        return dtos;
    }

    public BudgetDetail getBudgetDetail(String deptId, String budgetId) {
        BudgetDetail dto = new BudgetDetail();

        Optional<BudgetDetail> detailOpt = budgetDetailRepo.findByBudgetId(budgetId);

        if (detailOpt.isPresent()) {
            BudgetDetail d = detailOpt.get();
            dto.setId(d.getId());
            dto.setName(d.getName());
            dto.setNote(d.getNote());
            dto.setType(d.getType());

            List<BudgetAllocation> plans = new ArrayList<>();
            if (d.getAllocations() != null) {
                d.getAllocations().forEach(plans::add);
            }
            dto.setAllocations(plans);
        }
        return dto;
    }

    public List<BudgetDetail> getBudgetsDetail(String deptId, String year) {
        List<BudgetDetail> dtos = new ArrayList<>();

        budgetDetailRepo.findAllBudgetDetailsByDeptIdAndYear(deptId, year)
                .forEach(d -> {
                    BudgetDetail detailDto = new BudgetDetail();
                    detailDto.setId(d.getId());
                    detailDto.setName(d.getName());
                    detailDto.setType(d.getType());
                    detailDto.setNote(d.getNote());

                    List<BudgetAllocation> plans = new ArrayList<>();
                    if (d.getAllocations() != null) {
                        d.getAllocations().forEach(plans::add);
                    }
                    String currentMonth = new SimpleDateFormat("MMM").format(new Date());
                    BigInteger totalBudget = plans.stream()
                            .filter(data -> data.getDate() != null
                                    && new SimpleDateFormat("MMM").format(data.getDate())
                                            .equalsIgnoreCase(currentMonth))
                            .map(data -> data.getPlan() != null ? data.getPlan() : BigInteger.ZERO).findFirst()
                            .orElse(BigInteger.ZERO);
                    detailDto.setTotalBudget(totalBudget);

                    dtos.add(detailDto);
                });

        return dtos;
    }
}
