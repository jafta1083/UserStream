# UserStream

UserStream is an event-driven microservices system that collects user registration data via a frontend, processes it through message queues, stores it in a SQL database, generates CSV reports, logs activities, and sends notifications using independent backend services.

## Overview

This project demonstrates a modern microservices architecture with five independent services working together to handle user registration and reporting workflows.

## Architecture

The system consists of five microservices:

1. **Frontend Service** - Web application for collecting user data (name, surname, email)
2. **Queue Receiver Service** - Consumes messages from the queue and routes them to appropriate services
3. **Sender Service** - Handles email notifications and communications
4. **Database Service** - Manages data persistence and retrieval operations
5. **Report Generator Service** - Generates CSV reports from stored user data

## Quick Start

### Prerequisites

- Docker and Docker Compose
- Git

### Running the Application

1. **Clone the repository:**
   ```bash
   git clone https://github.com/jafta1083/UserStream.git
   cd UserStream
   ```

2. **Configure environment variables:**
   ```bash
   cp .env.template .env
   # Edit .env with your SMTP credentials and other settings
   ```

3. **Start all services:**
   ```bash
   docker-compose up -d
   ```

4. **Access the application:**
   - Frontend: http://localhost:3000
   - RabbitMQ Management: http://localhost:15672 (admin/admin123)
   - Database Service API: http://localhost:8083
   - Report Generator API: http://localhost:8084

## System Components

### Infrastructure
- **Message Queue:** RabbitMQ (ports 5672, 15672)
- **Database:** PostgreSQL (port 5432)
- **Services:** 5 microservices (ports 3000, 8081-8084)

### Data Flow
```
User Input → Frontend → Message Queue → Queue Receiver
                                           ↓
                                    Database Service
                                      ↓         ↓
                              Sender Service   Report Generator
                             (Email)          (CSV Reports)
```

## Service Details

| Service | Port | Description |
|---------|------|-------------|
| **Frontend** | 3000 | User-facing web application for data collection |
| **Queue Receiver** | 8081 | Processes messages from the queue |
| **Sender** | 8082 | Sends email notifications |
| **Database Service** | 8083 | Handles data persistence (SQL) |
| **Report Generator** | 8084 | Generates CSV reports |

## Features

- ✅ User registration with validation
- ✅ Event-driven architecture with message queues
- ✅ Asynchronous message processing
- ✅ Email notifications
- ✅ CSV report generation
- ✅ RESTful APIs
- ✅ Docker containerization
- ✅ Health checks and monitoring
- ✅ Scalable microservices design

## Documentation

- [**ARCHITECTURE.md**](ARCHITECTURE.md) - Detailed system architecture and design
- [**PROJECT_STRUCTURE.md**](PROJECT_STRUCTURE.md) - Complete directory structure and file organization
- [**services/*/README.md**](services/) - Individual service documentation

## Development

### Building Individual Services

```bash
# Build a specific service
docker-compose build frontend

# Rebuild and restart a service
docker-compose up -d --build queue-receiver
```

### Viewing Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f database-service
```

### Running Tests

```bash
# Run tests for a specific service
docker-compose exec queue-receiver npm test
```

### Stopping Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## API Examples

### Submit User Registration (Frontend → Queue)
```javascript
POST /api/submit
{
  "name": "John",
  "surname": "Doe",
  "email": "john.doe@example.com"
}
```

### Store User Data (Database Service)
```bash
curl -X POST http://localhost:8083/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John","surname":"Doe","email":"john.doe@example.com"}'
```

### Generate CSV Report (Report Generator)
```bash
curl -X POST http://localhost:8084/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{"startDate":"2025-01-01","endDate":"2025-12-31"}'
```

### Download Report
```bash
curl http://localhost:8084/api/reports/{reportId}/download -o users.csv
```

## Database Schema

```sql
-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Notification logs
CREATE TABLE notification_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Report logs
CREATE TABLE report_logs (
    id BIGSERIAL PRIMARY KEY,
    report_name VARCHAR(255) NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    record_count INT NOT NULL,
    file_path VARCHAR(500),
    status VARCHAR(50) NOT NULL
);
```

## Configuration

### Environment Variables

Key configuration options in `.env`:

```env
# SMTP Configuration (Sender Service)
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=your-email@example.com
SMTP_PASSWORD=your-app-password

# Database Configuration
POSTGRES_DB=userstream
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres123

# RabbitMQ Configuration
RABBITMQ_DEFAULT_USER=admin
RABBITMQ_DEFAULT_PASS=admin123
```

## Monitoring

### Health Checks

Each service exposes a health check endpoint:
```bash
curl http://localhost:8081/health  # Queue Receiver
curl http://localhost:8082/health  # Sender
curl http://localhost:8083/health  # Database Service
curl http://localhost:8084/health  # Report Generator
```

### RabbitMQ Management

Access the RabbitMQ management interface at http://localhost:15672
- Username: admin
- Password: admin123

## Scalability

Each microservice can be scaled independently:

```bash
# Scale Queue Receiver to 3 instances
docker-compose up -d --scale queue-receiver=3

# Scale Report Generator to 2 instances
docker-compose up -d --scale report-generator=2
```

## Security Considerations

- Use environment variables for sensitive credentials
- Enable TLS/SSL for production deployments
- Implement authentication/authorization for APIs
- Validate all user inputs
- Use secure SMTP connections
- Regularly update dependencies

## Troubleshooting

### Service Won't Start
```bash
docker-compose logs [service-name]
```

### Database Connection Issues
```bash
docker-compose exec postgres psql -U postgres -d userstream
```

### Clear Message Queue
Access RabbitMQ Management UI and purge queues manually

### Reset Everything
```bash
docker-compose down -v
docker-compose up -d
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the terms in the [LICENSE](LICENSE) file.

## Support

For issues and questions:
- Create an issue on GitHub
- Check the documentation in the `/services` directories
- Review the ARCHITECTURE.md file

## Technology Stack

- **Frontend:** HTML/CSS/JavaScript (React/Vue/Angular)
- **Backend:** Node.js/Java/Python
- **Database:** PostgreSQL
- **Message Queue:** RabbitMQ
- **Containerization:** Docker & Docker Compose
- **Email:** SMTP (Nodemailer/JavaMail/smtplib)

## Roadmap

- [ ] Add authentication and authorization
- [ ] Implement API gateway
- [ ] Add monitoring dashboard (Grafana)
- [ ] Support for multiple report formats (PDF, Excel)
- [ ] Real-time data streaming
- [ ] Integration tests
- [ ] CI/CD pipeline
- [ ] Kubernetes deployment manifests

---

**Note:** This is a demonstration project showcasing microservices architecture. Adapt it to your specific production requirements.
