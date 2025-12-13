# Report Generator Service

## Overview
The Report Generator Service is responsible for creating CSV reports from user registration data stored in the database.

## Purpose
- Retrieves user data from the Database Service
- Generates CSV reports with user information
- Creates formatted export files
- Manages report scheduling and delivery
- Maintains report history and logs
- Provides download capabilities for generated reports

## Technology Stack
- **Runtime:** Node.js / Java / Python
- **Framework:** Express.js / Spring Boot / Flask
- **CSV Library:** csv-writer / Apache Commons CSV / Python csv
- **Scheduler:** node-cron / Quartz / APScheduler
- **Logging:** Winston / Log4j / Python logging

## Features
- On-demand CSV report generation
- Scheduled report generation
- Customizable report formats
- Date range filtering
- Pagination for large datasets
- Report history tracking
- File compression (zip)
- Direct download API

## API Endpoints

### Generate CSV Report
```
POST /api/reports/generate
Content-Type: application/json

{
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "includeColumns": ["name", "surname", "email", "created_at"],
  "format": "csv"
}

Response:
{
  "reportId": "report_20251213_193700",
  "status": "generating",
  "message": "Report generation started"
}
```

### Check Report Status
```
GET /api/reports/:reportId/status

Response:
{
  "reportId": "report_20251213_193700",
  "status": "completed",
  "recordCount": 1523,
  "filePath": "/reports/report_20251213_193700.csv",
  "downloadUrl": "/api/reports/report_20251213_193700/download"
}
```

### Download Report
```
GET /api/reports/:reportId/download
```
Returns CSV file for download

### List All Reports
```
GET /api/reports?page=1&limit=20

Response:
{
  "reports": [
    {
      "reportId": "report_20251213_193700",
      "generatedAt": "2025-12-13T19:37:00Z",
      "recordCount": 1523,
      "status": "completed"
    }
  ],
  "total": 45,
  "page": 1,
  "limit": 20
}
```

### Schedule Report
```
POST /api/reports/schedule
Content-Type: application/json

{
  "name": "Daily User Report",
  "cronExpression": "0 2 * * *",
  "config": {
    "includeColumns": ["name", "surname", "email"],
    "format": "csv"
  }
}
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
DATABASE_SERVICE_URL=http://localhost:8083
REPORT_OUTPUT_DIR=/reports
CSV_DELIMITER=,
CSV_QUOTE_CHAR="
INCLUDE_HEADER=true
MAX_RECORDS_PER_FILE=50000
ENABLE_COMPRESSION=true
RETENTION_DAYS=30
PORT=8084
```

## Installation

### Prerequisites
- Node.js (v14+) or Java (11+) or Python (3.8+)
- Access to Database Service

### Setup
```bash
cd services/report-generator
npm install  # or mvn install, pip install -r requirements.txt
```

### Create Reports Directory
```bash
mkdir -p /reports
chmod 755 /reports
```

## Running the Service

### Development Mode
```bash
npm start  # or java -jar app.jar, python app.py
```
The service will start at `http://localhost:8084`

### Production Mode
```bash
npm run start:prod
```

## Docker Deployment

### Build Docker Image
```bash
docker build -t userstream-report-generator .
```

### Run Docker Container
```bash
docker run -p 8084:8084 -v /reports:/reports userstream-report-generator
```

## CSV Report Format

### Standard User Report
```csv
ID,Name,Surname,Email,Created At,Updated At
1,John,Doe,john.doe@example.com,2025-12-13 19:37:00,2025-12-13 19:37:00
2,Jane,Smith,jane.smith@example.com,2025-12-13 19:38:00,2025-12-13 19:38:00
3,Bob,Johnson,bob.johnson@example.com,2025-12-13 19:39:00,2025-12-13 19:39:00
```

### Customizable Columns
- `id` - User ID
- `name` - First name
- `surname` - Last name
- `email` - Email address
- `created_at` - Registration timestamp
- `updated_at` - Last update timestamp

## Report Generation Flow

1. **Receive Request:** API call or scheduled job
2. **Validate Parameters:** Check date range and filters
3. **Fetch Data:** Query Database Service for user data
4. **Generate CSV:** Create CSV file with data
5. **Store File:** Save file to reports directory
6. **Log Report:** Record report metadata in database
7. **Return Response:** Provide download link

## Scheduled Reports

### Cron Expression Examples
```
0 2 * * *        # Daily at 2 AM
0 2 * * 1        # Every Monday at 2 AM
0 0 1 * *        # First day of every month at midnight
0 */6 * * *      # Every 6 hours
```

## File Management

### Report Naming Convention
```
report_YYYYMMDD_HHMMSS.csv
report_20251213_193700.csv
```

### File Retention
- Reports older than 30 days are automatically deleted
- Can be configured via `RETENTION_DAYS` environment variable
- Archived reports can be stored in S3 or similar

### File Size Limits
- Max 50,000 records per file
- Large datasets split into multiple files
- Files compressed if enabled

## Performance Optimization

### Batch Processing
- Fetch data in batches of 1,000 records
- Stream data to CSV to reduce memory usage
- Use cursor-based pagination

### Caching
- Cache report metadata for quick retrieval
- Cache frequently accessed reports
- Invalidate cache after 1 hour

### Asynchronous Processing
- Long-running reports processed in background
- Status endpoint for progress tracking
- Webhook notification on completion

## Monitoring

### Key Metrics
- Reports generated per day
- Average generation time
- File size distribution
- Storage usage
- Error rate

### Alerts
- Report generation failure
- Storage threshold exceeded (>80%)
- Generation time exceeds threshold (>5 minutes)

## Error Handling

### Common Errors
- Invalid date range
- Database service unavailable
- Disk space exhausted
- Data fetch timeout

### Error Response
```json
{
  "error": "Report generation failed",
  "code": "GENERATION_ERROR",
  "details": "Database service unavailable",
  "timestamp": "2025-12-13T19:37:00Z"
}
```

## Logging
All operations are logged:
- **INFO:** Report generation started/completed
- **WARN:** Large dataset, slow generation
- **ERROR:** Generation failure, service errors
- **DEBUG:** Detailed metrics (development only)

## Testing
```bash
npm test
```

### Test Coverage
- CSV generation
- Data fetching
- File operations
- Scheduled jobs
- API endpoints
- Error scenarios

## File Structure
```
report-generator/
├── src/
│   ├── config/
│   │   ├── database.js
│   │   └── scheduler.js
│   ├── services/
│   │   ├── reportService.js
│   │   ├── csvGenerator.js
│   │   └── databaseClient.js
│   ├── routes/
│   │   └── reports.js
│   ├── utils/
│   │   ├── fileManager.js
│   │   └── logger.js
│   ├── jobs/
│   │   └── scheduledReports.js
│   └── index.js
├── reports/
│   └── .gitkeep
├── tests/
│   └── reportService.test.js
├── package.json
├── Dockerfile
└── README.md
```

## Security

### Best Practices
- Validate all input parameters
- Sanitize file names to prevent path traversal
- Implement rate limiting for report generation
- Restrict file access to authorized users
- Delete sensitive reports after retention period

## Advanced Features

### Future Enhancements
- Support for Excel (XLSX) format
- PDF report generation
- Email delivery of reports
- Report templates
- Data visualization (charts/graphs)
- Real-time streaming reports
- Report versioning

## Contributing
Please follow the coding standards and submit pull requests for any improvements.

## License
See the LICENSE file in the root directory.
