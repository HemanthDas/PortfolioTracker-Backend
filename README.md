# Portfolio Manager Application

## Overview
Portfolio Manager is a Spring Boot application designed to manage a user's stock portfolio. It supports features such as adding, updating, deleting stocks, initializing a portfolio, and calculating portfolio value.

---

## Features

- Add, update, and delete stocks.
- Initialize a portfolio with random stocks.
- Calculate the total value of a user's portfolio.
- Fetch stocks by user ID.
- Fetch user details by ID or username.

---

## Prerequisites

Before running the application, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

---

## Getting Started

### Clone the Repository
```bash
git clone <repository-url>
cd portfolio-manager
```

### Database Configuration

Set up a MySQL database named `portfolio_db`. Update the connection details in the appropriate `application.properties` or `application-prod.properties` file.

#### Default Configuration (`application.properties`):
```properties
spring.application.name=PortfolioManager
spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_db
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.cache.type=simple
spring.profiles.active=${ACTIVEPROFILE}
```

#### Production Configuration (`application-prod.properties`):
```properties
spring.application.name=PortfolioManager
spring.datasource.url=${DBURL}
spring.datasource.username=${DBUSER}
spring.datasource.password=${DBPASS}

spring.jpa.hibernate.ddl-auto=update
server.port=8080

spring.cache.type=simple
```

Ensure you set the environment variables:
- `DBURL` (e.g., `jdbc:mysql://prod-host:3306/prod_db`)
- `DBUSER` (e.g., `prod_user`)
- `DBPASS` (e.g., `prod_password`)

If no profile is set, the application will default to `application.properties`.

### Run the Application

1. Compile and package the application:
   ```bash
   mvn clean package
   ```

2. Run the application:
   ```bash
   java -Dspring.profiles.active=prod -jar target/portfolio-manager-0.0.1-SNAPSHOT.jar
   ```

---

## API Endpoints

### User Endpoints

| Method | Endpoint                   | Description               |
|--------|----------------------------|---------------------------|
| POST   | `/api/users`               | Create a new user         |
| GET    | `/api/users/{id}`          | Get user by ID            |
| GET    | `/api/users/username/{username}` | Get user by username |

### Stock Endpoints

| Method | Endpoint                      | Description                      |
|--------|-------------------------------|----------------------------------|
| POST   | `/api/stocks/{userId}`        | Add a stock to a user's portfolio |
| PUT    | `/api/stocks/{id}`            | Update stock details             |
| DELETE | `/api/stocks/{id}`            | Delete a stock                   |
| GET    | `/api/stocks/user/{userId}`   | Get all stocks for a user        |

### Portfolio Endpoints

| Method | Endpoint                           | Description                     |
|--------|------------------------------------|---------------------------------|
| POST   | `/api/portfolio/initialize/{username}` | Initialize portfolio for a user |
| GET    | `/api/portfolio/value/{userId}`    | Get total portfolio value       |

---

## Error Handling

The application uses structured error handling to return meaningful messages:

- **400 Bad Request:** For validation errors or incorrect input.
- **404 Not Found:** When resources like user or stock are not found.
- **500 Internal Server Error:** For unexpected server-side errors.

---

## Technologies Used

- **Backend:** Spring Boot, Spring Data JPA
- **Database:** MySQL
- **Build Tool:** Maven
- **Cache:** Simple Cache

---

## Contribution

Feel free to fork this repository, make enhancements, and submit a pull request. Ensure that your code adheres to the existing style and is well-tested.

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
