# UserStream Microservices Architecture

## Overview
UserStream is an event-driven microservices system designed to handle user registration data efficiently through a distributed architecture.

## System Architecture

```
┌─────────────┐
│  Frontend   │ (Service 1)
│  Web App    │
└──────┬──────┘
       │
       ↓
┌─────────────────────────────────────┐
│   Message Queue (RabbitMQ/Kafka)    │
└─────────────────────────────────────┘
       │
       ↓
┌──────────────┐     ┌──────────────┐
│    Queue     │────→│   Database   │ (Service 4)
│   Receiver   │     │   Service    │
└──────┬───────┘     └──────┬───────┘
       │                    │
       ↓                    ↓
┌──────────────┐     ┌──────────────┐
│    Sender    │     │   Report     │ (Service 5)
│   Service    │     │  Generator   │
└──────────────┘     └──────────────┘
(Service 3)          (CSV Export)
(Service 2)
```

## Microservices

### 1. Frontend Service
**Location:** `services/frontend`
**Purpose:** User-facing web application
**Responsibilities:**
- Collects user registration data (name, surname, email)
- Validates user input
- Publishes user data to message queue
- Provides user interface for data submission

**Technology Stack:** HTML, CSS, JavaScript (React/Vue/Angular)
**Port:** 3000

---

### 2. Queue Receiver Service
**Location:** `services/queue-receiver`
**Purpose:** Message queue consumer
**Responsibilities:**
- Listens to message queue for incoming user data
- Validates message format
- Routes messages to appropriate services
- Handles message acknowledgment and error handling

**Technology Stack:** Java/Python/Node.js
**Port:** 8081

---

### 3. Sender Service
**Location:** `services/sender`
**Purpose:** Notification and communication service
**Responsibilities:**
- Sends email notifications to registered users
- Sends confirmation messages
- Handles notification templates
- Manages notification delivery status

**Technology Stack:** Java/Python/Node.js
**Port:** 8082

---

### 4. Database Service
**Location:** `services/database-service`
**Purpose:** Data persistence layer
**Responsibilities:**
- Stores user registration data in SQL database
- Provides CRUD operations for user data
- Handles data validation and integrity
- Manages database connections and transactions
- Retrieves data for reporting

**Technology Stack:** Java/Python/Node.js with PostgreSQL/MySQL
**Port:** 8083

---

### 5. Report Generator Service
**Location:** `services/report-generator`
**Purpose:** CSV file generation and reporting
**Responsibilities:**
- Retrieves user data from database
- Generates CSV reports with user information
- Creates formatted export files
- Manages report scheduling and delivery
- Maintains report history and logs

**Technology Stack:** Java/Python/Node.js
**Port:** 8084

---

## Data Flow

1. **User Registration:**
   - User submits name, surname, and email through Frontend
   - Frontend publishes message to queue

2. **Message Processing:**
   - Queue Receiver consumes message from queue
   - Queue Receiver forwards data to Database Service

3. **Data Storage:**
   - Database Service validates and stores user data
   - Database Service confirms successful storage

4. **Notifications:**
   - Sender Service is triggered to send confirmation email
   - Email sent to user's registered email address

5. **Report Generation:**
   - Report Generator Service queries Database Service
   - CSV file created with all user registrations
   - Report stored and made available for download

---

## Message Queue

**Recommended:** RabbitMQ or Apache Kafka

**Queues:**
- `user.registration` - User registration events
- `email.notifications` - Email notification requests
- `report.requests` - Report generation requests

---

## Database Schema

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE notification_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    notification_type VARCHAR(50),
    status VARCHAR(50),
    sent_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE report_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_name VARCHAR(255),
    generated_at TIMESTAMP,
    record_count INT,
    file_path VARCHAR(500)
);
```

---

## Environment Variables

Each service should have its own `.env` file:

### Frontend
```
REACT_APP_API_URL=http://localhost:8081
REACT_APP_QUEUE_URL=amqp://localhost:5672
```

### Queue Receiver
```
QUEUE_URL=amqp://localhost:5672
DATABASE_SERVICE_URL=http://localhost:8083
SENDER_SERVICE_URL=http://localhost:8082
```

### Sender Service
```
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=your-email@example.com
SMTP_PASSWORD=your-password
```

### Database Service
```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=userstream
DB_USER=postgres
DB_PASSWORD=password
```

### Report Generator
```
DATABASE_SERVICE_URL=http://localhost:8083
REPORT_OUTPUT_DIR=/reports
CSV_DELIMITER=,
```

---

## Deployment

### Docker Compose
All services can be deployed using Docker Compose. See `docker-compose.yml` in the root directory.

### Individual Service Deployment
Each service has its own Dockerfile and can be deployed independently.

---

## Development

### Prerequisites
- Docker and Docker Compose
- Node.js (for Frontend)
- Java/Python (for backend services)
- PostgreSQL or MySQL
- RabbitMQ or Kafka

### Running Locally
```bash
# Start all services with Docker Compose
docker-compose up

# Or run individual services
cd services/frontend && npm start
cd services/queue-receiver && npm start
cd services/sender && npm start
cd services/database-service && npm start
cd services/report-generator && npm start
```

---

## Testing

Each service includes its own test suite:
```bash
# Run tests for a specific service
cd services/[service-name]
npm test  # or mvn test, pytest, etc.
```

---

## Monitoring and Logging

- All services log to stdout/stderr
- Logs are aggregated using centralized logging (ELK Stack recommended)
- Health check endpoints available at `/health` for each service
- Metrics exposed at `/metrics` (Prometheus format)

---

## API Documentation

API documentation for each service is available at:
- Frontend: See `services/frontend/README.md`
- Queue Receiver: `http://localhost:8081/api-docs`
- Sender: `http://localhost:8082/api-docs`
- Database Service: `http://localhost:8083/api-docs`
- Report Generator: `http://localhost:8084/api-docs`

---

## Security Considerations

1. **Authentication:** Use JWT tokens for service-to-service communication
2. **Encryption:** All data in transit should use TLS/SSL
3. **Input Validation:** Validate all input at each service boundary
4. **Secret Management:** Use environment variables or secret management systems
5. **Rate Limiting:** Implement rate limiting on public endpoints

---

## Scalability

Each service can be scaled independently:
- Frontend: Scale horizontally behind load balancer
- Queue Receiver: Multiple instances can consume from queue
- Sender: Scale based on notification volume
- Database Service: Use database replication and connection pooling
- Report Generator: Scale for concurrent report generation

---

## Future Enhancements

- Add user authentication and authorization
- Implement real-time data dashboard
- Add support for multiple report formats (PDF, Excel)
- Implement data analytics and insights
- Add webhook support for third-party integrations
