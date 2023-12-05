package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Discipline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u) FROM User u")
    int countUsers();

    @Query("SELECT COUNT(DISTINCT u.discipline) FROM User u")
    int countTotalDisciplines();

    int countByLastActiveLessThan(int currentDayOfMonth);

    @Query("SELECT u.discipline, COUNT(u) FROM User u WHERE u.discipline " +
            "IS NOT NULL GROUP BY u.discipline")
    Page<Object[]> getUsersPerDiscipline(Pageable pageable);

    @Query("SELECT u.discipline, COUNT(u) FROM User u GROUP BY u.discipline")
    List<Object[]> getUsersPerDiscipline();

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.discipline = :#{#discipline} " +
            "AND u.name = :#{#name}")
    Optional<User> findByUsernameAndDiscipline(@Param("name") String name, @Param("discipline") Discipline discipline);
}
