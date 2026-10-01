# Database Design

## 1. Database Overview

The BGV Portal uses **MySQL** as the primary relational database.

The database is designed around the core entities required for:

* User authentication
* Role management
* Client management
* Client locations
* Manage Users
* BGV components
* Sub-components
* Client-specific component selection

Relationships are maintained using primary keys, foreign keys, and unique constraints.

---

## 2. Core Database Structure

The current database structure can be represented as:

```text
roles
  │
  ▼
system_users
  │
  ├──────────────► client_information
  │
  └──────────────► manage_users

client_information
  │
  ├──────────────► client_locations
  │
  └──────────────► client_components
                         │
                         ▼
                     components
                         │
                         ▼
                   sub_components
```

---

## 3. `roles`

The `roles` table stores the roles available in the system.

### Important Columns

| Column        | Description                          |
| ------------- | ------------------------------------ |
| `id`          | Primary key                          |
| `role_name`   | Name of the role                     |
| `description` | Role description                     |
| `is_active`   | Indicates whether the role is active |
| `created_by`  | User who created the record          |
| `updated_by`  | User who last updated the record     |
| `created_at`  | Creation timestamp                   |
| `updated_at`  | Last update timestamp                |

### Current Roles

```text
SUPER_ADMIN
CLIENT_ADMIN
HR_USER
```

---

## 4. `system_users`

The `system_users` table stores the authentication-level user information.

It is used by Spring Security for user authentication and contains login and user account information.

### Important Columns

| Column          | Description                 |
| --------------- | --------------------------- |
| `id`            | Primary key                 |
| `first_name`    | User first name             |
| `last_name`     | User last name              |
| `email`         | Login email                 |
| `mobile_number` | User mobile number          |
| `password`      | Encoded password            |
| `role_id`       | Foreign key to `roles`      |
| `is_active`     | User account status         |
| `last_login`    | Last login information      |
| `created_by`    | User who created the record |
| `updated_by`    | User who updated the record |
| `created_at`    | Creation timestamp          |
| `updated_at`    | Last update timestamp       |

### Relationship

```text
roles
   │
   └──────< system_users
```

A System User is associated with a role.

---

## 5. `client_information`

The `client_information` table stores information about client organizations.

### Important Columns

| Column           | Description                 |
| ---------------- | --------------------------- |
| `id`             | Primary key                 |
| `client_name`    | Client organization name    |
| `client_code`    | Client code                 |
| `abbreviation`   | Client abbreviation         |
| `contact_person` | Client contact person       |
| `official_email` | Official client email       |
| `mobile`         | Client mobile number        |
| `landline`       | Client landline             |
| `address`        | Client address              |
| `city`           | Client city                 |
| `state`          | Client state                |
| `country`        | Client country              |
| `pincode`        | Client pincode              |
| `remarks`        | Additional remarks          |
| `status`         | Client status               |
| `user_id`        | Associated System User      |
| `created_at`     | Creation timestamp          |
| `updated_at`     | Last update timestamp       |
| `created_by`     | User who created the record |
| `updated_by`     | User who updated the record |

### Relationship

```text
system_users
     │
     └────── client_information
```

The client can be associated with a System User.

This relationship is also used to identify the client of a logged-in `CLIENT_ADMIN`.

---

## 6. `client_locations`

The `client_locations` table stores locations belonging to clients.

### Relationship

```text
client_information
        │
        └──────< client_locations
```

A client can have multiple locations.

Client locations are used when creating and managing users associated with a client.

---

## 7. `manage_users`

The Manage User table stores client-level user information.

A Manage User is connected to:

* A System User
* A Client
* A Client Location

### Relationship

```text
system_users
     │
     └────── manage_users
                 │
                 ├────── client_information
                 │
                 └────── client_locations
```

The System User contains authentication information, while the Manage User contains client-specific information.

---

## 8. `components`

The `components` table is a master table containing the main BGV verification components.

The current table structure is:

```sql
CREATE TABLE components (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    component_name VARCHAR(150) NOT NULL UNIQUE,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);
```

### Columns

| Column           | Description                               |
| ---------------- | ----------------------------------------- |
| `id`             | Primary key                               |
| `component_name`  | Unique component name                     |
| `is_active`       | Indicates whether the component is active |
| `created_at`      | Creation timestamp                        |
| `updated_at`      | Last update timestamp                     |

### Important Rule

Component names must be unique.

Components are master data controlled by the `SUPER_ADMIN`.

Clients do not create or delete master components.

---

## 9. `sub_components`

The `sub_components` table stores sub-components belonging to a Component.

The table uses a composite unique constraint:

```text
component_id + sub_component_name
```

This prevents the same sub-component name from being duplicated under the same component.

### Structure

```text
sub_components
    │
    ├── id
    ├── component_id
    ├── sub_component_name
    ├── is_active
    ├── created_at
    └── updated_at
```

### Relationship

```text
components
     │
     └──────< sub_components
```

A Component can contain multiple Sub Components.

Example:

```text
Education Verification
        │
        ├── 10th
        ├── 12th
        └── Graduation
```

### Important Rule

A Sub Component belongs to one Component.

The same sub-component name can exist under different components, but cannot be duplicated within the same component.

---

## 10. `client_components`

The `client_components` table is the mapping table between clients and components.

It determines which BGV components are assigned to a particular client.

The current table structure is:

```sql
CREATE TABLE client_components (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    client_id BIGINT NOT NULL,

    component_id BIGINT NOT NULL,

    is_selected BOOLEAN NOT NULL DEFAULT TRUE,

    created_by BIGINT,

    updated_by BIGINT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_client_component_client
        FOREIGN KEY (client_id)
        REFERENCES client_information(id),

    CONSTRAINT fk_client_component_component
        FOREIGN KEY (component_id)
        REFERENCES components(id),

    CONSTRAINT fk_client_component_created_by
        FOREIGN KEY (created_by)
        REFERENCES system_users(id),

    CONSTRAINT fk_client_component_updated_by
        FOREIGN KEY (updated_by)
        REFERENCES system_users(id),

    CONSTRAINT uk_client_component
        UNIQUE (client_id, component_id)
);
```

### Columns

| Column         | Description                                 |
| -------------- | ------------------------------------------- |
| `id`           | Primary key                                 |
| `client_id`    | Foreign key to `client_information`         |
| `component_id` | Foreign key to `components`                 |
| `is_selected`  | Indicates whether the component is selected |
| `created_by`   | System User who created the mapping         |
| `updated_by`   | System User who updated the mapping         |
| `created_at`   | Creation timestamp                          |
| `updated_at`   | Last update timestamp                       |

---

## 11. Client-Component Relationship

The relationship between clients and components is many-to-many through the `client_components` mapping table.

```text
Client A
   │
   ├──── Education Verification
   ├──── Driving Licence
   └──── Employment Verification


Client B
   │
   ├──── Education Verification
   └──── Address Verification
```

The database represents this as:

```text
client_information
        │
        │ 1
        │
        │ many
        ▼
client_components
        ▲
        │ many
        │
        │ 1
        │
    components
```

---

## 12. Unique Constraints

The database uses unique constraints to prevent duplicate records where required.

### Components

```text
component_name
```

must be unique.

### Sub Components

```text
component_id + sub_component_name
```

must be unique.

### Client Components

```text
client_id + component_id
```

must be unique.

This means a client cannot have the same component assigned more than once.

---

## 13. Foreign Key Relationships

The main foreign key relationships are:

```text
system_users.role_id
        │
        ▼
roles.id
```

```text
client_information.user_id
        │
        ▼
system_users.id
```

```text
client_locations.client_id
        │
        ▼
client_information.id
```

```text
manage_users.system_user_id
        │
        ▼
system_users.id
```

```text
manage_users.client_id
        │
        ▼
client_information.id
```

```text
manage_users.client_location_id
        │
        ▼
client_locations.id
```

```text
sub_components.component_id
        │
        ▼
components.id
```

```text
client_components.client_id
        │
        ▼
client_information.id
```

```text
client_components.component_id
        │
        ▼
components.id
```

```text
client_components.created_by
        │
        ▼
system_users.id
```

```text
client_components.updated_by
        │
        ▼
system_users.id
```

---

## 14. Client Component Selection Flow

When a client component selection is saved, the application follows this database flow:

```text
Request
   │
   ▼
Identify Logged-in User
   │
   ▼
Identify Role
   │
   ├── SUPER_ADMIN
   │       │
   │       └── Client ID from request
   │
   └── CLIENT_ADMIN
           │
           └── Client from logged-in user
   │
   ▼
Find Client
   │
   ▼
Delete Existing Client Components
   │
   ▼
Validate Components
   │
   ▼
Insert New Client Component Mappings
```

The operation is handled transactionally so that the removal of previous mappings and insertion of new mappings are treated as one business operation.

---

## 15. Database Integrity

The database design uses:

* Primary keys for unique record identification.
* Foreign keys for maintaining relationships.
* Unique constraints for preventing duplicates.
* Boolean status fields for active/inactive records.
* Timestamp fields for creation and update tracking.
* Transaction management for multi-step database operations.

---

## 16. High-Level ER Relationship

```text
                         ┌─────────────┐
                         │    roles    │
                         └──────┬──────┘
                                │
                                │
                         ┌──────▼──────┐
                         │ system_users│
                         └───┬──────┬──┘
                             │      │
                 ┌───────────┘      └───────────┐
                 ▼                              ▼
       ┌──────────────────┐             ┌──────────────┐
       │client_information│             │ manage_users │
       └───────┬──────────┘             └──────────────┘
               │
               ├───────────────┐
               ▼               ▼
      ┌────────────────┐   ┌──────────────────┐
      │client_locations│   │client_components │
      └────────────────┘   └────────┬─────────┘
                                    │
                                    ▼
                              ┌────────────┐
                              │ components │
                              └─────┬──────┘
                                    │
                                    ▼
                             ┌───────────────┐
                             │sub_components │
                             └───────────────┘
```

---

## 17. Database Design Principles

The current database design follows these principles:

* Keep authentication data separate from client-specific user information.
* Use master tables for reusable BGV components.
* Use sub-components to provide component-level detail.
* Use mapping tables for client-specific component assignments.
* Maintain referential integrity through foreign keys.
* Prevent duplicate mappings through unique constraints.
* Maintain active/inactive status instead of unnecessarily deleting master records.
* Maintain audit information where required.
* Keep database relationships aligned with the application's service and business logic.
