# Authentication and Security

## 1. Authentication Overview

The BGV Portal uses **Spring Security** for authentication and authorization.

Authentication is based on user credentials and **JWT (JSON Web Token)**.

The authentication system is responsible for:

* Validating user login credentials.
* Generating JWT tokens after successful login.
* Validating JWT tokens on protected API requests.
* Identifying the logged-in System User.
* Identifying the user's role.
* Controlling access to protected APIs.

---

## 2. Authentication Flow

The general login flow is:

```text
User
 │
 ▼
Login API
 │
 ▼
Validate Email & Password
 │
 ▼
Authentication Successful
 │
 ▼
Generate JWT Token
 │
 ▼
Return Token
 │
 ▼
Frontend Stores Token
 │
 ▼
Token Sent With Subsequent Requests
 │
 ▼
JWT Filter
 │
 ▼
Validate Token
 │
 ▼
Set Authentication in Security Context
 │
 ▼
Controller / Service
```

---

## 3. Login

The user provides login credentials through the login API.

The backend validates:

* User email.
* User password.
* User account status.
* Authentication credentials.

Passwords are stored in encoded form and are not stored as plain text.

After successful authentication, the backend generates a JWT token.

---

## 4. JWT Authentication

JWT is used for stateless authentication.

The generated token contains information that allows the application to identify the authenticated user.

The frontend sends the token with subsequent API requests using the HTTP Authorization header.

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 5. JWT Request Flow

For a protected API request:

```text
Frontend
   │
   │ Authorization: Bearer JWT
   ▼
Spring Security
   │
   ▼
JWT Authentication Filter
   │
   ├── Extract Token
   │
   ├── Extract Username / Email
   │
   ├── Validate Token
   │
   └── Set Authentication
   │
   ▼
SecurityContext
   │
   ▼
Controller
   │
   ▼
Service
```

The application uses the authenticated user from the Security Context for subsequent business operations.

---

## 6. Logged-in User Identification

The application provides a `LoggedInUserService` to retrieve the currently authenticated System User.

The service uses the authenticated user's identity to retrieve the corresponding `SystemUser` from the database.

This allows application code to perform operations such as:

```text
Get logged-in user
        │
        ▼
Get role
        │
        ▼
Get associated client
        │
        ▼
Perform authorized operation
```

This is particularly important for client-level operations.

---

## 7. Role-Based Authorization

The application uses roles to control access to protected operations.

Current major roles include:

```text
SUPER_ADMIN
CLIENT_ADMIN
HR_USER
```

### SUPER_ADMIN

The Super Admin has system-level privileges.

The Super Admin can perform operations such as:

* Managing clients.
* Creating and managing components.
* Creating and managing sub-components.
* Assigning components to clients.
* Performing administrative configuration.

### CLIENT_ADMIN

The Client Admin operates within the scope of the associated client.

For client-level operations, the application can automatically identify the client from the logged-in user.

### HR_USER

HR_USER is a client-level role created through the Manage User workflow.

Access is limited according to the permissions configured for the role.

---

## 8. Role-Based Client Identification

The application handles client identification differently depending on the authenticated user's role.

### SUPER_ADMIN

The Super Admin can explicitly provide the client ID when performing operations for a particular client.

```text
SUPER_ADMIN
     │
     ▼
Client ID from Request
     │
     ▼
Find Client
     │
     ▼
Perform Operation
```

### CLIENT_ADMIN

The Client Admin does not need to provide the client ID for operations belonging to their own client.

```text
CLIENT_ADMIN
     │
     ▼
Logged-in System User
     │
     ▼
Find Associated Client
     │
     ▼
Perform Operation
```

This prevents the client user from simply changing a client ID in the request to operate on another client.

---

## 9. API Authorization

Protected controller methods can use role-based authorization.

For example:

```java
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
```

or:

```java
@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','CLIENT_ADMIN')")
```

This provides authorization at the API level.

Business-level authorization is also handled inside service methods where required.

---

## 10. Client Component Security

The Client Component module follows role-based behavior.

### SUPER_ADMIN

The Super Admin can configure components for a selected client.

Example:

```json
{
    "clientId": 5,
    "componentIds": [1, 2]
}
```

### CLIENT_ADMIN

The Client Admin works with the components belonging to the authenticated client's account.

The client is identified from the logged-in user instead of trusting a client ID supplied by the user.

Example:

```json
{
    "componentIds": [1, 2]
}
```

This provides an additional level of client data isolation.

---

## 11. Password Security

User passwords are encoded before being stored in the database.

The application uses a password encoder for password protection.

The general password flow is:

```text
User Password
     │
     ▼
Password Encoder
     │
     ▼
Encoded Password
     │
     ▼
Database
```

During login, the supplied password is compared against the stored encoded password through Spring Security's authentication mechanism.

---

## 12. Temporary Passwords

When a Manage User is created, the application generates a temporary password.

The flow is:

```text
Create Manage User
       │
       ▼
Generate Temporary Password
       │
       ├──────────────► Encode Password
       │                      │
       │                      ▼
       │                Store in Database
       │
       └──────────────► Send Credentials by Email
```

The plain temporary password is not stored in the database.

---

## 13. Duplicate User Validation

The application performs validation before creating users.

### Email

The System User email is checked before creation.

```text
Email exists?
     │
 ┌───┴───┐
Yes      No
 │        │
 ▼        ▼
Error    Continue
```

### Mobile

The Manage User mobile number is also checked for duplicates.

This prevents duplicate user records from being created.

---

## 14. Authorization and Data Isolation

Client-level data should be accessed according to the authenticated user's permissions.

The application uses the following approach:

```text
Authenticated User
       │
       ▼
Determine Role
       │
       ▼
Determine Client Scope
       │
       ▼
Validate Requested Operation
       │
       ▼
Access Allowed Data
```

For a `CLIENT_ADMIN`, the associated client is obtained from the authenticated user's relationship rather than relying solely on a client ID provided by the frontend.

---

## 15. Security Context

After successful JWT validation, the authenticated user is placed into the Spring Security Security Context.

Application services can then obtain the authenticated identity.

The current implementation uses the authenticated username/email to retrieve the corresponding `SystemUser`.

This allows business logic to use the actual logged-in user when setting audit fields such as:

* `createdBy`
* `updatedBy`

---

## 16. Audit Information

Where supported by the database model, user-related operations maintain audit information.

For example:

```text
created_by
updated_by
created_at
updated_at
```

The logged-in System User can be used as the creator or updater for administrative operations.

This provides traceability for changes made through the application.

---

## 17. Security Responsibilities by Layer

### Spring Security

Responsible for:

* Authentication.
* JWT validation.
* Security Context.
* API authorization.

### Controller

Responsible for:

* API-level authorization annotations.
* Receiving authenticated requests.
* Passing requests to the service layer.

### Service

Responsible for:

* Business authorization.
* Role-specific rules.
* Client identification.
* Data access restrictions.
* Business validation.

### Repository

Responsible for:

* Database access.
* Entity retrieval.
* Persistence operations.

---

## 18. Security Architecture

```text
                    ┌─────────────────┐
                    │     Frontend    │
                    └────────┬────────┘
                             │
                             │ JWT
                             ▼
                    ┌─────────────────┐
                    │ Spring Security │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   JWT Filter    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ SecurityContext │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   Controller    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │     Service     │
                    │                 │
                    │ Role Validation │
                    │ Client Scope    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   Repository    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │     MySQL       │
                    └─────────────────┘
```

---

## 19. Current Security Principles

The current security implementation follows these principles:

* JWT-based stateless authentication.
* Passwords are stored in encoded form.
* Protected APIs require authentication.
* Role-based authorization is applied to protected operations.
* Client Admin operations use the authenticated user's client association.
* Super Admin can operate across clients where authorized.
* Duplicate user validation is performed before user creation.
* Audit fields can identify the user responsible for changes.
* Business-level authorization is enforced in the service layer where required.
* Database relationships and foreign keys help maintain data integrity.
