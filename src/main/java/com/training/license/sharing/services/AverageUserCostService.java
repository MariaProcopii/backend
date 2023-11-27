package com.training.license.sharing.services;

import com.training.license.sharing.dto.AverageUserCostResponseDTO;
import com.training.license.sharing.dto.DisciplineCostDTO;
import com.training.license.sharing.entities.AverageUserCostView;
import com.training.license.sharing.repositories.AverageUserCostViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AverageUserCostService {
    private final AverageUserCostViewRepository repository;

    public AverageUserCostResponseDTO getAverageUserCosts() {
        List<AverageUserCostView> entities = repository.findAll();
        if (entities.isEmpty()) {
            return new AverageUserCostResponseDTO(0, Collections.emptyList());
        }

        Integer commonCalculation = entities.get(0).getCalculation();
        List<DisciplineCostDTO> disciplineCosts = entities.stream()
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
