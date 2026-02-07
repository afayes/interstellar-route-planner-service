package com.hyperspacetunnelingcorp.routeplanner.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gate {
    @Id
    private String id;
    private String name;

    @OneToMany(mappedBy = "fromGate", fetch = FetchType.LAZY)
    private List<GateConnection> connections = new ArrayList<>();
}