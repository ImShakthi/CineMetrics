## ✅ To-Do / Future Enhancements
- ### Implement API Rate Limiting
Prevent abuse and protect system resources by introducing request throttling using libraries like Bucket4j or Spring Cloud Gateway rate limiting.

- ### Migrate to Resilience4J for Resilience Patterns
Replace manual retry logic (if any) with Resilience4J for robust handling of transient failures using circuit breakers, retries, timeouts, and bulkheads.

- ### Use Liquibase `CustomTaskChange` for CSV Import
Cleanly and automatically import initial data (e.g., Oscar winners) into the database using Liquibase’s `CustomTaskChange` interface to avoid ad-hoc scripts.

- ### Display Combined Ratings (OMDb + User Ratings)
Enhance rating insights by aggregating OMDb-provided ratings with user-submitted ratings in API responses. Requires schema and DTO changes.

- ### Add Pagination for Rating and Movie Lists
Prevent large payloads and improve frontend performance by paginating endpoints that return user ratings or movie lists.

- ### Introduce Contract Testing for External APIs (e.g., OMDb)
Use tools like Pact or Spring Cloud Contract to ensure external APIs (e.g., OMDb) behave as expected and notify the team of breaking changes early.

- ### Integrate Structured Logging (Logback/Log4j2 + JSON)
Adopt structured, JSON-based logs to simplify log parsing and integration with observability platforms like ELK.

- ### Add Metrics and Health Endpoints with Spring Boot Actuator
Monitor app health, database connectivity, and runtime metrics (e.g., request count, memory usage) with Actuator. Integrate with Prometheus + Grafana dashboards.

- ### Enable Distributed Tracing with OpenTelemetry
Help trace requests across microservices (or future decomposed services) using OpenTelemetry, allowing better root cause analysis.

- ### Enforce Role-Based Access Control (RBAC)
Fix or enhance RBAC to restrict access based on user roles (e.g., admin vs user). Ensure each endpoint is properly protected with method-level security.

---
## 🕒 If More Time Were Available
- ### Add end-to-end (E2E) tests with Selenium or Playwright
To validate complete user journeys, especially for login, rating, and top movie listing flows.

- ### Introduce feature toggles with Togglz or FF4J
Allow experimentation and safe rollouts of new features like rating aggregation or RBAC.

- ### Add async processing (e.g., Spring Events, Kafka)
For background tasks like logging rating stats or syncing with external APIs.

- ### Implement graceful shutdown and retries on startup
Useful for smoother container orchestration and better cloud-native behavior.

----
