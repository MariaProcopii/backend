package com.training.license.sharing.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.util.UUID;

@Getter
@Setter
@Entity
@Immutable
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table
@Subselect("select uuid_generate_v4() as id, cost_view.* from cost_view")
public class CostView {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "total_costs_current_year")
    private Integer totalCostsCurrentYear;

    @NotNull
    @Column(name = "delta_total_costs")
    private Integer deltaTotalCosts;

    @NotNull
    @Column(name = "software")
    private Integer software;

    @NotNull
    @Column(name = "delta_software")
    private Integer deltaSoftware;

    @NotNull
    @Column(name = "trainings")
    private Integer trainings;

    @NotNull
    @Column(name = "delta_trainings")
    private Integer deltaTrainings;

}
