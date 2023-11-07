package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.CostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostViewRepository extends JpaRepository<CostView, Long> {
    // Custom methods if needed
}

