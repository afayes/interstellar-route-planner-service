# Interstellar Route Planner Service
This application has been built using: 
- Java 25
- Spring Boot 4
- AWS ECS (deployment)
- Terraform (Infrastructure automation)
- GitHub Actions (CI/CD)
- Docker
- Libraries: JGraphT (for Dijkstra's shortest path algorithm), Lombok and H2 database

# Deployed App Links
The applicatin has been deployed to AWS ECS (infra built using Terraform)
- Swagger - http://interstellar-route-planner-alb-1107355989.eu-west-2.elb.amazonaws.com/swagger-ui/index.html
- Opena API spec: http://interstellar-route-planner-alb-1107355989.eu-west-2.elb.amazonaws.com/v3/api-docs
- Health Check: http://interstellar-route-planner-alb-1107355989.eu-west-2.elb.amazonaws.com/actuator/health
- Version Info: http://interstellar-route-planner-alb-1107355989.eu-west-2.elb.amazonaws.com/actuator/info

# Notes and Assumptions
- Edge Case: For cheapest transport endpoint, if Personal Transport cost and HSTC Transport cost is the same then HSTC Transport will be returned for cheapest trasnport
- Edge Case: For cheapest transport endpoint, if days of parking is specified as zero then HSTC Transport will be used. Personal transport requires at least one day of parking. It is assumed that a partial day will be charged as one day of parking as storage costs cannot free. 
- Cheapest trasnport calculation rounds up returned prices up to 2 decimal place

# Requirements
- Java 25 (JDK) - To build and run locally

Optional:
- Terraform - To build AWS ECS infrastructure
- AWS CLI, Docker - To deploy to AWS ECS locally
- GitHub - To deploy using GitHub actions

# Build
```
./mvnw clean install   
```

# Run Tests
To run unit tests
```
./mvnw test
```

To run integration tests
```
./mvnw verify
```

# Run Application
```
./mvnw spring-boot:run  
```

Local Links:
- Swagger: http://localhost:8080/swagger-ui/index.html
- Opena API spec: http://localhost:8080/v3/api-docs
- Health check: http://localhost:8080/actuator/health
- Version Info: http://localhost:8080/actuator/info
