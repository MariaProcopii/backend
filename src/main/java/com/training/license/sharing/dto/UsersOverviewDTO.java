package com.training.license.sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UsersOverviewDTO {
    private int totalUsers;
    private int totalDisciplines;
    private int deltaUsers;
    private List<DisciplineUserCountDTO> disciplines;
}
