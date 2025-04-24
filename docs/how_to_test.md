# 🧪 How to Test the CineMetrics Service
CineMetrics supports both unit tests and integration tests. Integration testing is designed to simulate real-world usage, and includes full journey validation for core features like user management, movie ratings, and authentication.

The integration test class `com.skthvl.cinemetrics.service.UserAccountServiceTest` demonstrates a complete user journey.

----
## ✅ Run All Tests
```bash
./mvnw clean test
```

## 🧱 Integration Testing Stack
- ✅ [Testcontainers](https://www.testcontainers.org/) – Spins up isolated MySQL and Redis containers during integration tests
- ✅ [WireMock](http://wiremock.org/) – Mocks external HTTP calls to the OMDb API for reliable, repeatable test scenarios

_These tools ensure tests are environment-independent and do not rely on external services or shared databases._

## 🚀 Run the Server Locally
To run the app locally, build the project and start the services using Docker Compose:
```bash
./mvnw clean package
docker-compose up --build
```

## 📬 Test the API
You can test the REST APIs via:
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Postman Collection: Import from the file: `docs/cinemetrics-api.postman_collection.json`