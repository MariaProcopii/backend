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
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "average_user_cost_view")
@Subselect("select uuid_generate_v4() as id, average_user_cost_view.* from average_user_cost_view")
public class AverageUserCostView {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "calculation")
    private Integer calculation;

    @NotNull
    @Column(name = "discipline_name")
    private String disciplineName;

    @NotNull
    @Column(name = "average_costs_user_discipline")
    private Integer averageCostsUserDiscipline;

}