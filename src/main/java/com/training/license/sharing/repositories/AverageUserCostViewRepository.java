package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.AverageUserCostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AverageUserCostViewRepository extends JpaRepository<AverageUserCostView, Long> {
}