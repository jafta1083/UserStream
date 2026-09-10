# UserStream Quick Reference Card

## Quick Start
```bash
git clone https://github.com/jafta1083/UserStream.git
cd UserStream
cp .env.template .env
# Edit .env with your SMTP credentials
docker-compose up -d
```

## Access URLs
| Service | URL | Credentials |
|---------|-----|-------------|
| Frontend | http://localhost:3000 | N/A |
| RabbitMQ UI | http://localhost:15672 | admin/admin123 |
| Database API | http://localhost:8083/health | N/A |
| Reports API | http://localhost:8084/health | N/A |

## Five Microservices

### 1. Frontend (Port 3000)
- **Purpose:** Collects user data (name, surname, email)
- **Tech:** React/Vue/Angular
- **Location:** `services/frontend/`

### 2. Queue Receiver (Port 8081)
- **Purpose:** Processes messages from queue
- **Tech:** Node.js/Java/Python
- **Location:** `services/queue-receiver/`

### 3. Sender (Port 8082)
- **Purpose:** Sends email notifications
- **Tech:** Node.js with Nodemailer
- **Location:** `services/sender/`

### 4. Database Service (Port 8083)
- **Purpose:** Data persistence (SQL CRUD operations)
- **Tech:** Node.js + PostgreSQL
- **Location:** `services/database-service/`

### 5. Report Generator (Port 8084)
- **Purpose:** Generates CSV reports
- **Tech:** Node.js with csv-writer
- **Location:** `services/report-generator/`

## Common Commands

### Docker Compose
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose stop

# View logs
docker-compose logs -f [service-name]

# Rebuild service
docker-compose build [service-name]

# Restart service
docker-compose restart [service-name]

# Remove everything
docker-compose down -v
```

### Database
```bash
# Connect to PostgreSQL
docker-compose exec postgres psql -U postgres -d userstream

# View users
docker-compose exec postgres psql -U postgres -d userstream -c "SELECT * FROM users;"

# Backup database
docker-compose exec postgres pg_dump -U postgres userstream > backup.sql
```

### RabbitMQ
```bash
# Access Management UI
http://localhost:15672
Username: admin
Password: admin123
```

## API Examples

### Register User
```bash
curl -X POST http://localhost:8083/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John","surname":"Doe","email":"john@example.com"}'
```

### Generate Report
```bash
curl -X POST http://localhost:8084/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{"startDate":"2025-01-01","endDate":"2025-12-31"}'
```

### Get All Users
```bash
curl http://localhost:8083/api/users
```

## Service Architecture

```
User → Frontend → RabbitMQ → Queue Receiver
                                   ↓
                            Database Service
                              ↓         ↓
                      Sender Service   Report Generator
                         (Email)         (CSV Files)
```

## Environment Variables (.env)

```env
# Required for Sender Service
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=your-email@gmail.com
SMTP_PASSWORD=your-app-password
FROM_EMAIL=noreply@userstream.com

# Database (defaults work for local)
POSTGRES_DB=userstream
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres123

# RabbitMQ (defaults work for local)
RABBITMQ_DEFAULT_USER=admin
RABBITMQ_DEFAULT_PASS=admin123
```

## Documentation Files

| File | Description |
|------|-------------|
| `README.md` | Main project overview |
| `GETTING_STARTED.md` | Detailed setup guide |
| `ARCHITECTURE.md` | System architecture details |
| `PROJECT_STRUCTURE.md` | Directory organization |
| `SERVICE_NAMES.md` | Service naming explanation |
| `DIAGRAMS.md` | Visual architecture diagrams |
| `services/*/README.md` | Individual service docs |

## Database Schema

### users
- id (BIGSERIAL PRIMARY KEY)
- name (VARCHAR 100)
- surname (VARCHAR 100)
- email (VARCHAR 255 UNIQUE)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

### notification_logs
- id (BIGSERIAL PRIMARY KEY)
- user_id (BIGINT FK)
- notification_type (VARCHAR 50)
- status (VARCHAR 50)
- sent_at (TIMESTAMP)
- error_message (TEXT)

### report_logs
- id (BIGSERIAL PRIMARY KEY)
- report_name (VARCHAR 255)
- generated_at (TIMESTAMP)
- record_count (INT)
- file_path (VARCHAR 500)
- status (VARCHAR 50)

## Troubleshooting

### Service won't start
```bash
docker-compose logs [service-name]
docker-compose restart [service-name]
```

### Port already in use
```bash
# Edit docker-compose.yml and change external port
# Example: "3001:3000" instead of "3000:3000"
```

### Email not sending
- Use Gmail App Password (not regular password)
- Enable 2FA on Google account
- Check SMTP credentials in .env

### Database connection failed
```bash
docker-compose restart postgres
docker-compose logs postgres
```

### Reset everything
```bash
docker-compose down -v
docker system prune -a
docker-compose up -d
```

## Monitoring

### Health Checks
```bash
curl http://localhost:8081/health  # Queue Receiver
curl http://localhost:8082/health  # Sender
curl http://localhost:8083/health  # Database Service
curl http://localhost:8084/health  # Report Generator
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f queue-receiver

# Last 50 lines
docker-compose logs --tail=50 database-service
```

## Scaling Services

```bash
# Scale Queue Receiver to 3 instances
docker-compose up -d --scale queue-receiver=3

# Scale Report Generator to 2 instances
docker-compose up -d --scale report-generator=2
```

## File Locations

```
UserStream/
├── .env.template              # Environment config template
├── docker-compose.yml         # Service orchestration
├── database/init.sql          # Database initialization
└── services/
    ├── frontend/              # Service 1: Web UI
    ├── queue-receiver/        # Service 2: Message consumer
    ├── sender/                # Service 3: Email service
    ├── database-service/      # Service 4: Data persistence
    └── report-generator/      # Service 5: CSV reports
```

## Development Workflow

1. **Make changes** to service code
2. **Rebuild**: `docker-compose build [service-name]`
3. **Restart**: `docker-compose restart [service-name]`
4. **Test**: Access service via browser or curl
5. **View logs**: `docker-compose logs -f [service-name]`

## Key Features

✅ Event-driven architecture  
✅ Microservices design  
✅ Message queue (RabbitMQ)  
✅ PostgreSQL database  
✅ Email notifications  
✅ CSV report generation  
✅ Docker containerization  
✅ RESTful APIs  
✅ Health monitoring  
✅ Independent scaling  

## Next Steps

1. ✅ Review [GETTING_STARTED.md](GETTING_STARTED.md) for detailed setup
2. ✅ Check [ARCHITECTURE.md](ARCHITECTURE.md) for system design
3. ✅ Read individual service READMEs in `services/*/README.md`
4. ✅ Configure SMTP settings in `.env`
5. ✅ Start services with `docker-compose up -d`
6. ✅ Test the system with sample data
7. ✅ Customize services for your needs

## Support Resources

- **GitHub Issues**: Report bugs and request features
- **Documentation**: Read the comprehensive docs
- **Architecture Diagrams**: See [DIAGRAMS.md](DIAGRAMS.md)
- **Service Naming**: See [SERVICE_NAMES.md](SERVICE_NAMES.md)

---

**Answer to "Can you come up with the name of the service?"**

The fifth service is called **Report Generator Service**. It generates CSV files from user registration data stored in the database. The service provides both on-demand and scheduled report generation capabilities.

Alternative names considered:
- CSV Export Service
- Data Export Service
- Report Service
- File Generator Service

**Report Generator Service** was chosen because it clearly describes the purpose and is extensible for future enhancements like PDF and Excel reports.

---

© 2025 UserStream Project. Licensed under the terms in the LICENSE file.
