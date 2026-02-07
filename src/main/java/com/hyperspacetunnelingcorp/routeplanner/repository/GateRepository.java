package com.hyperspacetunnelingcorp.routeplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hyperspacetunnelingcorp.routeplanner.model.Gate;

@Repository
public interface GateRepository extends JpaRepository<Gate, String> {

    /**
     * Find all gates joined with their connections to avoid N+1 queries issue
     * @return
     */
    @Query("SELECT DISTINCT g FROM Gate g LEFT JOIN FETCH g.connections")
    List<Gate> findAllWithConnections();

}
