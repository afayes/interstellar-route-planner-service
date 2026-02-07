package com.hyperspacetunnelingcorp.routeplanner.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gate_connection")
@IdClass(GateConnectionId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GateConnection {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_gate_id")
    private Gate fromGate;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_gate_id")
    private Gate toGate;

    private int hu;
}

class GateConnectionId implements Serializable {
    private String fromGate;
    private String toGate;
}
