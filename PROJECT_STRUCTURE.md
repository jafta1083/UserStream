# UserStream Project Structure

## Directory Organization

```
UserStream/
├── .git/                           # Git repository
├── .gitignore                      # Git ignore rules
├── LICENSE                         # Project license
├── README.md                       # Main project documentation
├── ARCHITECTURE.md                 # Detailed architecture documentation
├── .env.template                   # Environment variables template
├── docker-compose.yml              # Docker orchestration configuration
│
├── database/                       # Database scripts
│   └── init.sql                    # Database initialization SQL
│
└── services/                       # Microservices directory
    │
    ├── frontend/                   # Service 1: Frontend Web Application
    │   ├── src/                    # Source code
    │   │   ├── components/         # React components
    │   │   │   ├── UserForm.jsx    # User registration form
    │   │   │   ├── NotificationBanner.jsx
    │   │   │   └── LoadingSpinner.jsx
    │   │   ├── services/           # Service integrations
    │   │   │   └── queueService.js # Message queue client
    │   │   ├── utils/              # Utility functions
    │   │   │   └── validation.js   # Input validation
    │   │   ├── App.js              # Main application
    │   │   └── index.js            # Entry point
    │   ├── public/                 # Static assets
    │   │   ├── index.html
    │   │   └── favicon.ico
    │   ├── package.json            # Dependencies
    │   ├── Dockerfile              # Docker configuration
    │   └── README.md               # Service documentation
    │
    ├── queue-receiver/             # Service 2: Queue Consumer
    │   ├── src/                    # Source code
    │   │   ├── config/             # Configuration
    │   │   │   └── queue.js        # Queue configuration
    │   │   ├── services/           # Business logic
    │   │   │   ├── queueConsumer.js  # Message consumer
    │   │   │   ├── databaseClient.js # Database service client
    │   │   │   └── senderClient.js   # Sender service client
    │   │   ├── utils/              # Utility functions
    │   │   │   ├── validator.js    # Message validation
    │   │   │   └── logger.js       # Logging utility
    │   │   └── index.js            # Entry point
    │   ├── tests/                  # Unit tests
    │   │   └── queueConsumer.test.js
    │   ├── package.json            # Dependencies
    │   ├── Dockerfile              # Docker configuration
    │   ├── healthcheck.js          # Health check script
    │   └── README.md               # Service documentation
    │
    ├── sender/                     # Service 3: Email Notification Service
    │   ├── src/                    # Source code
    │   │   ├── config/             # Configuration
    │   │   │   └── smtp.js         # SMTP configuration
    │   │   ├── services/           # Business logic
    │   │   │   ├── emailService.js # Email sending logic
    │   │   │   └── templateService.js # Template rendering
    │   │   ├── templates/          # Email templates
    │   │   │   ├── welcome.html    # Welcome email template
    │   │   │   └── notification.html
    │   │   ├── routes/             # API routes
    │   │   │   └── notifications.js
    │   │   ├── utils/              # Utility functions
    │   │   │   ├── validator.js    # Email validation
    │   │   │   └── logger.js       # Logging utility
    │   │   └── index.js            # Entry point
    │   ├── tests/                  # Unit tests
    │   │   └── emailService.test.js
    │   ├── package.json            # Dependencies
    │   ├── Dockerfile              # Docker configuration
    │   ├── healthcheck.js          # Health check script
    │   └── README.md               # Service documentation
    │
    ├── database-service/           # Service 4: Data Persistence Layer
    │   ├── src/                    # Source code
    │   │   ├── config/             # Configuration
    │   │   │   └── database.js     # Database configuration
    │   │   ├── models/             # Data models
    │   │   │   ├── User.js         # User model
    │   │   │   ├── NotificationLog.js
    │   │   │   └── ReportLog.js
    │   │   ├── services/           # Business logic
    │   │   │   └── userService.js  # User CRUD operations
    │   │   ├── routes/             # API routes
    │   │   │   └── users.js        # User endpoints
    │   │   ├── middleware/         # Middleware
    │   │   │   ├── validation.js   # Request validation
    │   │   │   └── errorHandler.js # Error handling
    │   │   └── index.js            # Entry point
    │   ├── migrations/             # Database migrations
    │   │   └── 001_create_users_table.sql
    │   ├── tests/                  # Unit tests
    │   │   └── userService.test.js
    │   ├── package.json            # Dependencies
    │   ├── Dockerfile              # Docker configuration
    │   ├── healthcheck.js          # Health check script
    │   └── README.md               # Service documentation
    │
    └── report-generator/           # Service 5: CSV Report Generator
        ├── src/                    # Source code
        │   ├── config/             # Configuration
        │   │   ├── database.js     # Database configuration
        │   │   └── scheduler.js    # Job scheduler config
        │   ├── services/           # Business logic
        │   │   ├── reportService.js   # Report generation logic
        │   │   ├── csvGenerator.js    # CSV file creation
        │   │   └── databaseClient.js  # Database service client
        │   ├── routes/             # API routes
        │   │   └── reports.js      # Report endpoints
        │   ├── utils/              # Utility functions
        │   │   ├── fileManager.js  # File operations
        │   │   └── logger.js       # Logging utility
        │   ├── jobs/               # Scheduled jobs
        │   │   └── scheduledReports.js
        │   └── index.js            # Entry point
        ├── reports/                # Generated reports directory
        │   └── .gitkeep            # Keep directory in git
        ├── tests/                  # Unit tests
        │   └── reportService.test.js
        ├── package.json            # Dependencies
        ├── Dockerfile              # Docker configuration
        ├── healthcheck.js          # Health check script
        └── README.md               # Service documentation
```

## Service Ports

| Service | Port | Purpose |
|---------|------|---------|
| Frontend | 3000 | Web application UI |
| Queue Receiver | 8081 | Message queue consumer |
| Sender | 8082 | Email notification service |
| Database Service | 8083 | Data persistence API |
| Report Generator | 8084 | CSV report generation |
| RabbitMQ | 5672 | Message queue AMQP |
| RabbitMQ Management | 15672 | Queue management UI |
| PostgreSQL | 5432 | Database server |

## Key Files

### Configuration Files
- `docker-compose.yml` - Orchestrates all services
- `.env.template` - Environment variables template
- `database/init.sql` - Database schema and initialization

### Documentation Files
- `README.md` - Main project overview
- `ARCHITECTURE.md` - Detailed architecture and design
- `services/*/README.md` - Individual service documentation

### Docker Files
- `services/*/Dockerfile` - Container configuration for each service
- `services/*/healthcheck.js` - Health check scripts

## Data Flow

1. **User Input** → Frontend → Message Queue
2. **Message Queue** → Queue Receiver → Database Service
3. **Queue Receiver** → Sender (Email Notification)
4. **Database Service** → Report Generator → CSV Files

## Technology Stack by Service

### Frontend
- React.js / Vue.js / Angular
- Axios for HTTP requests
- RabbitMQ AMQP client

### Queue Receiver
- Node.js / Java / Python
- Express.js / Spring Boot / Flask
- RabbitMQ client library

### Sender
- Node.js / Java / Python
- Nodemailer / JavaMail / smtplib
- Template engine (Handlebars/Jinja2)

### Database Service
- Node.js / Java / Python
- Express.js / Spring Boot / Flask
- PostgreSQL with ORM (Sequelize/Hibernate/SQLAlchemy)

### Report Generator
- Node.js / Java / Python
- CSV generation library
- node-cron / Quartz / APScheduler

## External Dependencies

### Infrastructure
- **RabbitMQ** - Message queue for asynchronous communication
- **PostgreSQL** - Relational database for data persistence
- **Docker** - Containerization platform
- **Docker Compose** - Multi-container orchestration

### Optional
- **Nginx** - Reverse proxy and load balancer
- **ELK Stack** - Centralized logging
- **Prometheus/Grafana** - Monitoring and metrics
- **Redis** - Caching layer

## Environment-Specific Configurations

### Development
- All services run locally with Docker Compose
- Hot-reload enabled for faster development
- Debug logging enabled
- Sample data pre-loaded

### Staging
- Services deployed to staging environment
- Production-like configuration
- Integration testing enabled
- Limited sample data

### Production
- Services deployed across multiple nodes
- Load balancing configured
- Auto-scaling enabled
- Production security settings
- Monitoring and alerting active

## Getting Started

1. **Clone Repository**
   ```bash
   git clone https://github.com/jafta1083/UserStream.git
   cd UserStream
   ```

2. **Configure Environment**
   ```bash
   cp .env.template .env
   # Edit .env with your configuration
   ```

3. **Start Services**
   ```bash
   docker-compose up -d
   ```

4. **Access Services**
   - Frontend: http://localhost:3000
   - RabbitMQ Management: http://localhost:15672
   - Database Service API: http://localhost:8083/api
   - Report Generator API: http://localhost:8084/api

## Development Workflow

1. Make changes to service code
2. Rebuild specific service: `docker-compose build [service-name]`
3. Restart service: `docker-compose restart [service-name]`
4. View logs: `docker-compose logs -f [service-name]`
5. Run tests: `docker-compose exec [service-name] npm test`

## Maintenance

### Backup Database
```bash
docker-compose exec postgres pg_dump -U postgres userstream > backup.sql
```

### Clear Queue
Access RabbitMQ Management UI and purge queues manually

### Clean Reports
```bash
docker-compose exec report-generator rm -rf /reports/*
```

## Troubleshooting

### Service Won't Start
- Check logs: `docker-compose logs [service-name]`
- Verify environment variables
- Ensure dependencies are running

### Database Connection Failed
- Verify PostgreSQL is running
- Check connection parameters in `.env`
- Ensure database is initialized

### Message Queue Issues
- Check RabbitMQ is running
- Verify queue configuration
- Check connection credentials

For more information, see the individual service README files.
