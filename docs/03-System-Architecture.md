# System Architecture

## 1. Architecture Overview

The BGV Portal follows a layered client-server architecture.

The frontend communicates with the Spring Boot backend through REST APIs. The backend handles authentication, authorization, business logic, validation, database operations, and email-related functionality.

The backend is organized into separate layers so that responsibilities remain clearly separated.

```text
┌──────────────────────────────┐
│          Frontend            │
│            React             │
└──────────────┬───────────────┘
               │
               │ REST API / JSON
               ▼
┌──────────────────────────────┐
│       Spring Boot API        │
│                              │
│   Controller Layer           │
│          │                   │
│          ▼                   │
│   Service Layer              │
│          │                   │
│          ▼                   │
│   Repository Layer           │
│          │                   │
│          ▼                   │
│   JPA / Hibernate            │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│           MySQL              │
│          Database            │
└──────────────────────────────┘
```

---

## 2. Frontend Layer

The frontend is developed using React.

The frontend is responsible for:

* Providing the user interface.
* Login and authentication interaction.
* Displaying client and user information.
* Displaying BGV components and sub-components.
* Allowing authorized users to perform permitted operations.
* Sending requests to the backend REST APIs.
* Handling API responses and displaying appropriate information to users.

The frontend does not directly access the database.

All database-related operations are performed through the backend APIs.

---

## 3. API Layer

The Spring Boot application exposes REST APIs for communication between the frontend and backend.

The Controller layer is responsible for:

* Receiving HTTP requests.
* Reading request parameters.
* Reading request bodies.
* Validating incoming requests.
* Calling the appropriate service method.
* Returning API responses.

The controller does not contain complex business logic.

The business logic is handled by the Service layer.

---

## 4. Security Layer

Spring Security is used to protect the backend APIs.

JWT is used for authentication.

The general authentication flow is:

```text
User
 │
 ▼
Login API
 │
 ▼
Validate Credentials
 │
 ▼
Generate JWT
 │
 ▼
Return JWT to Client
 │
 ▼
Frontend Stores Token
 │
 ▼
JWT Sent With API Requests
 │
 ▼
Spring Security Validates Token
 │
 ▼
Authenticated User
```

The authenticated user is available to the application through the Spring Security context.

The system can identify the logged-in user and use the user's role and client association for authorization and business operations.

---

## 5. Role-Based Architecture

The system uses role-based authorization.

The major roles currently implemented are:

```text
SUPER_ADMIN -                Complete system access
ADMIN-                       Manage internal operations
DATA_ENTRY -                 Enter and manage BGV case data
VERIFICATION_EXECUTIVE       Perform verification checks
QUALITY_CHECK                Review and approve verification reports
CLIENT_ADMIN                 Manage client organization
HR_USER                      Submit and track employee verifications
```

The role determines which operations a user can perform.

For example:

```text
SUPER_ADMIN
    │
    ├── Manage Clients
    ├── Manage Components
    ├── Manage Sub Components
    └── Configure Components for Clients

CLIENT_ADMIN
    │
    ├── Access Own Client
    ├── Work With Assigned Components
    └── Manage Permitted Client Users

HR_USER
    │
    └── Client-level permitted operations
```

The exact permissions are controlled through Spring Security and application-level business logic.

---

## 6. Controller Layer

Controllers provide the HTTP endpoints of the application.

Current controller areas include:

* Authentication
* Client Information
* Client Location
* Manage User
* Component
* Sub Component
* Client Component

Example architecture:

```text
HTTP Request
     │
     ▼
ClientComponentController
     │
     ▼
ClientComponentService
     │
     ▼
ClientComponentServiceImpl
     │
     ▼
ClientComponentRepository
     │
     ▼
MySQL
```

---

## 7. Service Layer

The Service layer contains the application's business logic.

The Service layer is responsible for:

* Business validation.
* Role-based business rules.
* Client identification.
* Managing relationships between entities.
* Coordinating multiple repository operations.
* Handling transactional business operations.

For example, when a Client Component configuration is saved:

```text
Identify Logged-in User
        │
        ▼
Check User Role
        │
        ├── SUPER_ADMIN
        │      └── Use Client ID from request
        │
        └── CLIENT_ADMIN
               └── Identify Client from Logged-in User
        │
        ▼
Delete Existing Client Components
        │
        ▼
Validate Requested Components
        │
        ▼
Save New Client Components
        │
        ▼
Return Selected Components
```

---

## 8. Transaction Management

Operations involving multiple database changes are handled as a single transaction where required.

For example, Client Component selection performs:

```text
BEGIN TRANSACTION
       │
       ▼
Delete Existing Component Mappings
       │
       ▼
Save Component 1
       │
       ▼
Save Component 2
       │
       ▼
Save Component 3
       │
       ▼
COMMIT
```

If an operation fails:

```text
ROLLBACK
```

This prevents the database from being left in a partially updated state.

---

## 9. Repository Layer

The Repository layer is responsible for database access.

Spring Data JPA repositories are used for:

* CRUD operations.
* Entity retrieval.
* Existence checks.
* Custom queries.
* Delete and update operations where required.

The repository layer keeps database access separate from business logic.

---

## 10. Entity and Database Layer

JPA entities represent database tables.

Relationships between entities are defined using JPA annotations such as:

* `@OneToOne`
* `@OneToMany`
* `@ManyToOne`
* `@ManyToMany`

The current core relationship is:

```text
SystemUser
     │
     ├────────────── ClientInformation
     │
     └────────────── ManageUser

ClientInformation
     │
     ├────────────── ClientLocation
     │
     └────────────── ClientComponent
                         │
                         ▼
                     Component
                         │
                         ▼
                   SubComponent
```

Hibernate translates JPA operations into SQL queries executed against MySQL.

---

## 11. DTO Layer

DTOs are used to transfer data between the frontend and backend.

Request DTOs contain data received from the client.

Response DTOs contain data returned by the backend.

DTOs prevent the application from directly exposing database entities through API contracts and allow API request/response structures to evolve independently of the database entities.

---

## 12. Exception Handling

The application uses centralized exception handling.

Custom exceptions include cases such as:

* Resource not found.
* Duplicate resource.
* Unauthorized operation.

The Global Exception Handler processes these exceptions and returns consistent API error responses.

General flow:

```text
Exception
   │
   ▼
Service / Controller
   │
   ▼
Global Exception Handler
   │
   ▼
Standard API Error Response
```

---

## 13. Email Integration

Email functionality is integrated into backend workflows where user notifications are required.

For example, during Manage User creation:

```text
Create Manage User
       │
       ▼
Generate Temporary Password
       │
       ▼
Save Encoded Password
       │
       ▼
Generate Email Template
       │
       ▼
Send Email
```

Email failure is handled separately so that a user creation operation does not necessarily fail solely because email delivery fails.

---

## 14. Client Identification Architecture

Client-level operations support two different flows.

### SUPER_ADMIN

The Super Admin can operate on a selected client.

```text
SUPER_ADMIN
     │
     ▼
clientId from Request
     │
     ▼
Find Client
     │
     ▼
Perform Operation
```

### CLIENT_ADMIN

The Client Admin does not need to manually provide the client ID for operations that belong to their own client.

```text
CLIENT_ADMIN
     │
     ▼
Logged-in SystemUser
     │
     ▼
Find Associated Client
     │
     ▼
Perform Operation for That Client
```

This prevents a Client Admin from manually changing the client ID to access another client's data.

---

## 15. Client Component Architecture

The Client Component module implements a many-to-many style relationship between clients and components through a mapping table.

```text
Client A
   │
   ├── Education Verification
   ├── Driving Licence
   └── Employment Verification

Client B
   │
   ├── Education Verification
   └── Address Verification
```

The `client_components` table stores these mappings.

A unique constraint on the combination of:

```text
client_id + component_id
```

prevents the same component from being assigned to the same client more than once.

---

## 16. Component and Sub Component Architecture

Components are master records controlled by the Super Admin.

Sub-components belong to components.

```text
Component
    │
    ├── Sub Component
    ├── Sub Component
    └── Sub Component
```

Example:

```text
Education Verification
    │
    ├── 10th
    ├── 12th
    └── Graduation
```

Clients do not create or delete master components or sub-components.

The components made available to a client are controlled through Client Component mappings.

---

## 17. Overall Request Flow

A typical authenticated API request follows this architecture:

```text
React Frontend
      │
      │ HTTP Request + JWT
      ▼
Spring Security
      │
      │ Validate JWT
      ▼
Authentication Context
      │
      ▼
Controller
      │
      ▼
Service
      │
      ├── Business Validation
      ├── Role Validation
      └── Transaction Management
      │
      ▼
Repository
      │
      ▼
Hibernate / JPA
      │
      ▼
MySQL
      │
      ▼
Response
      │
      ▼
React Frontend
```

---

## 18. Architectural Principles

The project follows these main architectural principles:

* Separation of concerns.
* Layered backend architecture.
* REST API communication.
* DTO-based API contracts.
* Role-based authorization.
* Centralized exception handling.
* Database relationship integrity through foreign keys.
* Transaction management for multi-step database operations.
* Reusable service and repository components.
* Client-level data isolation through authenticated user context.
* Modular design to support future BGV verification modules.
