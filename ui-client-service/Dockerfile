# Use official Node.js image as the base image
FROM node:20.10.0-slim

# Set working directory
WORKDIR /usr/src/app

# Install dependencies
# Copy package files
COPY package.json .
COPY package-lock.json .

RUN npm install

# Copy the entire application
COPY . .

# Build the application
RUN npm run build

# Expose port 3000
EXPOSE 3000

# Command to run the application
CMD ["npm", "start"]
