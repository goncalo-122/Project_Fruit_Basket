# Quarkus Fruit Management API

This project is a microservice developed with the **Quarkus** framework, focused on managing a fruit inventory and a basket system.  
The main purpose of this repository is to serve as a foundation for studying the transition from monolithic architectures to **Microservices**, applying software design patterns and Clean Architecture principles.

---

## Current Status: Service Layer and Transactionality

The project has been structured to ensure **Separation of Concerns**, moving business logic into a dedicated service layer.

### Key Features Implemented:
* **Layered Architecture** — Clear separation between Resource (REST), Service (Business Logic), and Entity (Persistence Model)
* **Dependency Injection (`@Inject`)** — Decoupled components using CDI
* **Transactional Control (`@Transactional`)** — Ensures atomicity (All-or-Nothing) in database operations
* **Data Validation (Bean Validation)** — Using `@Valid`, `@NotBlank`, `@Min`, etc.

---

## Tech Stack
* **Java 17+**
* **Quarkus Framework**
* **Hibernate with Panache** (Active Record Pattern)
* **H2 Database** (In-memory for development)

---

## Evolution Roadmap (Microservices)

Following best practices for distributed architectures, the next steps include:

1. **Phase 1:** Build first microservice + add tests for Resource layer
2. **Phase 2:** Build second microservice + add tests for Resource layer
3. **Phase 3:** Establish communication between both services

### Extended Roadmap (Professional Growth)
* Add unit tests for Service layer
* Add missing Basket operations:
    - **[Remove item](ca://s?q=remove_item_from_basket)**
    - **[Delete basket](ca://s?q=clear_basket)**
      - Note:In this method the quantity allocated in the basket is updated in Fruit microService
    - **[Update item quantity](ca://s?q=update_basket_item_quantity)**
* Add Fault Tolerance (timeouts, retries, circuit breakers)
* Add MapStruct for automatic DTO mapping
* Add Kafka events (stock updated, item added)
* Add Docker + Kubernetes deployment
* Add Observability (logs, metrics, tracing)

---

## Useful Commands

### If you changed dependencies, it's better to use Clean Mode:
```bash
mvn clean quarkus:dev
