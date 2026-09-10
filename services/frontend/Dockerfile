# Frontend Service Dockerfile (Node.js/React)
# Build stage
FROM node:16-alpine AS build

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies (including dev dependencies for build)
RUN npm ci

# Copy application code
COPY . .

# Build the application
RUN npm run build

# Production stage
FROM node:16-alpine

WORKDIR /app

# Install serve as a local dependency
RUN npm init -y && npm install serve

# Copy built application from build stage
COPY --from=build /app/build ./build

# Expose port
EXPOSE 3000

# Start the application using npx to run local serve
CMD ["npx", "serve", "-s", "build", "-l", "3000"]
