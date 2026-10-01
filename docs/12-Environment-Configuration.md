# Environment Configuration

## 1. Environment Overview

The BGV Portal backend is developed as a Spring Boot application and uses environment-specific configuration for application and database connectivity.

Configuration is maintained separately from application business logic wherever possible.

The environment configuration is responsible for:

* Application settings.
* Database connectivity.
* JPA/Hibernate configuration.
* Security-related configuration.
* Email configuration.
* Environment-specific values.

---

## 2. Spring Boot Configuration

The backend uses Spring Boot configuration files to define application settings.

Configuration is maintained through the standard Spring Boot configuration mechanism.

The main configuration file is maintained under:

```text
src/main/resources/
```

---

## 3. Database Configuration

The application uses MySQL as the database.

The database configuration contains the required connection information for:

* Database URL.
* Database username.
* Database password.
* JDBC driver.
* JPA/Hibernate settings.

Example structure:

```properties
spring.datasource.url=jdbc:mysql://<host>:<port>/<database>
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Actual credentials must not be committed to the GitHub repository.

---

## 4. JPA / Hibernate Configuration

The application uses Spring Data JPA with Hibernate.

JPA configuration controls:

* Entity management.
* SQL generation.
* Database schema validation.
* Hibernate behavior.

The project uses schema validation where the database structure is managed separately from automatic Hibernate schema creation.

The configuration should therefore ensure that the application's entities remain consistent with the actual database schema.

---

## 5. JWT Configuration

JWT-related configuration is required for authentication.

Configuration may include:

* JWT secret/key.
* Token expiration configuration.
* Authentication-related settings.

Sensitive JWT secrets must not be committed to the repository.

Example:

```properties
jwt.secret=<secret>
jwt.expiration=<expiration>
```

The actual production secret should be provided through secure environment configuration.

---

## 6. Email Configuration

The application uses email functionality for relevant user workflows.

Email configuration may contain:

* SMTP host.
* SMTP port.
* SMTP username.
* SMTP password.
* Mail authentication settings.
* TLS settings.

Example structure:

```properties
spring.mail.host=<smtp-host>
spring.mail.port=<smtp-port>
spring.mail.username=<email>
spring.mail.password=<password>
```

Email credentials are sensitive and must not be committed to GitHub.

---

## 7. Frontend Configuration

The React frontend communicates with the backend using the backend API base URL.

The frontend configuration should allow the API URL to be changed between environments without modifying application logic.

Example:

```text
Development
    │
    ▼
Local Spring Boot API

Production
    │
    ▼
AWS / Production API
```

The frontend API base URL should be maintained through environment-specific configuration.

---

## 8. Development Environment

The development environment is used for local application development and API testing.

Typical development components include:

```text
React Frontend
      │
      ▼
Spring Boot Backend
      │
      ▼
Local MySQL Database
```

Postman is used to test backend APIs during development.

---

## 9. Production Environment

The production environment will contain the deployed backend, frontend, database, and required supporting services.

The production environment is planned to use AWS for deployment.

Production configuration must be separated from local development configuration.

Production secrets should be provided through secure environment variables or an appropriate secrets-management mechanism.

---

## 10. Environment Separation

The application should support separate configurations for different environments.

Typical environments are:

```text
Development
    │
    ├── Local Database
    ├── Local Backend
    └── Local Frontend

Production
    │
    ├── Production Database
    ├── Deployed Backend
    └── Deployed Frontend
```

Environment-specific values should not be hard-coded into Java or React business logic.

---

## 11. Sensitive Configuration

The following information must be treated as sensitive:

* Database passwords.
* JWT secrets.
* Email passwords.
* SMTP credentials.
* Production API credentials.
* Cloud credentials.
* Other authentication secrets.

These values should not be committed to GitHub.

---

## 12. GitHub Configuration Rules

The repository should contain configuration templates or example files where necessary, but should not contain actual production secrets.

Sensitive files and local environment files should be excluded through `.gitignore` where appropriate.

Before pushing changes to GitHub, verify that:

* No database password is included.
* No JWT secret is included.
* No email password is included.
* No AWS credentials are included.
* No other production secrets are included.

---

## 13. Configuration During Development

When moving the project to another development machine:

1. Clone the GitHub repository.
2. Configure the local Java/Spring Boot environment.
3. Configure MySQL.
4. Create or restore the required database.
5. Configure local database credentials.
6. Configure JWT settings.
7. Configure email settings if email functionality is required.
8. Start the Spring Boot backend.
9. Start the React frontend.
10. Import the Postman collection for API testing.

The database schema and Postman collection should be maintained alongside the project so development can be resumed on another machine.

---

## 14. Configuration Principle

Environment-specific configuration should remain separate from application code.

The application should be able to move between environments by changing configuration values rather than modifying business logic.

The same application code should therefore be usable across:

```text
Local Development
       │
       ▼
Testing
       │
       ▼
Production
```
