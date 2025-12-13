# Queue Receiver Service

## Overview
The Queue Receiver Service is a message queue consumer that processes incoming user registration data.

## Purpose
- Listens to the message queue for incoming user data
- Validates message format and data integrity
- Routes messages to appropriate downstream services
- Handles message acknowledgment and error handling
- Implements retry logic for failed messages

## Technology Stack
- **Runtime:** Node.js / Java / Python
- **Framework:** Express.js / Spring Boot / Flask
- **Message Queue:** RabbitMQ / Apache Kafka
- **Logging:** Winston / Log4j / Python logging

## Features
- Asynchronous message consumption
- Message validation and sanitization
- Dead letter queue for failed messages
- Retry mechanism with exponential backoff
- Health check endpoint
- Metrics and monitoring

## Message Queue Configuration

### Queues
- **Primary Queue:** `user.registration`
- **Dead Letter Queue:** `user.registration.dlq`
- **Retry Queue:** `user.registration.retry`

### Exchange
- **Type:** Topic
- **Name:** `userstream.exchange`
- **Routing Key:** `user.registration.*`

## API Endpoints

### Health Check
```
GET /health
```
Returns service health status

### Metrics
```
GET /metrics
```
Returns Prometheus-formatted metrics

## Configuration

### Environment Variables
```env
QUEUE_URL=amqp://localhost:5672
QUEUE_NAME=user.registration
DATABASE_SERVICE_URL=http://localhost:8083
SENDER_SERVICE_URL=http://localhost:8082
MAX_RETRY_ATTEMPTS=3
RETRY_DELAY_MS=5000
```

## Installation

### Prerequisites
- Node.js (v14+) or Java (11+) or Python (3.8+)
- RabbitMQ or Kafka running

### Setup
```bash
cd services/queue-receiver
npm install  # or mvn install, pip install -r requirements.txt
```

## Running the Service

### Development Mode
```bash
npm start  # or java -jar app.jar, python app.py
```
The service will start at `http://localhost:8081`

### Production Mode
```bash
npm run start:prod
```

## Docker Deployment

### Build Docker Image
```bash
docker build -t userstream-queue-receiver .
```

### Run Docker Container
```bash
docker run -p 8081:8081 userstream-queue-receiver
```

## Message Processing Flow

1. **Receive Message:** Consume message from queue
2. **Validate Message:** Check message format and required fields
3. **Store Data:** Call Database Service to store user data
4. **Trigger Notification:** Call Sender Service to send email
5. **Acknowledge Message:** Confirm successful processing
6. **Error Handling:** Route to DLQ if processing fails after retries

## Message Format

### Expected Message Structure
```json
{
  "name": "John",
  "surname": "Doe",
  "email": "john.doe@example.com",
  "timestamp": "2025-12-13T19:37:00.000Z"
}
```

## Error Handling

### Retry Strategy
- Failed messages are retried up to 3 times
- Exponential backoff between retries: 5s, 10s, 20s
- After max retries, message is moved to DLQ

### Dead Letter Queue
Messages in DLQ can be:
- Manually reviewed and reprocessed
- Logged for analysis
- Archived for auditing

## Logging
All operations are logged with appropriate levels:
- **INFO:** Successful message processing
- **WARN:** Retry attempts
- **ERROR:** Failed processing, validation errors
- **DEBUG:** Detailed message content (development only)

## Monitoring

### Key Metrics
- Messages consumed per second
- Processing time per message
- Success/failure rate
- Queue depth
- DLQ message count

## Testing
```bash
npm test
```

### Integration Tests
Tests cover:
- Message consumption
- Data validation
- Service integration
- Error handling
- Retry logic

## File Structure
```
queue-receiver/
├── src/
│   ├── config/
│   │   └── queue.js
│   ├── services/
│   │   ├── queueConsumer.js
│   │   ├── databaseClient.js
│   │   └── senderClient.js
│   ├── utils/
│   │   ├── validator.js
│   │   └── logger.js
│   └── index.js
├── tests/
│   └── queueConsumer.test.js
├── package.json
├── Dockerfile
└── README.md
```

## Contributing
Please follow the coding standards and submit pull requests for any improvements.

## License
See the LICENSE file in the root directory.
