## 🚀 How to Run the Project
### 🔧 Prerequisites
Ensure the following tools are installed:
- Java 21 
- Apache Maven 3.9 
- Docker 
- Docker Compose 
- [Jasypt CLI](https://github.com/jasypt/jasypt/releases/download/jasypt-1.9.3/jasypt-1.9.3-dist.zip) Tool (Only required if modifying encrypted properties)

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
_⚠️ Make sure to keep sensitive keys (like JASYPT_ENCRYPTOR_PASSWORD) secure and private. Never commit them to version control._

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

