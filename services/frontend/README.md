# Frontend Service

## Overview
The Frontend Service is the user-facing web application that collects user registration data.

## Purpose
- Provides a user interface for data submission
- Collects user information: name, surname, and email
- Validates user input before submission
- Publishes validated data to the message queue

## Technology Stack
- **Framework:** React.js / Vue.js / Angular
- **UI Library:** Bootstrap / Material-UI / Tailwind CSS
- **State Management:** Redux / Vuex / Context API
- **HTTP Client:** Axios / Fetch API
- **Message Queue Client:** RabbitMQ AMQP client

## Features
- Responsive user registration form
- Client-side validation
- Real-time feedback on submission
- Success/error notifications
- Loading states during submission

## API Endpoints
This service primarily consumes APIs but doesn't expose any REST endpoints.

## Configuration

### Environment Variables
```env
REACT_APP_API_URL=http://localhost:8081
REACT_APP_QUEUE_URL=amqp://localhost:5672
REACT_APP_QUEUE_NAME=user.registration
```

## Installation

### Prerequisites
- Node.js (v14 or higher)
- npm or yarn

### Setup
```bash
cd services/frontend
npm install
```

## Running the Service

### Development Mode
```bash
npm start
```
The application will start at `http://localhost:3000`

### Production Build
```bash
npm run build
```

## Docker Deployment

### Build Docker Image
```bash
docker build -t userstream-frontend .
```

### Run Docker Container
```bash
docker run -p 3000:3000 userstream-frontend
```

## Form Structure

### User Registration Form
```javascript
{
  name: string,      // User's first name (required)
  surname: string,   // User's last name (required)
  email: string      // User's email address (required, valid email format)
}
```

## Validation Rules
- **Name:** Required, 2-50 characters, letters only
- **Surname:** Required, 2-50 characters, letters only
- **Email:** Required, valid email format, max 255 characters

## Message Queue Integration
When a user submits the form:
1. Data is validated on the client side
2. Form data is published to `user.registration` queue
3. Success message is displayed to the user
4. Form is reset for next submission

## Testing
```bash
npm test
```

## File Structure
```
frontend/
├── public/
│   ├── index.html
│   └── favicon.ico
├── src/
│   ├── components/
│   │   ├── UserForm.jsx
│   │   ├── NotificationBanner.jsx
│   │   └── LoadingSpinner.jsx
│   ├── services/
│   │   └── queueService.js
│   ├── utils/
│   │   └── validation.js
│   ├── App.js
│   └── index.js
├── package.json
├── Dockerfile
└── README.md
```

## Contributing
Please follow the coding standards and submit pull requests for any improvements.

## License
See the LICENSE file in the root directory.
