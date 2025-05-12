# wallet-service-assignment
Application that manages users' money. This service will support operations for depositing, withdrawing, and transferring funds between users.

## Technologies
- Java 21
- Spring Boot 3.4.5
- H2 Database
- Spring Data JPA

## Instructions to run the application

1. Download and install JDK 21
2. Clone the repository
3. Navigate to the project directory
4. Run the application using the command:
   ```bash
   ./gradlew bootRun
   ```
5. Access the application at `http://localhost:8080`

## API Documentation
- The API documentation is available at `http://localhost:8080/swagger-ui/index.html`

## Design Choices
The application is designed to be modular, with separate packages for controllers, services, and repositories. It uses Spring Data JPA for database interactions, providing a clean and efficient way to manage data. H2 in-memory database is used for development and testing purposes, making it easy to set up and run without external dependencies. It also includes Swagger for API documentation, making it easier to understand and test the endpoints.

## Functional Requirements
- Create wallet: Allows a user to create a new wallet on endpoint `POST /wallet`
- Get wallet balance: Allows a user to check the balance of their wallet on endpoint `GET /wallet/{id}/balance`
- Deposit funds: Allows a user to deposit money into their wallet on endpoint `POST /wallet/{id}/deposit/{amount}`
- Withdraw funds: Allows a user to withdraw money from their wallet on endpoint `POST /wallet/{id}/withdraw/{amount}`
- Transfer funds: Allows a user to transfer money from one wallet to another on endpoint `POST /wallet/{id}/transfer/{amount}/recipient/{recipientId}`
- Get historical balance: Allows a user to view their balance on a specific date and time on endpoint `GET /wallet/{id}/date/{date}/balance`

## Non-Functional Requirements
Logs are implemented using SLF4J to audit and track this mission-critical application.

## Compromises
- Due to time constraints, the application does not include user authentication and authorization. In a production environment, it is crucial to implement security measures to protect sensitive data and ensure that only authorized users can access certain endpoints.
- Integration tests are not included in this version of the application. In a production environment, it is important to have comprehensive tests to ensure that all components work together as expected.
- The project is not fully covered by unit tests. In a production environment, it is important to have a high level of test coverage to ensure that the application behaves as expected and to catch any potential bugs early in the development process.