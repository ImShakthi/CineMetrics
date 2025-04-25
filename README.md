# CineMetric 🎦

This service integrates with the OMDb API and a provided CSV file to fetch and store movie details locally. It can also
determine whether a given movie title won the Best Picture award. The application supports user management (creation and
deletion), JWT-based authentication (login and logout), movie ratings, and provides an endpoint to retrieve the top 10
highest-rated movies, ranked by box office revenue.

## Core features

- Determine whether a given movie title won the Best Picture award using data from a CSV file
- Fetch movie details from the OMDb API
- Store user ratings on a scale of 1 to 100 (one rating per user per movie)
- Retrieve all ratings for a specific movie title
- List the top **N** highest-rated movies, ranked by box office revenue
- Create and delete user accounts, with JWT based authentication

---

## 📌 Resources

- [Solution](https://github.com/ImShakthi/CineMetrics/blob/main/docs/solution.md)
- [How to run?](https://github.com/ImShakthi/CineMetrics/blob/main/docs/how_to_run.md)
- [How to test?](https://github.com/ImShakthi/CineMetrics/blob/main/docs/how_to_test.md)
- [TODOs](https://github.com/ImShakthi/CineMetrics/blob/main/docs/to_do.md)
- [How to scale?](https://github.com/ImShakthi/CineMetrics/blob/main/docs/scale.md)
- [Assumptions](https://github.com/ImShakthi/CineMetrics/blob/main/docs/assumptions.md)

---

## 🎨 Sequence diagram

- [Login flow](https://github.com/ImShakthi/CineMetrics/blob/main/docs/sequence-diagram/login-journey.png)
- [User creation / deletion flow](https://github.com/ImShakthi/CineMetrics/blob/main/docs/sequence-diagram/user-create-delete-journey-Admin_Initiated_User_Management_Flow__Create___Delete_.png)
- [Movie details flow](https://github.com/ImShakthi/CineMetrics/blob/main/docs/sequence-diagram/movie-details-journey.png)
- [Rating flow](https://github.com/ImShakthi/CineMetrics/blob/main/docs/sequence-diagram/rating-details-journey-Add_Rating_by_Logged_In_User.png)

---

## Techstack used👾

- Java 21
- Spring Boot 3
- Maven 3.9
- Docker
- docker-compose
- Github Actions
- Liquibase
- MapStruct
- MySql 8
- Redis
- JWT
- Jasypt
- Junit 5
- TestContainers
- Heroku
- swagger
- postman
- Wiremock

## 🚀 How to Run the Project

### 🔧 Prerequisites

Ensure the following tools are installed:

- Java 21
- Apache Maven 3.9
- Docker
- Docker Compose
- [Jasypt CLI](https://github.com/jasypt/jasypt/releases/download/jasypt-1.9.3/jasypt-1.9.3-dist.zip) Tool (Only
  required if modifying encrypted properties)

---

### 📁 Environment Variable Setup

The application relies on environment variables for configuration. You can set them in a .env file at the project root.

📝 Example .env file

``` env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/cinemetrics
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password
JASYPT_ENCRYPTOR_PASSWORD=sample-jasypt-encrypt-passwd

SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
```

_⚠️ Make sure to keep sensitive keys (like JASYPT_ENCRYPTOR_PASSWORD) secure and private. Never commit them to version
control._

---

### 📦 Setup and Run

This application is Dockerized using docker-compose.yml, bundling the backend, MySQL, and Redis.

1. Clone the repository

```bash
git clone git@github.com:ImShakthi/CineMetrics.git
cd CineMetrics
```

2. Build the application using Maven

```bash
./mvnw clean package
```

3. Build and start the server with Docker Compose, export all environment variables

```bash
docker-compose up --build
```

4. Access Swagger UI to explore available APIs:

```
http://localhost:8080/swagger-ui/index.html
```

---

### ⚙️ CI/CD

CI/CD is handled via GitHub Actions and Heroku.

- CI/CD Pipeline = [GitHub Actions](https://github.com/ImShakthi/CineMetrics/actions)

- Live Deployment (Heroku)
  https://cinemetrics-api-d21f2e62e3ca.herokuapp.com/swagger-ui/index.html

**_📝 Note: The Heroku server is hosted on a free-tier plan. Expect some delay due to cold starts when inactive._**

