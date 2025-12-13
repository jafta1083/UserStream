# Sender Service

## Overview
The Sender Service handles all notification and communication tasks, primarily sending email notifications to registered users.

## Purpose
- Sends email notifications to registered users
- Sends confirmation messages after registration
- Manages notification templates
- Tracks notification delivery status
- Handles notification failures and retries

## Technology Stack
- **Runtime:** Node.js / Java / Python
- **Framework:** Express.js / Spring Boot / Flask
- **Email Library:** Nodemailer / JavaMail / smtplib
- **Template Engine:** Handlebars / Thymeleaf / Jinja2
- **Logging:** Winston / Log4j / Python logging

## Features
- Email template management
- HTML and plain text email support
- Attachment support
- Bulk email sending
- Delivery status tracking
- Retry mechanism for failed deliveries
- Email queue management

## API Endpoints

### Send Email Notification
```
POST /api/notifications/send
Content-Type: application/json

{
  "to": "user@example.com",
  "subject": "Welcome to UserStream",
  "template": "welcome",
  "data": {
    "name": "John",
    "surname": "Doe"
  }
}
```

### Get Notification Status
```
GET /api/notifications/:id
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
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_SECURE=false
SMTP_USER=your-email@example.com
SMTP_PASSWORD=your-password
FROM_EMAIL=noreply@userstream.com
FROM_NAME=UserStream
MAX_RETRY_ATTEMPTS=3
RETRY_DELAY_MS=10000
```

## Installation

### Prerequisites
- Node.js (v14+) or Java (11+) or Python (3.8+)
- SMTP server access (Gmail, SendGrid, etc.)

### Setup
```bash
cd services/sender
npm install  # or mvn install, pip install -r requirements.txt
```

## Running the Service

### Development Mode
```bash
npm start  # or java -jar app.jar, python app.py
```
The service will start at `http://localhost:8082`

### Production Mode
```bash
npm run start:prod
```

## Docker Deployment

### Build Docker Image
```bash
docker build -t userstream-sender .
```

### Run Docker Container
```bash
docker run -p 8082:8082 userstream-sender
```

## Email Templates

### Welcome Email Template
```html
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to UserStream</title>
</head>
<body>
    <h1>Welcome {{name}} {{surname}}!</h1>
    <p>Thank you for registering with UserStream.</p>
    <p>Your email {{email}} has been successfully registered.</p>
</body>
</html>
```

### Template Variables
- `{{name}}` - User's first name
- `{{surname}}` - User's last name
- `{{email}}` - User's email address
- `{{date}}` - Registration date

## Notification Flow

1. **Receive Request:** API call or message queue event
2. **Load Template:** Fetch appropriate email template
3. **Render Email:** Populate template with user data
4. **Send Email:** Connect to SMTP server and send
5. **Log Status:** Record delivery status
6. **Handle Errors:** Retry on failure or log error

## Error Handling

### Retry Strategy
- Failed emails are retried up to 3 times
- Exponential backoff: 10s, 20s, 40s
- Permanent failures are logged and reported

### Common Errors
- Invalid email address
- SMTP connection failure
- Authentication error
- Rate limiting

## Logging
All email operations are logged:
- **INFO:** Successful email delivery
- **WARN:** Retry attempts
- **ERROR:** Failed delivery, SMTP errors
- **DEBUG:** Email content (development only)

## Monitoring

### Key Metrics
- Emails sent per minute
- Delivery success rate
- Average send time
- Failed deliveries
- Queue depth

## Security

### Best Practices
- Use environment variables for credentials
- Enable TLS/SSL for SMTP connections
- Validate email addresses before sending
- Implement rate limiting
- Sanitize user input in templates

## Testing
```bash
npm test
```

### Test Coverage
- Email sending functionality
- Template rendering
- Error handling
- Retry logic
- API endpoints

## File Structure
```
sender/
├── src/
│   ├── config/
│   │   └── smtp.js
│   ├── services/
│   │   ├── emailService.js
│   │   └── templateService.js
│   ├── templates/
│   │   ├── welcome.html
│   │   └── notification.html
│   ├── utils/
│   │   ├── validator.js
│   │   └── logger.js
│   ├── routes/
│   │   └── notifications.js
│   └── index.js
├── tests/
│   └── emailService.test.js
├── package.json
├── Dockerfile
└── README.md
```

## Email Service Providers

### Recommended Providers
- **Gmail:** Good for development and small-scale
- **SendGrid:** Professional email service with high deliverability
- **Amazon SES:** Cost-effective for high volume
- **Mailgun:** Developer-friendly with good API
- **Postmark:** Excellent deliverability for transactional emails

## Contributing
Please follow the coding standards and submit pull requests for any improvements.

## License
See the LICENSE file in the root directory.
