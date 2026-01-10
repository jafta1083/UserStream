# UserStream API Endpoints

## Service Overview
All services now have complete CRUD endpoints with in-memory database storage and CSV generation capabilities.

---

## 1. WebService (Port 7000)
**Purpose:** Accepts data from web requests

### Endpoints:
- `GET /requests` - Get all requests
- `GET /requests/{id}` - Get request by ID  
- `POST /requests` - Create new request
- `DELETE /requests/{id}` - Delete request

### Request Model:
```json
{
  "id": "string",
  "type": "string",
  "data": "string",
  "timestamp": "long"
}
```

---

## 2. UserService (Port 7001)
**Purpose:** Saves user data to database

### Endpoints:
- `GET /users` - Get all users
- `GET /users/{id}` - Get user by ID
- `POST /users` - Create new user
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

### User Model:
```json
{
  "id": "string",
  "name": "string",
  "surname": "string",
  "email": "string",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime",
  "active": "boolean"
}
```

---

## 3. EventService (Port 7002)
**Purpose:** Receives events from queue

### Endpoints:
- `GET /events` - Get all events
- `GET /events/{id}` - Get event by ID
- `POST /events` - Create new event

### Event Model:
```json
{
  "id": "string",
  "type": "string",
  "data": "string",
  "timestamp": "long"
}
```

---

## 4. ReportService (Port 7003)
**Purpose:** Creates CSV reports and stores data in database

### Endpoints:
- `GET /reports` - Get all reports
- `GET /reports/{id}` - Get report by ID
- `POST /reports` - Create new report
- `POST /reports/users` - Store user data for reporting
- `GET /reports/users` - Get all user reports
- `GET /reports/users/csv` - Download user reports as CSV file
- `POST /reports/generate-csv` - Generate custom CSV from array data

### UserReport Model:
```json
{
  "id": "string",
  "name": "string",
  "surname": "string",
  "email": "string",
  "createdAt": "LocalDateTime"
}
```

### CSV Output Format:
```
ID,Name,Surname,Email,Created At
123,John,Doe,john@example.com,2026-01-08T...
456,Jane,Smith,jane@example.com,2026-01-08T...
```

---

## 5. NotificationService (Port 7004)
**Purpose:** Sends notification messages

### Endpoints:
- `GET /notifications` - Get all notifications
- `GET /notifications/{id}` - Get notification by ID
- `POST /notifications` - Create new notification
- `POST /notifications/{id}/send` - Send notification

### Notification Model:
```json
{
  "id": "string",
  "userId": "string",
  "title": "string",
  "content": "string",
  "channel": "string",
  "status": "string",
  "createdAt": "LocalDateTime",
  "sentAt": "LocalDateTime"
}
```

---

## Complete Workflow Example

### 1. Store User Data (UserService - Port 7001)
```bash
POST http://localhost:7001/users
Content-Type: application/json

{
  "id": "u123",
  "name": "John",
  "surname": "Doe",
  "email": "john@example.com"
}
```

### 2. Send User Data to Reports (ReportService - Port 7003)
```bash
POST http://localhost:7003/reports/users
Content-Type: application/json

{
  "id": "u123",
  "name": "John",
  "surname": "Doe",
  "email": "john@example.com"
}
```

### 3. Generate CSV Report (ReportService - Port 7003)
```bash
GET http://localhost:7003/reports/users/csv
```

This will download a CSV file with all stored user data:
```
ID,Name,Surname,Email,Created At
u123,John,Doe,john@example.com,2026-01-08T16:00:00
```

---

## Database Storage
All services use in-memory ConcurrentHashMap for thread-safe data storage:
- **WebService**: Stores web requests
- **UserService**: Stores user data
- **EventService**: Stores events
- **ReportService**: Stores reports and user report data
- **NotificationService**: Stores notifications

Data persists while the service is running and is cleared on restart.
