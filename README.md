# Payments-demo

This project was developed to demonstrate a service that will provide REST endpoints
to Fetch/Create/Update/Delete payment resources and persist their state. (e.g. to a database)

## Getting started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Depending on how you want to build/run the application (methods below) you might need to have [docker](https://docs.docker.com/) & [docker-compose](https://docs.docker.com/compose/) installed 
The application itself is using Java 8, so you need to have a JDK installed of that version to build/run without Docker.
If you have chosen the Docker/Docker Compose method, you will <b>not</b> need to install anything as it is handled by Docker.

### Building and running

1. Clone or download the repository

2. Build & Run

    At the root of the project:
    * Without Docker/Docker Compose
        * Build
        ```
        mvn clean package 
        ```
        * Run
        ```
        java -jar target/
        ```
        or
        ```
        mvn spring-boot:run
        ```
        
        This will only run the application itself, without any external service or middleware
    * With Docker/Docker Compose
        ```
        docker-compose up 
        ```
        or if you want to rebuild the docker image:
        ```
        docker-compose up --build
        ```
        This method is using Docker's MultiStage build which creates an intermediary container with the build dependencies 
        to build the artifact which is than copied to the run-container and a Docker image is created out of it.
        This way you do not have to install anything besides Docker and Docker Compose does that for you inside containers.

### Endpoints
  - the application API: http://localhost:8080/api/payments
  - the application metrics: http://localhost:8080/metrics
  - Swagger* API documentation: http://localhost:8080/swagger-ui.html
  - HAL browser*: http://localhost:8080/api
  - Mongo Express* to manage MongoDB: http://localhost:8081/

(* For demonstration purposes. Can be disabled for production deployments!)

## Tests
Automated integration tests are using an embedded MongoDB and sample data (payments.json). These should pass when building the artifact. 

## Built with

* [Spring](https://spring.io/) - The web framework
* [Spring Data](https://projects.spring.io/spring-data/) - Store-specific features and capabilities
* [Spring Data MongoDB](https://projects.spring.io/spring-data-mongodb/) - store-specific features and capabilities for [MongoDB](https://www.mongodb.com/)
* [Spring Data REST](https://projects.spring.io/spring-data-rest/) - REST web services on top of Spring Data repositories
* [Maven](https://maven.apache.org/) - Dependency management
* [Docker](https://www.docker.com) & [Docker Compose](https://www.docker.com) - Containerization & orchestration
* [MongoDB](https://www.mongodb.com/) - Database
* [Mongo Express](https://github.com/mongo-express/mongo-express) - Web-based MongoDB admin interface
* [HAL browser](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#_the_hal_browser) - An API browser for the hal+json media type


## Author

**Miklos Barabas** - [GitHub](https://github.com/miklosbarabas)
