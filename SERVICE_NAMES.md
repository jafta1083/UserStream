# Service Names and Their Purpose

## The Five Microservices

This document provides clarity on the naming and purpose of each microservice in the UserStream system.

---

## 1. Frontend Service
**Directory:** `services/frontend`  
**Port:** 3000  
**Technology:** React.js / Vue.js / Angular  

**Purpose:**
- User-facing web application
- Collects user registration data (name, surname, email)
- Validates input before submission
- Publishes data to message queue

**Key Responsibility:** User interface and data collection

---

## 2. Queue Receiver Service
**Directory:** `services/queue-receiver`  
**Port:** 8081  
**Technology:** Node.js / Java / Python  

**Purpose:**
- Consumes messages from RabbitMQ queue
- Validates message format and content
- Routes data to Database Service for storage
- Triggers Sender Service for notifications
- Handles retry logic for failed messages

**Key Responsibility:** Message queue consumer and data router

---

## 3. Sender Service
**Directory:** `services/sender`  
**Port:** 8082  
**Technology:** Node.js / Java / Python with Nodemailer/JavaMail  

**Purpose:**
- Sends email notifications to registered users
- Manages email templates
- Tracks notification delivery status
- Handles email delivery failures and retries

**Key Responsibility:** Email notification and communication

---

## 4. Database Service
**Directory:** `services/database-service`  
**Port:** 8083  
**Technology:** Node.js / Java / Python with PostgreSQL  

**Purpose:**
- Provides RESTful API for data operations
- Stores user registration data in SQL database
- Performs CRUD operations (Create, Read, Update, Delete)
- Manages data integrity and validation
- Retrieves data for reporting

**Key Responsibility:** Data persistence and retrieval

---

## 5. Report Generator Service
**Directory:** `services/report-generator`  
**Port:** 8084  
**Technology:** Node.js / Java / Python with CSV libraries  

**Purpose:**
- Generates CSV files containing user registration data
- Provides on-demand and scheduled report generation
- Manages report history and file storage
- Offers download API for generated reports
- Maintains report logs in database

**Key Responsibility:** CSV report generation and export

**Alternative Names Considered:**
- CSV Export Service
- Report Service
- Data Export Service
- File Generator Service
- Export Manager

**Final Name:** **Report Generator Service** (or **Report Generator**)

---

## Service Interaction Flow

```
┌──────────────┐
│   Frontend   │ (Service 1)
│   Service    │
└──────┬───────┘
       │ Publishes user data
       ↓
┌─────────────────────────┐
│   Message Queue         │
│   (RabbitMQ)           │
└─────────────────────────┘
       │
       ↓
┌──────────────┐
│    Queue     │ (Service 2)
│   Receiver   │
│   Service    │
└───┬──────┬───┘
    │      │
    │      └─────────────────┐
    │                        │
    ↓                        ↓
┌──────────────┐      ┌──────────────┐
│   Database   │      │    Sender    │ (Service 3)
│   Service    │      │   Service    │
└───────┬──────┘      └──────────────┘
        │ (Service 4)
        │
        ↓
┌──────────────┐
│    Report    │ (Service 5)
│  Generator   │
│   Service    │
└──────────────┘
```

---

## Why These Names?

### Frontend Service
Self-explanatory - it's the front-facing part of the application.

### Queue Receiver Service
Clearly indicates its role: receiving and processing messages from the queue.

### Sender Service
Concise name that reflects its purpose: sending communications (emails).

### Database Service
Straightforward - it manages all database operations.

### Report Generator Service
Descriptive name that clearly communicates its function: generating reports (CSV files).

This name was chosen because:
- **Clear Purpose:** Immediately conveys what the service does
- **Extensible:** Can be expanded to support other report formats (PDF, Excel) in the future
- **Professional:** Aligns with industry standard naming conventions
- **Searchable:** Easy to find in documentation and logs
- **Memorable:** Simple and easy to remember

---

## Alternative Architecture Considerations

### If You Need to Rename Services

The services can be renamed to fit your organization's naming conventions:

| Current Name | Alternative Names |
|--------------|-------------------|
| Frontend Service | Web App, User Interface, Client App |
| Queue Receiver Service | Message Consumer, Event Processor, Queue Worker |
| Sender Service | Notification Service, Email Service, Messenger |
| Database Service | Data Service, Persistence Layer, Storage Service |
| Report Generator Service | Export Service, CSV Generator, Report Service |

### Naming Conventions

When naming microservices, consider:

1. **Clarity:** Name should clearly indicate purpose
2. **Brevity:** Keep names concise but meaningful
3. **Consistency:** Use consistent naming patterns
4. **Domain Language:** Use terms familiar to your business domain
5. **Scalability:** Name should remain relevant as service evolves

---

## Service Responsibilities Summary

| Service | Input | Processing | Output |
|---------|-------|------------|--------|
| **Frontend** | User form data | Validation, Queue publish | Message to queue |
| **Queue Receiver** | Queue messages | Validation, Routing | API calls to other services |
| **Sender** | Email requests | Template rendering, SMTP | Email delivery |
| **Database** | API requests | CRUD operations | Data stored/retrieved |
| **Report Generator** | Report requests | Data fetch, CSV generation | CSV files |

---

## Service Configuration Summary

Each service is independently:
- **Deployable:** Can be deployed separately
- **Scalable:** Can scale based on load
- **Maintainable:** Has its own codebase
- **Testable:** Can be tested in isolation
- **Resilient:** Failure doesn't affect other services

---

## Conclusion

The **Report Generator Service** is the fifth microservice in the UserStream system. It's responsible for creating CSV reports from user registration data, providing both on-demand and scheduled report generation capabilities.

This naming choice reflects its primary function while leaving room for future enhancements such as additional report formats and advanced analytics features.
