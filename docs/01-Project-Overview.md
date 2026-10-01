# Project Overview

## 1. Project Name

**BGV Portal**

BGV Portal is a web-based Background Verification system designed to manage clients, users, verification components, sub-components, and client-specific verification configurations.

---

## 2. Project Purpose

The purpose of the BGV Portal is to provide a centralized system for managing the Background Verification process for multiple clients.

The system allows the Super Admin to manage clients, users, verification components, and sub-components, while client users can access and work with the verification components assigned to their organization.

The application is designed with role-based access so that different users have access only to the operations permitted for their role.

---

## 3. Project Objective

The main objectives of the BGV Portal are:

* Manage multiple clients from a centralized platform.
* Manage users associated with clients.
* Provide secure login and authentication.
* Implement role-based authorization.
* Allow Super Admin to configure verification components for clients.
* Allow creation and management of verification components and sub-components.
* Automatically identify the logged-in client for client-level operations.
* Maintain relationships between clients, users, components, and sub-components.
* Provide a scalable backend structure that can support additional BGV modules in the future.

---

## 4. Project Scope

The current project scope includes:

* Authentication and authorization.
* Client management.
* Client location management.
* System user management.
* Manage User management.
* Component management.
* Sub Component management.
* Client Component management.
* Role-based access control.
* Email notification for relevant user creation workflows.
* REST API-based backend architecture.
* Database-driven configuration of BGV components.

The project is being developed incrementally, with additional BGV verification modules planned for future development.

---

## 5. User Roles

The application currently works with role-based access.

### 5.1 SUPER_ADMIN

The Super Admin has system-level access and is responsible for managing the overall BGV platform.

Responsibilities include:

* Managing clients.
* Managing client-related configuration.
* Creating and managing system users where permitted.
* Creating and managing BGV components.
* Creating and managing sub-components.
* Assigning components to clients.
* Configuring client-specific verification requirements.
* Managing system-level data.

The Super Admin can select components for a client during client onboarding or configuration.

---

### 5.2 CLIENT_ADMIN

The Client Admin represents a client organization within the BGV Portal.

Responsibilities include:

* Logging into the system.
* Accessing information associated with their own client.
* Working with components assigned to their client.
* Performing client-level operations according to their permissions.
* Creating/managing permitted users under their client.

For client-level operations, the system can identify the client automatically from the logged-in user's account instead of requiring the client ID to be manually provided.

---

### 5.3 HR_USER

HR_USER is a client-level user created under a client organization.

The user is associated with:

* A System User account.
* A Client.
* A Client Location.

A temporary password can be generated when the user is created, and the user's login credentials are sent through email.

---

## 6. Main Modules

### 6.1 Authentication and Authorization

The system provides secure user authentication using login credentials and JWT-based authentication.

After successful login, the authenticated user is available through the Spring Security context.

The logged-in user is used to determine:

* User identity.
* User role.
* Associated client.
* Authorization for client-level operations.

---

### 6.2 Client Management

The Client Management module manages client organizations registered in the BGV Portal.

Client information is associated with a System User where applicable.

Client-related information includes details such as:

* Client name.
* Client code.
* Abbreviation.
* Contact information.
* Address.
* Location information.
* Status.
* Official email.

---

### 6.3 Client Location Management

Clients can have locations associated with them.

Locations are linked to the client and are used when managing users and client-specific operations.

A Manage User can be associated with both:

* Client
* Client Location

---

### 6.4 Manage User

The Manage User module handles users created for client organizations.

The creation flow includes:

1. Identify the logged-in user.
2. Determine the user's role.
3. Determine the client based on the role.
4. Validate the client.
5. Validate the client location.
6. Check duplicate email.
7. Check duplicate mobile number.
8. Assign the `HR_USER` role.
9. Generate a temporary password.
10. Create the System User.
11. Create the Manage User.
12. Send the login credentials through email.

For a `SUPER_ADMIN`, the client can be supplied explicitly.

For a `CLIENT_ADMIN`, the client is automatically identified from the logged-in user.

---

### 6.5 Component Management

Components represent the main verification categories available in the BGV system.

Examples can include verification categories such as:

* Education Verification
* Driving Licence
* Employment Verification
* Address Verification

Components are master data controlled by the Super Admin.

The Component module supports creation, retrieval, updating, activation, deactivation, and deletion of components.

---

### 6.6 Sub Component Management

Sub Components provide more detailed options under a Component.

For example:

```text
Education Verification
    ├── 10th
    ├── 12th
    └── Graduation
```

A Sub Component belongs to one Component.

The Super Admin has the authority to create, update, activate, deactivate, and delete sub-components.

Sub-components have a unique relationship with their parent component so that duplicate sub-component names cannot be created under the same component.

---

### 6.7 Client Component Management

The Client Component module controls which verification components are assigned to each client.

The relationship is:

```text
Client
   │
   ├── Component 1
   ├── Component 2
   └── Component 3
```

The Super Admin can select components for a specific client.

The Client Admin can work with components belonging to their own client.

The system does not require a Client Admin to manually provide the client ID for client-level operations. The client is identified from the authenticated user.

The current implementation supports replacing a client's previously selected components with a new selection.

---

## 7. High-Level System Workflow

The current high-level workflow is:

```text
User Login
     │
     ▼
JWT Authentication
     │
     ▼
Identify Logged-in System User
     │
     ▼
Identify User Role
     │
     ├───────────────┐
     │               │
 SUPER_ADMIN    CLIENT_ADMIN
     │               │
     ▼               ▼
Select Client    Automatically
     │            Identify Client
     └───────┬───────┘
             │
             ▼
      Client Operations
             │
             ▼
    Assigned Components
             │
             ▼
       Sub Components
```

---

## 8. Component Configuration Workflow

The component configuration follows this structure:

```text
SUPER_ADMIN
     │
     ▼
Create Component
     │
     ▼
Create Sub Components
     │
     ▼
Select Components for Client
     │
     ▼
Client Component Mapping
     │
     ▼
CLIENT_ADMIN
     │
     ▼
Access Assigned Components
```

The Super Admin controls the creation and configuration of the master components and sub-components.

Clients do not create or delete master components.

---

## 9. Business Rules

The major business rules currently implemented include:

* Only authorized roles can access protected operations.
* Components are master data controlled by the Super Admin.
* Sub-components belong to a parent component.
* A component name must be unique.
* A sub-component name must be unique within its parent component.
* A client can have multiple components.
* A component can be assigned to multiple clients.
* The combination of client and component is unique in the `client_components` table.
* A Client Admin should work with the client associated with the logged-in user.
* Super Admin can explicitly select the client when configuring client components.
* Manage Users are assigned the `HR_USER` role.
* Duplicate email addresses are not allowed for System Users.
* Duplicate mobile numbers are not allowed for Manage Users.
* A temporary password is generated for newly created Manage Users.
* Login credentials are sent through email.
* Client Component selection is treated as a single business operation so that deleting previous mappings and saving new mappings can be handled transactionally.

---

## 10. Database Relationship Overview

The current core relationship can be represented as:

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

The `client_components` table acts as the mapping between clients and components.

A client can therefore have multiple components, and the same component can be used by multiple clients.

---

## 11. Backend Architecture

The BGV backend follows a layered Spring Boot architecture.

The major layers include:

```text
Controller
    │
    ▼
Service
    │
    ▼
Service Implementation
    │
    ▼
Repository
    │
    ▼
Database
```

DTOs are used for request and response data.

Entities represent database tables and relationships.

Security components handle authentication and authorization.

Exception handling is implemented using application-specific exceptions and global exception handling.

---

## 12. Current Development Status

The following major backend areas have been developed:

* Authentication and JWT security.
* Client Management.
* Client Location Management.
* Manage User.
* Component Management.
* Sub Component Management.
* Client Component Management.
* Logged-in user identification.
* Role-based client identification.
* Email integration for relevant user creation workflows.

The Component APIs have been tested successfully.

The Sub Component APIs have been developed and are being tested.

The Client Component APIs have been developed and are currently being tested.

The latest backend changes have been pushed to the GitHub repository.

---

## 13. Current Development Approach

The project is being developed module by module.

For each module, the development process follows:

```text
Database Design
      ↓
Entity
      ↓
Repository
      ↓
Request / Response DTO
      ↓
Service Interface
      ↓
Service Implementation
      ↓
Controller
      ↓
API Testing
      ↓
Documentation
      ↓
GitHub
```

This approach helps ensure that each module is completed and tested before moving to the next module.

---

## 14. Future Scope

The BGV Portal is intended to be expanded with additional Background Verification functionality.

Future development can include:

* Additional verification components.
* Detailed verification workflows.
* Candidate management.
* Case management.
* Verification status tracking.
* Document management.
* Verification reports.
* Client dashboards.
* Administrative dashboards.
* Frontend implementation.
* AWS deployment.
* Production environment configuration.
* Additional reporting and monitoring functionality.

The system architecture is being maintained so that new modules can be added without disrupting the existing client, user, component, and authentication structure.
