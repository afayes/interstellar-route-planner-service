package com.hyperspacetunnelingcorp.routeplanner.service;

import java.math.BigDecimal;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.hyperspacetunnelingcorp.routeplanner.exception.GateNotFoundException;
import com.hyperspacetunnelingcorp.routeplanner.exception.RouteNotFoundException;
import com.hyperspacetunnelingcorp.routeplanner.model.CheapestRoute;
import com.hyperspacetunnelingcorp.routeplanner.model.Gate;
import com.hyperspacetunnelingcorp.routeplanner.repository.GateRepository;

@Service
public class RouteService {

    private static final BigDecimal COST_PER_PASSENGER_PER_HU = BigDecimal.valueOf(0.10);
   
    private final GateRepository gateRepository;
    private final SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph;
    private DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraSpf;

    public RouteService(GateRepository gateRepository) {
        this.gateRepository = gateRepository;
        this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }   


    public CheapestRoute getCheapestRoute(String source, String destination) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }

        if (destination == null || destination.isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }

        if (!graph.containsVertex(source)) {
            throw new GateNotFoundException(source);
        }

        if (!graph.containsVertex(destination)) {
            throw new GateNotFoundException(destination);
        }

        GraphPath<String, DefaultWeightedEdge> path  = dijkstraSpf.getPath(source, destination);

        if (path == null) {
            throw new RouteNotFoundException(source, destination);
        }

        return new CheapestRoute(path.getVertexList(), (int) path.getWeight(), BigDecimal.valueOf(path.getWeight()).multiply(COST_PER_PASSENGER_PER_HU));
    }

    /**
     * Build the graph and the dijkstra algorithm once so that is not built on each request
     * Build when the application is ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    void buildGraph() {
        List<Gate> graphInternal = gateRepository.findAllWithConnections();
        
        graphInternal.forEach( gate -> {
            graph.addVertex(gate.getId());
        });

        graphInternal.forEach(gate -> {
            gate.getConnections().forEach(connection -> {
                DefaultWeightedEdge edge = graph.addEdge(gate.getId(), connection.getToGate().getId());
                graph.setEdgeWeight(edge, connection.getHu());
            });
        });

        dijkstraSpf = new DijkstraShortestPath<>(graph);
    }
}
