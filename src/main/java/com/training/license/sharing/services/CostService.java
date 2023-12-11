package com.training.license.sharing.services;

import com.training.license.sharing.dto.CostViewDTO;
import com.training.license.sharing.dto.MonthCostDTO;
import com.training.license.sharing.entities.CostView;
import com.training.license.sharing.entities.MonthlyCost;
import com.training.license.sharing.repositories.CostViewRepository;
import com.training.license.sharing.repositories.MonthlyCostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.training.license.sharing.util.InfoMessageUtil.GET_COSTS;
import static com.training.license.sharing.validator.ErrorMessagesUtil.COST_NOT_AVAILABLE;

@Service
@RequiredArgsConstructor
@Log4j2
public class CostService {

    private final CostViewRepository costViewRepository;
    private final MonthlyCostRepository monthlyCostRepository;


    public CostViewDTO getCosts() {
        log.info(GET_COSTS);
        CostView costView = costViewRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(COST_NOT_AVAILABLE));

        List<MonthlyCost> monthlyCosts = monthlyCostRepository.findAll();

        return convertToDTO(costView, monthlyCosts);
    }

    private CostViewDTO convertToDTO(CostView costView, List<MonthlyCost> monthlyCosts) {
        List<MonthCostDTO> monthCostDTOs = monthlyCosts.stream()
                .map(monthCost -> new MonthCostDTO(monthCost.getMonth(), monthCost.getValue()))
                .collect(Collectors.toList());

        return CostViewDTO.builder()
                .totalCostsCurrentYear(costView.getTotalCostsCurrentYear())
                .deltaTotalCosts(costView.getDeltaTotalCosts())
                .software(costView.getSoftware())
                .deltaSoftware(costView.getDeltaSoftware())
                .trainings(costView.getTrainings())
                .deltaTrainings(costView.getDeltaTrainings())
                .costsPerMonth(monthCostDTOs)
                .build();
    }

}
