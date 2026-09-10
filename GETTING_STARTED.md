# Getting Started with UserStream

This guide will help you set up and run the UserStream microservices system on your local machine.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Docker** (version 20.x or higher)
- **Docker Compose** (version 1.29 or higher)
- **Git** (version 2.x or higher)

### Verify Prerequisites

```bash
# Check Docker
docker --version

# Check Docker Compose
docker-compose --version

# Check Git
git --version
```

## Installation Steps

### Step 1: Clone the Repository

```bash
git clone https://github.com/jafta1083/UserStream.git
cd UserStream
```

### Step 2: Configure Environment Variables

```bash
# Copy the environment template
cp .env.template .env

# Edit the .env file with your preferred editor
nano .env  # or vim .env, or code .env
```

**Important Environment Variables to Configure:**

```env
# SMTP Configuration (Required for email notifications)
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=your-email@gmail.com
SMTP_PASSWORD=your-app-password  # Use App Password for Gmail
FROM_EMAIL=noreply@userstream.com

# Database Configuration (Default values work for local development)
POSTGRES_DB=userstream
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres123

# RabbitMQ Configuration (Default values work for local development)
RABBITMQ_DEFAULT_USER=admin
RABBITMQ_DEFAULT_PASS=admin123
```

**Note for Gmail Users:** 
- You need to use an App Password, not your regular Gmail password
- Enable 2-factor authentication on your Google account
- Generate an App Password at: https://myaccount.google.com/apppasswords

### Step 3: Start the Services

```bash
# Start all services in detached mode
docker-compose up -d

# This will:
# 1. Pull necessary Docker images
# 2. Build custom service images
# 3. Create network and volumes
# 4. Start all containers
```

### Step 4: Verify Services are Running

```bash
# Check status of all services
docker-compose ps

# Expected output should show all services as "Up"
```

### Step 5: Access the Application

Once all services are running, you can access:

| Service | URL | Credentials |
|---------|-----|-------------|
| **Frontend** | http://localhost:3000 | N/A |
| **RabbitMQ Management** | http://localhost:15672 | admin / admin123 |
| **Database Service API** | http://localhost:8083/health | N/A |
| **Report Generator API** | http://localhost:8084/health | N/A |

## Using the Application

### 1. Register a User

**Via Frontend (Browser):**
1. Open http://localhost:3000
2. Fill in the registration form:
   - Name: John
   - Surname: Doe
   - Email: john.doe@example.com
3. Click Submit
4. You should receive a confirmation

**Via API (Command Line):**
```bash
curl -X POST http://localhost:8083/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John",
    "surname": "Doe",
    "email": "john.doe@example.com"
  }'
```

### 2. Generate a CSV Report

```bash
# Request report generation
curl -X POST http://localhost:8084/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "startDate": "2025-01-01",
    "endDate": "2025-12-31"
  }'

# This returns a reportId, e.g., "report_20251213_193700"
```

### 3. Download the Report

```bash
# Replace {reportId} with the actual ID from step 2
curl http://localhost:8084/api/reports/{reportId}/download -o users.csv
```

### 4. View All Users

```bash
curl http://localhost:8083/api/users
```

## Monitoring

### View Service Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f frontend
docker-compose logs -f queue-receiver
docker-compose logs -f sender
docker-compose logs -f database-service
docker-compose logs -f report-generator

# Last 100 lines
docker-compose logs --tail=100 queue-receiver
```

### Check Service Health

```bash
# Queue Receiver
curl http://localhost:8081/health

# Sender Service
curl http://localhost:8082/health

# Database Service
curl http://localhost:8083/health

# Report Generator
curl http://localhost:8084/health
```

### Monitor RabbitMQ

1. Open http://localhost:15672
2. Login with: admin / admin123
3. Navigate to "Queues" tab to see message statistics
4. Check "user.registration" queue for pending messages

### Check Database

```bash
# Connect to PostgreSQL
docker-compose exec postgres psql -U postgres -d userstream

# Inside PostgreSQL console:
\dt                                    # List tables
SELECT * FROM users;                   # View all users
SELECT COUNT(*) FROM users;            # Count users
\q                                     # Exit
```

## Development Workflow

### Making Changes to a Service

1. **Edit the service code** in `services/[service-name]/`

2. **Rebuild the service:**
   ```bash
   docker-compose build [service-name]
   ```

3. **Restart the service:**
   ```bash
   docker-compose restart [service-name]
   ```

4. **Or rebuild and restart in one command:**
   ```bash
   docker-compose up -d --build [service-name]
   ```

### Running Tests

```bash
# If tests are implemented in a service
docker-compose exec [service-name] npm test
```

## Troubleshooting

### Problem: Services won't start

**Solution:**
```bash
# Check logs for errors
docker-compose logs [service-name]

# Restart the service
docker-compose restart [service-name]

# Or restart all services
docker-compose restart
```

### Problem: Port already in use

**Solution:**
```bash
# Stop the service using the port
# Then change the port in docker-compose.yml
# Example: Change "3000:3000" to "3001:3000"
```

### Problem: Database connection failed

**Solution:**
```bash
# Check if PostgreSQL is running
docker-compose ps postgres

# Check PostgreSQL logs
docker-compose logs postgres

# Restart PostgreSQL
docker-compose restart postgres
```

### Problem: RabbitMQ connection failed

**Solution:**
```bash
# Check if RabbitMQ is running
docker-compose ps rabbitmq

# Check RabbitMQ logs
docker-compose logs rabbitmq

# Restart RabbitMQ
docker-compose restart rabbitmq
```

### Problem: Email notifications not working

**Solution:**
1. Verify SMTP credentials in `.env` file
2. For Gmail, use App Password (not regular password)
3. Check Sender service logs:
   ```bash
   docker-compose logs sender
   ```

### Problem: "No space left on device"

**Solution:**
```bash
# Clean up Docker
docker system prune -a
docker volume prune
```

## Stopping the Application

### Stop all services (keeps data)
```bash
docker-compose stop
```

### Stop and remove containers (keeps volumes/data)
```bash
docker-compose down
```

### Stop and remove everything (including data)
```bash
docker-compose down -v
```

## Resetting the Application

If you want to start fresh:

```bash
# Stop and remove everything
docker-compose down -v

# Remove all UserStream images
docker images | grep userstream | awk '{print $3}' | xargs docker rmi

# Start again
docker-compose up -d
```

## Next Steps

1. **Explore the Architecture:** Read [ARCHITECTURE.md](ARCHITECTURE.md) for detailed system design
2. **Understand the Structure:** Review [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) for code organization
3. **Read Service Documentation:** Check individual `services/*/README.md` files
4. **Customize Services:** Modify service code to fit your needs
5. **Add Features:** Extend the system with new capabilities

## Getting Help

If you encounter issues:

1. Check this guide for common problems
2. Review service-specific README files
3. Check Docker logs for error messages
4. Create an issue on GitHub

## Useful Commands Reference

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose stop

# View logs
docker-compose logs -f [service-name]

# Rebuild service
docker-compose build [service-name]

# Restart service
docker-compose restart [service-name]

# Scale service
docker-compose up -d --scale [service-name]=3

# Execute command in container
docker-compose exec [service-name] [command]

# Remove everything
docker-compose down -v
```

## Production Deployment

For production deployment:

1. Use proper secret management (not .env files)
2. Enable SSL/TLS for all services
3. Set up monitoring and alerting
4. Configure backup strategy
5. Use orchestration platform (Kubernetes)
6. Implement CI/CD pipeline
7. Set up load balancing
8. Configure auto-scaling

See [ARCHITECTURE.md](ARCHITECTURE.md) for more production considerations.

---

**Congratulations!** You now have UserStream running on your local machine. 🎉
