package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.MonthlyCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyCostRepository extends JpaRepository<MonthlyCost, String> {
}
