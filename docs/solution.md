# CineMetric 🎦
This service integrates with the OMDb API and a provided CSV file to fetch and store movie details locally. It can also determine whether a given movie title won the Best Picture award. The application supports user management (creation and deletion), JWT-based authentication (login and logout), movie ratings, and provides an endpoint to retrieve the top 10 highest-rated movies, ranked by box office revenue.

## Core features
- Determine whether a given movie title won the Best Picture award using data from a CSV file
- Fetch movie details from the OMDb API
- Store user ratings on a scale of 1 to 100 (one rating per user per movie)
- Retrieve all ratings for a specific movie title
- List the top **N** highest-rated movies, ranked by box office revenue
- Create and delete user accounts, with JWT based authentication

## Techstack
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
