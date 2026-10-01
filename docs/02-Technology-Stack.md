# Technology Stack

## 1. Backend

### Java

Java is used as the primary programming language for the backend application.

### Spring Boot

Spring Boot is used to develop the REST-based backend application and provide the application structure, dependency management, configuration, and web services.

### Spring Data JPA / Hibernate

Spring Data JPA is used for database access and repository development.

Hibernate is used as the JPA implementation for:

* Entity mapping
* Database relationships
* CRUD operations
* Query execution
* Transaction management

### Spring Security

Spring Security is used for:

* Authentication
* Authorization
* Role-based access control
* Securing REST APIs

### JWT

JSON Web Token (JWT) is used for stateless authentication.

After successful login, the JWT token is used to authenticate subsequent API requests.

---

## 2. API Architecture

The backend follows a REST API architecture.

APIs are organized using HTTP methods such as:

* `GET`
* `POST`
* `PUT`
* `PATCH`
* `DELETE`

The backend uses DTOs to separate API request/response models from database entities.

---

## 3. Database

### MySQL

MySQL is used as the primary relational database for the BGV Portal.

The database stores information related to:

* System Users
* Roles
* Clients
* Client Locations
* Manage Users
* Components
* Sub Components
* Client Component mappings

Foreign keys and unique constraints are used to maintain data integrity.

---

## 4. Database Access

The project uses:

* Spring Data JPA
* Hibernate
* JPA Entity relationships
* Repository interfaces
* JPQL queries where required

The application uses transactions for business operations that involve multiple database changes.

For example, Client Component configuration performs:

1. Delete previously selected component mappings.
2. Save the newly selected components.

These operations are handled as a single transactional business operation.

---

## 5. Validation and Exception Handling

The backend uses Jakarta Validation for request validation.

Validation is used for checking required request fields and preventing invalid input.

Custom exceptions are used for application-level errors, including:

* Resource not found
* Duplicate resource
* Unauthorized operations
* GlobalExceptionHandler
* Bad Request Exception

A global exception handling mechanism is used to provide consistent API error responses.

---

## 6. Email Integration

Email functionality is implemented in the backend for system-generated notifications.

The current implementation includes email notifications for relevant user creation workflows.

For Manage User creation:

1. A temporary password is generated.
2. The password is securely stored after encoding.
3. Login information is sent to the user's email address.

---

## 7. Password Security

Passwords are not stored as plain text.

The backend uses a password encoder to encode passwords before storing them in the database.

Temporary passwords generated during user creation are also encoded before persistence.

---

## 8. Development and Testing Tools

### IDE

The backend is developed using a Java-compatible development environment such as Spring Tool Suite / VS Code.

### Postman

Postman is used for REST API testing.

API testing includes:

* Request validation
* Authentication testing
* Authorization testing
* CRUD operations
* Error handling
* Role-based behavior
* Client-specific behavior

### Git

Git is used for source code version control.

### GitHub

GitHub is used as the remote repository for the project.

Development changes are committed and pushed to the repository regularly.

---

## 9. Frontend

The BGV Portal frontend is planned/developed using React.

The frontend communicates with the Spring Boot backend through REST APIs.

The frontend will provide interfaces for:

* Login
* Client management
* User management
* Component management
* Sub-component management
* Client component selection
* BGV workflows

The detailed frontend technology and implementation will be maintained in:

`09-Frontend-Documentation.md`

---

## 10. Architecture Summary

The current technology stack follows this general structure:

```text
Frontend
   │
   ▼
React
   │
   │ REST API
   ▼
Spring Boot
   │
   ├── Spring Security
   ├── JWT
   ├── Service Layer
   ├── DTO Layer
   ├── Repository Layer
   └── Hibernate / JPA
   │
   ▼
MySQL
```

Supporting tools:

```text
Git
GitHub
Postman
Spring Tool Suite / VS Code
```
