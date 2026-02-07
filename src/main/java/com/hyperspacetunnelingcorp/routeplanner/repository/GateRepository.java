package com.hyperspacetunnelingcorp.routeplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hyperspacetunnelingcorp.routeplanner.model.Gate;

@Repository
public interface GateRepository extends JpaRepository<Gate, String> {

}
