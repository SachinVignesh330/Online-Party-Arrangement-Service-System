# EventBridge — User Management Module (fixed)

Spring Boot 3 / Java 17 — authentication and role administration for
**Customer, Admin, Vendor, Coordinator**.

## Fixes applied vs original zip

| Issue | Fix |
|-------|-----|
| ID type `Long` vs DB `INTEGER` | Changed to `Integer` across entities, repos, DTOs |
| Port conflict with Booking module | Server port **8081** |
| Spring Security 6 `DaoAuthenticationProvider` | Constructor takes `UserDetailsService` |
| JWT filter crashes on bad token | Try/catch; clears security context |
| `password` NOT NULL on entity vs nullable vendor/coordinator columns | Password nullable at JPA level |
| Credentials hard-coded | Support `DB_USERNAME` / `DB_PASSWORD` env vars |

## Schema (Flyway)

On startup Flyway runs `V1__user_management_auth.sql`:

- `customer.status`
- `vendor.password`, `vendor.created_at`
- `coordinator.password`, `coordinator.created_at`
- new `admin` table

Requires the original `EventBridge_Database.sql` already loaded into database `eventbridge`.

## Run

1. PostgreSQL running, database `eventbridge` loaded.
2. Set password in `application.yml` (or env `DB_PASSWORD`) if not `postgres`.
3. From this folder:

```bash
mvn spring-boot:run
```

Or open in IntelliJ and run `UserManagementApplication`.

App URL: **http://localhost:8081**

## API

| Method | Path | Access |
|--------|------|--------|
| POST | `/api/auth/register` | public |
| POST | `/api/auth/login` | public |
| GET | `/api/admin/users/{role}` | ADMIN JWT |
| PATCH | `/api/admin/users/{role}/{id}/status` | ADMIN JWT |

### Register (PowerShell)

```powershell
Invoke-RestMethod -Method POST -Uri "http://localhost:8081/api/auth/register" `
  -ContentType "application/json" `
  -Body '{
    "name": "Admin User",
    "email": "admin@eventbridge.com",
    "phone": "0770000000",
    "password": "password123",
    "role": "ADMIN"
  }'
```

### Login

```powershell
Invoke-RestMethod -Method POST -Uri "http://localhost:8081/api/auth/login" `
  -ContentType "application/json" `
  -Body '{
    "email": "admin@eventbridge.com",
    "password": "password123"
  }'
```

Copy the `token` from the response, then:

```powershell
$token = "PASTE_TOKEN_HERE"
Invoke-RestMethod -Uri "http://localhost:8081/api/admin/users/CUSTOMER" `
  -Headers @{ Authorization = "Bearer $token" }
```

Roles for register/login: `CUSTOMER`, `ADMIN`, `VENDOR`, `COORDINATOR`  
For vendor, also send `"category": "Caterer"` (or DJ, Decorator, Photographer).
