# Module Documentation

## 1. Module Overview

The BGV Portal backend is developed as a modular Spring Boot application.

The current modules cover authentication, client management, user management, client locations, BGV components, sub-components, and client-specific component configuration.

The modules are designed so that each business area has its own:

* Entity
* Repository
* Request DTO
* Response DTO
* Service
* Service Implementation
* Controller

---

## 2. Authentication Module

### Purpose

The Authentication module handles user login and authentication.

### Responsibilities

* Authenticate users using email and password.
* Validate user credentials.
* Generate JWT tokens.
* Authenticate subsequent API requests.
* Identify the logged-in System User.
* Provide role information for authorization.

### Main Components

```text
Authentication Controller
        │
        ▼
Authentication Service
        │
        ▼
Spring Security
        │
        ▼
JWT
```

---

## 3. Client Management Module

### Purpose

The Client Management module manages organizations using the BGV Portal.

### Responsibilities

* Create client information.
* Retrieve client information.
* Update client information.
* Manage client status.
* Associate clients with System Users.
* Support client-level operations.

### Main Entity

```text
ClientInformation
```

### Relationship

```text
SystemUser
    │
    ▼
ClientInformation
```

---

## 4. Client Location Module

### Purpose

The Client Location module manages locations belonging to clients.

### Responsibilities

* Create client locations.
* Retrieve client locations.
* Update client locations.
* Activate/deactivate locations where applicable.
* Associate locations with clients.

### Relationship

```text
ClientInformation
        │
        └── ClientLocation
```

A client can have multiple locations.

Client locations are used during Manage User creation and client-level user management.

---

## 5. Manage User Module

### Purpose

The Manage User module manages users belonging to client organizations.

### User Creation Flow

```text
Logged-in User
      │
      ▼
Check Role
      │
      ├── SUPER_ADMIN
      │       │
      │       └── Client from Request
      │
      └── CLIENT_ADMIN
              │
              └── Client from Logged-in User
      │
      ▼
Validate Client
      │
      ▼
Validate Client Location
      │
      ▼
Check Duplicate Email
      │
      ▼
Check Duplicate Mobile
      │
      ▼
Assign HR_USER Role
      │
      ▼
Generate Temporary Password
      │
      ▼
Create System User
      │
      ▼
Create Manage User
      │
      ▼
Send Email
```

### Main Relationships

```text
SystemUser
    │
    ▼
ManageUser
    │
    ├── ClientInformation
    │
    └── ClientLocation
```

### Important Business Rules

* Email duplication is checked before user creation.
* Mobile duplication is checked before user creation.
* A temporary password is generated for the new user.
* The password is encoded before storage.
* Login information is sent through email.
* The user is associated with the appropriate client.
* The user is assigned the appropriate client-level role.

---

## 6. Component Module

### Purpose

The Component module manages the master BGV verification components.

Components are controlled by the Super Admin.

### Examples

```text
Education Verification
Driving Licence
Employment Verification
Address Verification
```

### Responsibilities

* Create components.
* Retrieve components.
* Retrieve a component by ID.
* Update components.
* Activate components.
* Deactivate components.
* Delete components.

### Main Entity

```text
Component
```

### Database Table

```text
components
```

### Important Business Rules

* Component name must be unique.
* Components are master data.
* Component creation and management belong to the Super Admin.
* Clients do not create or delete master components.

---

## 7. Sub Component Module

### Purpose

The Sub Component module provides detailed options under a Component.

### Example

```text
Education Verification
    │
    ├── 10th
    ├── 12th
    └── Graduation
```

### Responsibilities

* Create sub-components.
* Retrieve all sub-components.
* Retrieve a sub-component by ID.
* Retrieve sub-components by Component ID.
* Update sub-components.
* Activate sub-components.
* Deactivate sub-components.
* Delete sub-components.

### Main Entity

```text
SubComponent
```

### Database Table

```text
sub_components
```

### Relationship

```text
Component
    │
    └──< SubComponent
```

### Important Business Rule

A sub-component name must be unique within its parent component.

---

## 8. Client Component Module

### Purpose

The Client Component module manages which BGV components are assigned to a client.

This module connects the Client and Component master data.

### Database Table

```text
client_components
```

### Relationship

```text
ClientInformation
        │
        ▼
ClientComponent
        ▲
        │
        ▼
Component
```

A client can have multiple components, and the same component can be assigned to multiple clients.

---

## 9. Client Component Selection

The module supports two main flows.

### SUPER_ADMIN Flow

The Super Admin can select components for a particular client.

```text
SUPER_ADMIN
     │
     ▼
Select Client
     │
     ▼
Select Components
     │
     ▼
Save Client Components
```

Example request:

```json
{
    "clientId": 5,
    "componentIds": [1, 2]
}
```

---

### CLIENT_ADMIN Flow

The Client Admin does not need to provide the client ID.

The application identifies the client using the logged-in System User.

```text
CLIENT_ADMIN
     │
     ▼
Logged-in System User
     │
     ▼
Associated Client
     │
     ▼
Select Components
     │
     ▼
Save Components
```

Example request:

```json
{
    "componentIds": [1, 2]
}
```

---

## 10. Client Component Replacement Logic

When saving a new component selection for a client, the application removes the client's existing component mappings before saving the new selection.

```text
Existing Selection
        │
        ▼
Delete Existing Mappings
        │
        ▼
Validate New Components
        │
        ▼
Insert New Mappings
        │
        ▼
Return Selected Components
```

This operation is handled transactionally.

The combination of `client_id` and `component_id` is unique in the database.

---

## 11. Client Component APIs

The Client Component module currently provides operations for:

### Save Components

```text
POST /api/client-components
```

### Get Components By Client

```text
GET /api/client-components/client/{clientId}
```

### Get Clients By Component

```text
GET /api/client-components/component/{componentId}
```

### Remove Client Components

```text
DELETE /api/client-components/client/{clientId}
```

Detailed API request and response structures are maintained separately in `06-API-Documentation.md`.

---

## 12. Module Relationship

The main modules are connected as follows:

```text
Authentication
      │
      ▼
System User
      │
      ├────────────── Client
      │                  │
      │                  ├── Client Location
      │                  │
      │                  └── Manage User
      │
      └── Role

Client
  │
  ▼
Client Component
  │
  ▼
Component
  │
  ▼
Sub Component
```

---

## 13. Module Development Pattern

Each backend module follows a layered development pattern:

```text
Database Table
      │
      ▼
Entity
      │
      ▼
Request / Response DTO
      │
      ▼
Repository
      │
      ▼
Service Interface
      │
      ▼
Service Implementation
      │
      ▼
Controller
      │
      ▼
API Testing
```

This pattern is followed to keep the application modular and maintainable.

---

## 14. Current Module Status

The following modules have been implemented as part of the current backend development:

* Authentication
* Client Management
* Client Location Management
* Manage User
* Component
* Sub Component
* Client Component

The Component APIs have been tested successfully.

The Sub Component APIs have been developed and tested.

The Client Component APIs have been implemented and are being tested.

Additional BGV verification modules will be added as the project progresses.
