package com.training.license.sharing.repositories;

import com.training.license.sharing.entities.User;
import com.training.license.sharing.entities.enums.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.discipline = :#{#discipline} " +
            "AND u.name = :#{#name}")
    Optional<User> findByUsernameAndDiscipline(@Param("name") String name, @Param("discipline") Discipline discipline);
}
