# Database Service

## Overview
The Database Service is the data persistence layer that manages all database operations for user registration data.

## Purpose
- Stores user registration data in SQL database
- Provides CRUD operations for user data
- Handles data validation and integrity
- Manages database connections and transactions
- Retrieves data for reporting and analytics
- Maintains data consistency and reliability

## Technology Stack
- **Runtime:** Node.js / Java / Python
- **Framework:** Express.js / Spring Boot / Flask
- **ORM:** Sequelize / Hibernate / SQLAlchemy
- **Database:** PostgreSQL / MySQL / MariaDB
- **Connection Pool:** pg-pool / HikariCP / psycopg2
- **Logging:** Winston / Log4j / Python logging

## Features
- RESTful API for data operations
- Database connection pooling
- Transaction management
- Data validation
- Query optimization
- Database migration support
- Backup and restore capabilities

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_created_at (created_at)
);
```

### Notification Logs Table
```sql
CREATE TABLE notification_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    error_message TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_sent_at (sent_at)
);
```

### Report Logs Table
```sql
CREATE TABLE report_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_name VARCHAR(255) NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    record_count INT NOT NULL,
    file_path VARCHAR(500),
    status VARCHAR(50) NOT NULL,
    INDEX idx_generated_at (generated_at)
);
```

## API Endpoints

### Create User
```
POST /api/users
Content-Type: application/json

{
  "name": "John",
  "surname": "Doe",
  "email": "john.doe@example.com"
}
```

### Get User by ID
```
GET /api/users/:id
```

### Get User by Email
```
GET /api/users/email/:email
```

### Get All Users
```
GET /api/users?page=1&limit=50
```

### Update User
```
PUT /api/users/:id
Content-Type: application/json

{
  "name": "John",
  "surname": "Doe"
}
```

### Delete User
```
DELETE /api/users/:id
```

### Get Users for Report
```
GET /api/users/report?startDate=2025-01-01&endDate=2025-12-31
```

### Health Check
```
GET /health
```

### Metrics
```
GET /metrics
```

## Configuration

### Environment Variables
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=userstream
DB_USER=postgres
DB_PASSWORD=password
DB_POOL_MIN=2
DB_POOL_MAX=10
DB_SSL=false
PORT=8083
```

## Installation

### Prerequisites
- Node.js (v14+) or Java (11+) or Python (3.8+)
- PostgreSQL (v12+) or MySQL (v8+)

### Setup
```bash
cd services/database-service
npm install  # or mvn install, pip install -r requirements.txt
```

### Database Setup
```bash
# Create database
createdb userstream

# Run migrations
npm run migrate  # or mvn flyway:migrate, alembic upgrade head
```

## Running the Service

### Development Mode
```bash
npm start  # or java -jar app.jar, python app.py
```
The service will start at `http://localhost:8083`

### Production Mode
```bash
npm run start:prod
```

## Docker Deployment

### Build Docker Image
```bash
docker build -t userstream-database-service .
```

### Run Docker Container
```bash
docker run -p 8083:8083 userstream-database-service
```

## Data Validation

### User Data Validation Rules
- **Name:** Required, 2-100 characters, letters and spaces only
- **Surname:** Required, 2-100 characters, letters and spaces only
- **Email:** Required, valid email format, max 255 characters, must be unique

## Database Operations

### Connection Pooling
- Minimum connections: 2
- Maximum connections: 10
- Connection timeout: 30 seconds
- Idle timeout: 10 minutes

### Transaction Management
All write operations use transactions:
- Automatic rollback on error
- ACID compliance
- Isolation level: READ COMMITTED

## Performance Optimization

### Indexing Strategy
- Primary key index on `id`
- Unique index on `email`
- Index on `created_at` for time-based queries
- Foreign key indexes for joins

### Query Optimization
- Use prepared statements
- Implement pagination for large result sets
- Cache frequently accessed data
- Use connection pooling

## Backup and Recovery

### Backup Strategy
```bash
# Daily backup
pg_dump userstream > backup_$(date +%Y%m%d).sql

# Automated backup
0 2 * * * /usr/local/bin/backup-db.sh
```

### Restore
```bash
psql userstream < backup_20251213.sql
```

## Monitoring

### Key Metrics
- Database connection pool usage
- Query execution time
- Active connections
- Transaction throughput
- Error rate

### Alerts
- Connection pool exhaustion
- Slow queries (>1s)
- Failed transactions
- Database unavailability

## Error Handling

### Error Codes
- `400` - Bad request (validation error)
- `404` - User not found
- `409` - Duplicate email
- `500` - Database error
- `503` - Database unavailable

## Testing
```bash
npm test
```

### Test Coverage
- CRUD operations
- Data validation
- Transaction handling
- Error scenarios
- Connection pooling

## Migration Management

### Create New Migration
```bash
npm run migration:create -- add-new-column
```

### Run Migrations
```bash
npm run migrate
```

### Rollback Migration
```bash
npm run migrate:rollback
```

## File Structure
```
database-service/
├── src/
│   ├── config/
│   │   └── database.js
│   ├── models/
│   │   ├── User.js
│   │   ├── NotificationLog.js
│   │   └── ReportLog.js
│   ├── services/
│   │   └── userService.js
│   ├── routes/
│   │   └── users.js
│   ├── middleware/
│   │   ├── validation.js
│   │   └── errorHandler.js
│   └── index.js
├── migrations/
│   └── 001_create_users_table.sql
├── tests/
│   └── userService.test.js
├── package.json
├── Dockerfile
└── README.md
```

## Security

### Best Practices
- Use parameterized queries to prevent SQL injection
- Encrypt sensitive data at rest
- Use SSL/TLS for database connections
- Implement proper access control
- Regular security audits
- Keep database software updated

## Contributing
Please follow the coding standards and submit pull requests for any improvements.

## License
See the LICENSE file in the root directory.
