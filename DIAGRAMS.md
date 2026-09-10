# UserStream System Architecture Diagram

## High-Level Architecture

```
                                    ┌─────────────────────────────────────┐
                                    │         USER STREAM SYSTEM           │
                                    └─────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────────────────────┐
│                                                                               │
│  ┌─────────────────┐                                                         │
│  │   Web Browser   │                                                         │
│  └────────┬────────┘                                                         │
│           │                                                                   │
│           │ HTTP                                                              │
│           ↓                                                                   │
│  ┌─────────────────┐                                                         │
│  │  Service 1:     │                                                         │
│  │  Frontend       │  Collects user data (name, surname, email)             │
│  │  Port: 3000     │                                                         │
│  └────────┬────────┘                                                         │
│           │                                                                   │
│           │ AMQP                                                              │
│           ↓                                                                   │
│  ┌─────────────────────────────────────────────────────┐                    │
│  │         Message Queue (RabbitMQ)                    │                    │
│  │         Port: 5672, Management: 15672               │                    │
│  └─────────────────────────────────────────────────────┘                    │
│           │                                                                   │
│           │ Queue: user.registration                                         │
│           ↓                                                                   │
│  ┌─────────────────┐                                                         │
│  │  Service 2:     │                                                         │
│  │  Queue          │  Consumes messages from queue                           │
│  │  Receiver       │  Routes to appropriate services                         │
│  │  Port: 8081     │                                                         │
│  └────┬──────────┬─┘                                                         │
│       │          │                                                            │
│       │          │ HTTP/REST                                                 │
│       │          └─────────────────────┐                                     │
│       │                                │                                     │
│       │                                ↓                                     │
│       │                       ┌─────────────────┐                            │
│       │                       │  Service 3:     │                            │
│       │                       │  Sender         │  Sends email notifications │
│       │                       │  Port: 8082     │                            │
│       │                       └────────┬────────┘                            │
│       │                                │                                     │
│       │                                │ SMTP                                │
│       │                                ↓                                     │
│       │                       ┌─────────────────┐                            │
│       │                       │  Email Server   │                            │
│       │                       │  (Gmail, etc)   │                            │
│       │                       └─────────────────┘                            │
│       │                                                                       │
│       │ HTTP/REST                                                            │
│       ↓                                                                       │
│  ┌─────────────────┐                                                         │
│  │  Service 4:     │                                                         │
│  │  Database       │  Stores and retrieves user data                         │
│  │  Service        │  CRUD operations                                        │
│  │  Port: 8083     │                                                         │
│  └────────┬────────┘                                                         │
│           │                                                                   │
│           │ SQL                                                               │
│           ↓                                                                   │
│  ┌─────────────────────────────────────────────────────┐                    │
│  │         PostgreSQL Database                         │                    │
│  │         Port: 5432                                  │                    │
│  │  Tables: users, notification_logs, report_logs      │                    │
│  └─────────────────────────────────────────────────────┘                    │
│           │                                                                   │
│           │ SQL Queries                                                      │
│           ↓                                                                   │
│  ┌─────────────────┐                                                         │
│  │  Service 5:     │                                                         │
│  │  Report         │  Generates CSV reports                                  │
│  │  Generator      │  Scheduled & on-demand                                  │
│  │  Port: 8084     │                                                         │
│  └────────┬────────┘                                                         │
│           │                                                                   │
│           │ File System                                                      │
│           ↓                                                                   │
│  ┌─────────────────┐                                                         │
│  │  CSV Files      │                                                         │
│  │  /reports/      │                                                         │
│  └─────────────────┘                                                         │
│                                                                               │
└───────────────────────────────────────────────────────────────────────────────┘
```

## Data Flow Sequence

```
1. USER REGISTRATION FLOW
   ─────────────────────────

   User → Frontend → RabbitMQ → Queue Receiver → Database Service → PostgreSQL
                                       ↓
                                  Sender Service → Email Server → User's Email


2. REPORT GENERATION FLOW
   ───────────────────────

   User/Schedule → Report Generator → Database Service → PostgreSQL
                         ↓
                   CSV File Generation
                         ↓
                   /reports/report_*.csv


3. DATA QUERY FLOW
   ────────────────

   User → Frontend → Database Service → PostgreSQL
                           ↓
                      JSON Response
```

## Service Communication Matrix

```
┌──────────────────┬──────────┬───────────┬────────┬──────────┬────────────┐
│ Service          │ Frontend │ Queue Rcv │ Sender │ Database │ Report Gen │
├──────────────────┼──────────┼───────────┼────────┼──────────┼────────────┤
│ Frontend         │    -     │     -     │   -    │    -     │     -      │
│ Queue Receiver   │    -     │     -     │  REST  │   REST   │     -      │
│ Sender           │    -     │     -     │   -    │    -     │     -      │
│ Database Service │    -     │     -     │   -    │    -     │     -      │
│ Report Generator │    -     │     -     │   -    │   REST   │     -      │
└──────────────────┴──────────┴───────────┴────────┴──────────┴────────────┘

Legend:
  REST = HTTP/REST API communication
  -    = No direct communication
```

## Technology Stack Visualization

```
┌─────────────────────────────────────────────────────────────────┐
│                        FRONTEND LAYER                           │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  React.js / Vue.js / Angular + HTML/CSS/JavaScript      │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                     MESSAGE QUEUE LAYER                         │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  RabbitMQ (AMQP Protocol)                                │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    BUSINESS LOGIC LAYER                         │
│  ┌────────────────┬─────────────┬──────────────────────────┐   │
│  │ Queue Receiver │   Sender    │    Report Generator      │   │
│  │  (Node.js/     │ (Node.js/   │     (Node.js/           │   │
│  │   Java/Python) │  Java/      │      Java/Python)       │   │
│  │                │  Python)    │                         │   │
│  └────────────────┴─────────────┴──────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                     DATA ACCESS LAYER                           │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Database Service (Node.js/Java/Python)                  │  │
│  │  ORM: Sequelize / Hibernate / SQLAlchemy                 │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      PERSISTENCE LAYER                          │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  PostgreSQL Database                                      │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Deployment Architecture

```
┌───────────────────────────────────────────────────────────────────────┐
│                         DOCKER HOST                                   │
│                                                                       │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐  │
│  │   Frontend       │  │  Queue Receiver  │  │    Sender        │  │
│  │   Container      │  │   Container      │  │   Container      │  │
│  │   Port: 3000     │  │   Port: 8081     │  │   Port: 8082     │  │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘  │
│                                                                       │
│  ┌──────────────────┐  ┌──────────────────┐                         │
│  │  Database        │  │  Report Gen      │                         │
│  │  Service         │  │  Container       │                         │
│  │  Container       │  │  Port: 8084      │                         │
│  │  Port: 8083      │  └──────────────────┘                         │
│  └──────────────────┘                                                │
│                                                                       │
│  ┌──────────────────┐  ┌──────────────────┐                         │
│  │   RabbitMQ       │  │   PostgreSQL     │                         │
│  │   Container      │  │   Container      │                         │
│  │   Port: 5672     │  │   Port: 5432     │                         │
│  │   Mgmt: 15672    │  └──────────────────┘                         │
│  └──────────────────┘                                                │
│                                                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │              Docker Network: userstream-network             │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Docker Volumes: rabbitmq_data, postgres_data, reports_data │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                       │
└───────────────────────────────────────────────────────────────────────┘
```

## Port Mapping

```
External Port → Container Port → Service

3000  → 3000  → Frontend Service
8081  → 8081  → Queue Receiver Service
8082  → 8082  → Sender Service
8083  → 8083  → Database Service
8084  → 8084  → Report Generator Service
5672  → 5672  → RabbitMQ (AMQP)
15672 → 15672 → RabbitMQ Management UI
5432  → 5432  → PostgreSQL Database
```

## Network Diagram

```
                    Internet
                       │
                       │
              ┌────────┴────────┐
              │   Load Balancer  │ (Future)
              └────────┬────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ↓              ↓              ↓
   ┌─────────┐   ┌─────────┐   ┌─────────┐
   │Frontend │   │Frontend │   │Frontend │
   │Instance1│   │Instance2│   │Instance3│
   └─────────┘   └─────────┘   └─────────┘
        │              │              │
        └──────────────┼──────────────┘
                       │
                   RabbitMQ
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ↓              ↓              ↓
   ┌─────────┐   ┌─────────┐   ┌─────────┐
   │ Queue   │   │ Queue   │   │ Queue   │
   │Receiver1│   │Receiver2│   │Receiver3│
   └─────────┘   └─────────┘   └─────────┘
        │              │              │
        └──────────────┼──────────────┘
                       │
            ┌──────────┼──────────┐
            │          │          │
            ↓          ↓          ↓
       Database    Sender    Report Gen
       Service     Service    Service
            │
            ↓
        PostgreSQL
```

## Security Layers

```
┌─────────────────────────────────────────────────┐
│  Layer 1: Network Security                      │
│  - Docker Network Isolation                     │
│  - Firewall Rules                               │
└─────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────┐
│  Layer 2: Application Security                  │
│  - Input Validation                             │
│  - SQL Injection Prevention                     │
│  - XSS Protection                               │
└─────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────┐
│  Layer 3: Authentication & Authorization        │
│  - JWT Tokens (Future)                          │
│  - API Keys                                     │
│  - Service-to-Service Auth                     │
└─────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────┐
│  Layer 4: Data Security                         │
│  - Encryption at Rest                           │
│  - TLS/SSL in Transit                           │
│  - Secret Management                            │
└─────────────────────────────────────────────────┘
```

---

**Note:** This is a logical architecture diagram. The actual implementation details may vary based on technology choices and deployment environment.
