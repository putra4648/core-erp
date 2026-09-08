package id.putra.core_erp.purchase_requisition.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import id.putra.core_erp.purchase_requisition.dto.BudgetDetailDto;
import id.putra.core_erp.purchase_requisition.dto.BudgetDto;
import id.putra.core_erp.purchase_requisition.dto.BudgetTypeDto;
import id.putra.core_erp.purchase_requisition.dto.DepartmentDto;
import id.putra.core_erp.purchase_requisition.model.Budget;
import id.putra.core_erp.purchase_requisition.model.BudgetDetail;
import id.putra.core_erp.purchase_requisition.model.Department;
import id.putra.core_erp.purchase_requisition.repository.BudgetDetailRepo;
import id.putra.core_erp.purchase_requisition.repository.BudgetRepo;
import id.putra.core_erp.purchase_requisition.repository.DepartmentRepo;
import id.putra.core_erp.purchase_requisition.service.BudgetService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional(rollbackOn = { ParseException.class })
public class BudgetServiceImpl implements BudgetService {

    @Inject
    private BudgetRepo budgetRepo;

    @Inject
    private DepartmentRepo departmentRepo;

    @Inject
    private BudgetDetailRepo budgetDetailRepo;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

    @Override
    public Map<String, Object> getBudgets(List<String> deptId) throws ParseException {
        List<BudgetDto> current = new ArrayList<>();
        List<BudgetDto> past = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);

        Date year = simpleDateFormat.parse(new SimpleDateFormat("yyyy").format(calendar.getTime()));

        List<Department> dept = departmentRepo.findAll().list();
        Map<String, Object> result = new HashMap<>();

        budgetRepo.findByDepartmentInAndFiscalYearAfter(dept, year).forEach(budget -> {
            BudgetDto dto = new BudgetDto();

            if (budget.getDepartment() != null) {
                DepartmentDto departmentDto = new DepartmentDto();
                departmentDto.setDept_name(budget.getDepartment().getDeptName());
                departmentDto.setDept_number(budget.getDepartment().getDeptNumber());
                dto.setDepartment(departmentDto);
            }

            dto.setId(budget.getId());
            if (budget.getFiscalYear() != null) {
                dto.setFy(simpleDateFormat.format(budget.getFiscalYear()));
            }
            dto.setCreated_by(budget.getUserCreateBy());
            dto.setUpdated_by(budget.getUserUpdateBy());
            if (budget.getCreatedDate() != null) {
                dto.setCreated_date(Date.from(budget.getCreatedDate()));
            }
            if (budget.getUpdatedDate() != null) {
                dto.setUpdated_date(Date.from(budget.getUpdatedDate()));
            }

            current.add(dto);
        });
        result.put("current", current);

        budgetRepo.findByDepartmentInAndFiscalYearBefore(dept, year).forEach(budget -> {
            BudgetDto dto = new BudgetDto();

            if (budget.getDepartment() != null) {
                DepartmentDto departmentDto = new DepartmentDto();
                departmentDto.setDept_name(budget.getDepartment().getDeptName());
                departmentDto.setDept_number(budget.getDepartment().getDeptNumber());
                dto.setDepartment(departmentDto);
            }

            dto.setId(budget.getId());
            if (budget.getFiscalYear() != null) {
                dto.setFy(simpleDateFormat.format(budget.getFiscalYear()));
            }
            dto.setCreated_by(budget.getUserCreateBy());
            dto.setUpdated_by(budget.getUserUpdateBy());
            if (budget.getCreatedDate() != null) {
                dto.setCreated_date(Date.from(budget.getCreatedDate()));
            }
            if (budget.getUpdatedDate() != null) {
                dto.setUpdated_date(Date.from(budget.getUpdatedDate()));
            }

            past.add(dto);
        });
        result.put("past", past);
        return result;
    }

    @Override
    public void addOrUpdateBudget(BudgetDto body) throws ParseException {
        Long id = Long.parseLong(body.getId());
        if (budgetRepo.findByIdOptional(id).isPresent()) {
            Budget entity = budgetRepo.findById(id);
            entity.setFiscalYear(simpleDateFormat.parse(body.getFy()));

            List<BudgetDetail> details = entity.getDetails();

            details.clear();
            if (body.getDetails() != null) {
                body.getDetails().forEach(b -> {
                    BudgetDetail budgetDetail = new BudgetDetail();
                    budgetDetail.setName(b.getName());
                    budgetDetail.setId(b.getId());
                    budgetDetail.setType(b.getType());
                    budgetDetail.setNote(b.getNote());

                    if (b.getPlans() != null) {
                        List<BudgetTypeDto> detailDtos = b.getPlans().stream().map(d -> {
                            BudgetTypeDto dtlDto = new BudgetTypeDto();
                            dtlDto.setBudgetDateStr(d.getBudgetDateStr());
                            dtlDto.setPlan(d.getPlan());
                            dtlDto.setActual(d.getActual());
                            return dtlDto;
                        }).toList();
                        budgetDetail.setDetails(detailDtos);
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

            if (body.getDepartment() != null && body.getDepartment().getDept_number() != null) {
                Optional<Department> deptOpt = departmentRepo
                        .findByDeptNumber(body.getDepartment().getDept_number());
                deptOpt.ifPresent(entity::setDepartment);
            }

            if (body.getFy() != null) {
                entity.setFiscalYear(simpleDateFormat.parse(body.getFy()));
            }

            List<BudgetDetail> details = new ArrayList<>();
            if (body.getDetails() != null) {
                body.getDetails().forEach(b -> {
                    BudgetDetail budgetDetail = new BudgetDetail();
                    budgetDetail.setName(b.getName());
                    budgetDetail.setId(b.getId());
                    budgetDetail.setType(b.getType());
                    budgetDetail.setNote(b.getNote());

                    if (b.getPlans() != null) {
                        List<BudgetTypeDto> detailDtos = b.getPlans().stream().map(d -> {
                            BudgetTypeDto dtlDto = new BudgetTypeDto();
                            dtlDto.setBudgetDateStr(d.getBudgetDateStr());
                            dtlDto.setPlan(d.getPlan());
                            dtlDto.setActual(d.getActual());
                            return dtlDto;
                        }).toList();
                        budgetDetail.setDetails(detailDtos);
                    }

                    budgetDetail.setBudget(entity);
                    details.add(budgetDetail);
                });
            }
            entity.setDetails(details);
            budgetRepo.persist(entity);
        }
    }

    @Override
    public List<BudgetDetailDto> getBudgetsDetail(String deptId, String budgetId, String filter) {
        List<BudgetDetailDto> dtos = new ArrayList<>();
        budgetDetailRepo.findAllBudgetDetailsByDeptIdAndBudgetIdAndType(deptId, budgetId, filter).forEach(d -> {
            BudgetDetailDto detailDto = new BudgetDetailDto();
            detailDto.setId(d.getId());
            detailDto.setName(d.getName());
            detailDto.setType(d.getType());
            detailDto.setNote(d.getNote());

            List<BudgetTypeDto> plans = new ArrayList<>();
            if (d.getDetails() != null) {
                d.getDetails().forEach(plans::add);
            }
            detailDto.setPlans(plans);

            dtos.add(detailDto);
        });

        return dtos;
    }

    @Override
    public BudgetDetailDto getBudgetDetail(String deptId, String budgetId) {
        BudgetDetailDto dto = new BudgetDetailDto();

        Optional<BudgetDetail> detailOpt = budgetDetailRepo.findByBudgetId(budgetId);

        if (detailOpt.isPresent()) {
            BudgetDetail d = detailOpt.get();
            dto.setId(d.getId());
            dto.setName(d.getName());
            dto.setNote(d.getNote());
            dto.setType(d.getType());

            List<BudgetTypeDto> plans = new ArrayList<>();
            if (d.getDetails() != null) {
                d.getDetails().forEach(plans::add);
            }
            dto.setPlans(plans);
        }
        return dto;
    }

    @Override
    public List<BudgetDetailDto> getBudgetsDetail(String deptId, String year) {
        List<BudgetDetailDto> dtos = new ArrayList<>();

        budgetDetailRepo.findAllBudgetDetailsByDeptIdAndYear(deptId, year)
                .forEach(d -> {
                    BudgetDetailDto detailDto = new BudgetDetailDto();
                    detailDto.setId(d.getId());
                    detailDto.setName(d.getName());
                    detailDto.setType(d.getType());
                    detailDto.setNote(d.getNote());

                    List<BudgetTypeDto> plans = new ArrayList<>();
                    if (d.getDetails() != null) {
                        d.getDetails().forEach(plans::add);
                    }
                    String currentMonth = new SimpleDateFormat("MMM").format(new Date());
                    BigInteger totalBudget = plans.stream()
                            .filter(data -> data.getBudgetDateStr() != null
                                    && data.getBudgetDateStr().equalsIgnoreCase(currentMonth))
                            .map(data -> data.getPlan() != null ? data.getPlan() : BigInteger.ZERO).findFirst()
                            .orElse(BigInteger.ZERO);
                    detailDto.setTotal_budget(String.valueOf(totalBudget));

                    dtos.add(detailDto);
                });

        return dtos;
    }
}
