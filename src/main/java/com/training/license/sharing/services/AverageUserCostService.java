package com.training.license.sharing.services;

import com.training.license.sharing.dto.AverageUserCostResponseDTO;
import com.training.license.sharing.dto.DisciplineCostDTO;
import com.training.license.sharing.entities.AverageUserCostView;
import com.training.license.sharing.repositories.AverageUserCostViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.training.license.sharing.util.InfoMessageUtil.GET_AVERAGE_COSTS;

@Service
@RequiredArgsConstructor
@Log4j2
public class AverageUserCostService {
    private final AverageUserCostViewRepository repository;

    public AverageUserCostResponseDTO getAverageUserCosts() {
        log.info(GET_AVERAGE_COSTS);
        List<AverageUserCostView> entities = repository.findAll();
        if (entities.isEmpty()) {
            return new AverageUserCostResponseDTO(0, Collections.emptyList());
        }

        Integer commonCalculation = entities.get(0).getCalculation();
        List<DisciplineCostDTO> disciplineCosts = entities.stream()
                .sorted(Comparator.comparing(AverageUserCostView::getDisciplineName))
                .map(entity -> DisciplineCostDTO.builder()
                        .disciplineName(entity.getDisciplineName())
                        .averageCostsUserDiscipline(entity.getAverageCostsUserDiscipline())
                        .build())
                .collect(Collectors.toList());

        return AverageUserCostResponseDTO.builder()
                .calculation(commonCalculation)
                .disciplineCosts(disciplineCosts)
                .build();
    }

}
