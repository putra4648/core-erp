package id.putra.core_erp.purchaserequisition.dto;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BudgetTypeDto {
    @JsonAlias("budget_date")
    @JsonProperty("budget_date")
    private String budgetDateStr;
    private BigInteger plan;
    private BigInteger actual;
}

