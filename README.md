# BankCore - Distributed Banking Management System 🏦

## Project Description 📋

**BankCore** is a backend system designed with a **microservices architecture** for managing banking operations. The system is composed of independent services that communicate with each other to manage customers, bank accounts, and secure financial transactions.
The main objective is to build a distributed system that demonstrates synchronous communication, shared security via JWT, and cross-validations between services, ensuring transaction integrity.

> **Note:** This project is part of an educational and collaborative initiative to learn backend development best practices. Proposed by Bytes Colaborativos, a developer community.

## Work Methodology 🛠️

We follow an **Agile (Scrum)** methodology for task management.

## Development Team 🫂

- @karlosvassan
- @ELmoliii
- @cristiansdev

## System Architecture 🏗️

The system is divided into two main microservices:

**ms-customers (Port 8081):** Handles customer management, authentication, and authorization.  
**ms-accounts (Port 8082):** Responsible for managing bank accounts and transactions (deposits, withdrawals, transfers). It consumes `ms-customers` for validations.

The defined technology stack for the project is:

- **Java 21+**: Main language.
- **Spring Boot 5.x**: Framework for microservices development.
- **Spring Security + JWT**: Security management and shared tokens.
- **PostgreSQL**: Database (one independent instance per microservice).
- **OpenFeign / RestTemplate**: For synchronous communication between microservices.
- **Docker & Docker Compose**: Environment containerization and orchestration.
- **Swagger UI / OpenAPI 3.0**: Live API documentation.
- **MapStruct & Lombok**: Utilities for mapping and reducing boilerplate code.
- **JUnit 5 + Mockito**: Unit and integration testing.

## Installation and Configuration 🚀

### Prerequisites

- Java 21 or higher
- Docker and Docker Compose
- Maven or Gradle

### Running the Environment with Docker

The project is configured to launch both microservices and their respective databases using Docker Compose.

```bash
# 1. Clone the repository
git clone https://github.com/bytes-colaborativos/banking-management-system
cd banking-management-system

# 2. Start the entire system (Databases and Microservices)
docker-compose up -d

# 3. Verify that the services are running:
# ms-customers: http://localhost:8081
# ms-accounts: http://localhost:8082

```

### Manual Configuration (Without Docker Compose)

If you wish to run the services manually, ensure you configure the environment variables in the `.env` file, and set up the databases accordingly.
`.env.local` is provided as a template for local development.
If you dont provide a `.env` file, the system will use the default values defined in the `docker-compose.yml` for local development and testing.

## Key Features (Scope) ✅

Although development is just starting, these are the core functionalities to be implemented:

- **Customer Management**: Public registration and Authentication (Login) returning a JWT token.
- **Account Management**: Creation of savings/checking accounts with automatic IBAN generation.
- **Transactions**: Deposits and withdrawals with balance validation.
- **Transfers between Accounts**: Complex operation requiring validation of customer existence in the external microservice and ensuring operation atomicity.
- **Transaction History**: Transaction lookup with filters and pagination.

## API Documentation 🔗

Once the application is running, you will be able to access the interactive documentation (Swagger) at the following routes (once implemented):

- **ms-customers:** `http://localhost:8081/swagger-ui/index.html`
- **ms-accounts:** `http://localhost:8082/swagger-ui/index.html`

## Github Actions CI/CD 🔄

The project is configured with GitHub Actions for continuous integration. The workflow includes:

- **Format Code** (`format.yml`): Triggered on every PR to `main`. Automatically applies Spotless formatting across both microservices and commits the changes if any are detected.
- **Protect Main Branch** (`protect_main.yml`): Blocks any PR targeting `main` that does not originate from `develop`, enforcing the `feature → develop → main` branching strategy.
- ** Build and Test** (`build_and_test.yml`): Runs on every PR to `main` and on pushes to `develop`. It builds both microservices and executes all tests, ensuring code quality before merging.
- **Branch Ruleset**: Direct pushes to `main` are blocked at the repository level, ensuring all changes go through a pull request.
