# UserStream
UserStream is an event-driven microservices system that collects user registration data via a frontend,
processes it through message queues, stores it in a SQL database, generates CSV reports, logs activities,
and sends notifications using independent backend services.

## Project Structure

```
UserStream/
├── web/          # Main web API (Javalin) - Port 7070
├── users/        # User management service - Port 7000
├── events/       # Event processing service - Port 7001
├── reports/      # Report generation service - Port 7002
├── notification/ # Notification service - Port 7003
├── alert/        # Alert service - Port 7004
└── common/       # Shared utilities and database config
```

## Tech Stack

- **Java 21**
- **Maven** (multi-module project)
- **Javalin 5.6.3** - Web framework
- **Jackson 2.15.0** - JSON serialization
- **ActiveMQ 5.17.2** - Message queue
- **SQLite** - Database

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+

### Build
```bash
mvn clean install -DskipTests
```

### Run
```bash
mvn exec:java -pl web
```

The API will start on `http://localhost:7070`

## API Endpoints

### Quick Reference
- `POST /user` - Register a new user
- `GET /users` - Get all users
- `GET /user/{id}` - Get user by ID
- `DELETE /user/{id}` - Delete user

---

## CURL Examples

### Web Module (Port 7070) - Main API Gateway

#### Health Check
```bash
curl http://localhost:7070/health
```

#### Users (via API Gateway)
```bash
# Get all users
curl http://localhost:7070/api/users

# Create a new user
curl -X POST http://localhost:7070/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John","surname":"Doe","email":"john@example.com"}'
```

#### Reports (via API Gateway)
```bash
# Get dashboard report
curl http://localhost:7070/api/reports/dashboard

# Get status report
curl http://localhost:7070/api/reports/status
```

#### Web Requests
```bash
# Get all requests
curl http://localhost:7070/requests

# Get request by ID
curl http://localhost:7070/requests/1

# Create a request
curl -X POST http://localhost:7070/requests \
  -H "Content-Type: application/json" \
  -d '{"type":"registration","data":"sample"}'

# Delete a request
curl -X DELETE http://localhost:7070/requests/1
```

---

### Users Module (Port 7000)

```bash
# Get all users
curl http://localhost:7000/users

# Get user by ID
curl http://localhost:7000/users/abc123-uuid

# Create a user
curl -X POST http://localhost:7000/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane","surname":"Smith","email":"jane@example.com"}'

# Update a user
curl -X PUT http://localhost:7000/users/abc123-uuid \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane","surname":"Johnson","email":"jane.johnson@example.com"}'

# Delete a user
curl -X DELETE http://localhost:7000/users/abc123-uuid
```

---

### Events Module (Port 7001)

```bash
# Get all events
curl http://localhost:7001/events

# Get event by ID
curl http://localhost:7001/events/1

# Create an event
curl -X POST http://localhost:7001/events \
  -H "Content-Type: application/json" \
  -d '{"type":"USER_REGISTERED","userId":"abc123-uuid","timestamp":"2026-01-27T10:00:00"}'
```

---

### Reports Module (Port 7002)

```bash
# Get all reports
curl http://localhost:7002/reports

# Get report by ID
curl http://localhost:7002/reports/1

# Create a report
curl -X POST http://localhost:7002/reports \
  -H "Content-Type: application/json" \
  -d '{"title":"Monthly Report","type":"summary"}'

# Get all user reports
curl http://localhost:7002/reports/users

# Create a user report
curl -X POST http://localhost:7002/reports/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John","surname":"Doe","email":"john@example.com"}'

# Get user reports as CSV
curl http://localhost:7002/reports/users/csv

# Generate CSV from data
curl -X POST http://localhost:7002/reports/generate-csv \
  -H "Content-Type: application/json" \
  -d '[["Name","Email"],["John","john@example.com"],["Jane","jane@example.com"]]'
```

---

### Notification Module (Port 7003)

```bash
# Get all notifications
curl http://localhost:7003/notifications

# Get notification by ID
curl http://localhost:7003/notifications/1

# Create a notification
curl -X POST http://localhost:7003/notifications \
  -H "Content-Type: application/json" \
  -d '{"userId":"abc123-uuid","message":"Welcome to UserStream!","type":"email"}'

# Send a notification
curl -X POST http://localhost:7003/notifications/1/send
```

---

### Alert Module (Port 7004)

```bash
# Get all alerts
curl http://localhost:7004/alerts

# Get alert by ID
curl http://localhost:7004/alerts/1

# Create an alert
curl -X POST http://localhost:7004/alerts \
  -H "Content-Type: application/json" \
  -d '{"userId":"abc123-uuid","message":"New login detected","severity":"info"}'

# Get alerts for a user
curl http://localhost:7004/alerts/user/abc123-uuid

# Get unread alerts for a user
curl http://localhost:7004/alerts/user/abc123-uuid/unread

# Mark alert as read
curl -X POST http://localhost:7004/alerts/1/read

# Mark all user alerts as read
curl -X POST http://localhost:7004/alerts/user/abc123-uuid/read

# Delete an alert
curl -X DELETE http://localhost:7004/alerts/1
```

---

## Running Individual Microservices

Each module can be run independently:

```bash
# Run Users service on port 7000
mvn exec:java -pl users

# Run Events service on port 7001
mvn exec:java -pl events

# Run Reports service on port 7002
mvn exec:java -pl reports

# Run Notification service on port 7003
mvn exec:java -pl notification

# Run Alert service on port 7004
mvn exec:java -pl alert

# Run Web (main gateway) on port 7070
mvn exec:java -pl web
```

## Changelog

### January 2026 - Bug Fixes & Improvements

#### Critical Fixes
| Issue | Description | Solution |
|-------|-------------|----------|
| UUID Parsing Crash | `UserData.id` was `int`, causing UUID parse failures | Changed to `String` type |
| Null Repositories | Services had uninitialized repositories causing NPE | Initialized all repositories |
| Abstract Class Error | `InMemoryAlertRepository` was abstract, couldn't instantiate | Removed `abstract` keyword |
| Circular Dependency | `common` module depended on `users` module | Refactored `DatabaseConfig` to remove user-specific code |
| SQL Syntax Error | Malformed column definition in table creation | Fixed SQL syntax |

#### Medium Priority Fixes
| Issue | Description | Solution |
|-------|-------------|----------|
| Wrong Path Parameter | `AlertService` used `id` instead of `userId` | Fixed path parameter name |
| Inconsistent ID Types | Mixed `int`/`String` ID types across codebase | Standardized to `String` |
| Empty CSV Methods | `generateCSV()` methods returned empty strings | Implemented CSV generation logic |
| Version Mismatch | Hardcoded Jackson version in web module | Inherited from parent POM |
| Missing .gitignore | Build artifacts not ignored | Added `target/`, `.idea/`, `*.csv` |

#### Code Quality Improvements
| Issue | Description | Solution |
|-------|-------------|----------|
| Repository Naming | `InMemoryUserRepository` used in wrong modules | Renamed to proper names (`InMemoryEventRepository`, `InMemoryReportRepository`, `InMemoryNotificationRepository`) |
| Database Operations | User DB operations in common module caused coupling | Created `DatabaseUserRepository` in users module |
| SLF4J Conflicts | ActiveMQ logging conflicts | Excluded conflicting SLF4J bindings |

## License

See [LICENSE](LICENSE) for details.
