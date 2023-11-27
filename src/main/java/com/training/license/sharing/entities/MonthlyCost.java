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
@Subselect("select uuid_generate_v4() as id, monthly_costs_view.* from monthly_costs_view")
public class MonthlyCost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "month")
    private String month;

    @NotNull
    @Column(name = "value")
    private Integer value;

}
