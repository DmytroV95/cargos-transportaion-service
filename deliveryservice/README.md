# Delivery Service Application

### Content

1. [Introduction](#introduction)
2. [Endpoints Descriptions](#endpoints-descriptions)
3. [Running the Application](#running-the-application)
4. [Testing with Postman](#testing-with-postman)
5. [Swagger Documentation](#swagger-documentation)

## Introduction

Welcome to the Delivery Service! This application is designed to help you manage cargo items and
vehicles efficiently. Whether you're a logistics company, warehouse manager, or transportation provider, this system
provides the tools you need to track and coordinate your cargo and vehicle operations effectively.

# Endpoints Descriptions

### Vehicle Controller Endpoints

#### (During the application's runtime, the project utilizes Liquibase scripts to manage database schema changes, and it also saves vehicle data to the database.)

| Method | Endpoint             | Description                                     |
|--------|----------------------|-------------------------------------------------|
| GET    | `/api/vehicles`      | Retrieve all vehicles in the system.            |
| POST   | `/api/vehicles`      | Create a new vehicle.                           |
| PUT    | `/api/vehicles/{id}` | Update details of a specific vehicle by its ID. |
| DELETE | `/api/vehicles/{id}` | Delete a specific vehicle by its ID.            |

### Cargo Controller Endpoints

| Method | Endpoint                  | Description                                                                                                                                                                                                                                                                                                         |
|--------|---------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| GET    | `/api/cargos`             | Retrieve all cargo items in the system.                                                                                                                                                                                                                                                                             |
| GET    | `/api/cargos/{id}`        | Retrieve details of a specific cargo item by its ID.                                                                                                                                                                                                                                                                |
| POST   | `/api/cargos`             | Create a new cargo item.                                                                                                                                                                                                                                                                                            |
| PUT    | `/api/cargos/{id}`        | Update details of a specific cargo item by its ID.                                                                                                                                                                                                                                                                  |
| DELETE | `/api/cargos/{id}`        | Delete a specific cargo item by its ID.                                                                                                                                                                                                                                                                             |
| GET    | `/api/cargos/_list`       | Get cargos by searching parameters.                                                                                                                                                                                                                                                                                 |
| GET    | `/api/cargos/file/upload` | Upload JSON file containing cargo data. <br/> You will find the JSON result of the operation in the project's root directory named 'data_processing_response'." <br/> Please copy the sample dataset from 'src/main/resources/json_data_set' to your local machine for testing the upload endpoint."                |                           |                                                                                                                                                                                                                                                                                                     |
| GET    | `/api/cargos/_report`     | Generate an Excel report for all cargo items. <br/> To test the '_report' endpoint, you can copy the URL from the Postman collection dataset (dependent on the port), which you can find in the root of the application, and enter this URL into a browser. Please note that this endpoint may not work in Postman. |

## Running the Application

In this section, you will find out how the process of running application.

- [Back to application content](#content)

####

### To run locally without Docker:

To run the application without Docker, follow these steps:

-

***Run Docker before starting the application. It is necessary because integration tests require running test containers
in Docker.***

####

- ***Clone the project repository from GitHub to your local machine.***
   ```bash
    git clone git@github.com:DmytroV95/delivery-service-app-spring.git
    ```
  or
    ```bash
    git clone https://github.com/DmytroV95/delivery-service-app-spring.git
    ``` 
- ***Configure Application Properties:***
  Navigate to the src/main/resources directory and locate the application.properties file. Update this file with the
  necessary configuration, such as the database connection details and other environment-specific settings.

####

- ***Database Setup:***
  Ensure you have a PostgreSQL database server installed and running. Create a database for your application and
  configure its details in the application.properties file.

####

- ***Build the Application:***
  Open a terminal in the root directory of the project and use Maven to build the application.
    ```bash
    mvn clean package
  ```

####

- ***Run the Application:***
  Once the build is successful, you can run the application using the following command:
    ```bash
    java -jar target/deliveryservice-0.0.1-SNAPSHOT.jar
  ```

####

- ***Access the Application:***
  After the application is up and running, open your web browser and access the application at the specified endpoints.
  By default, it might be available at http://localhost:8080 unless you've configured a different port in the
  application.properties file.

####

####

### To run using Docker:

- ***Environment Variables:*** Create the ***.env*** file in project root directory with the necessary environment
  variables. These variables should include your database connection details and any secret keys required by
  application.
  ***Use .env.sample file from application root directory as a sample data to connection with docker container with your
  custom properties.***

####

- ***Docker Setup:*** Ensure that you have Docker installed on your system.

####

- ***Docker Compose:*** The application is configured to use Docker Compose for orchestrating containers. Make sure you
  have Docker Compose installed as well. You can check if it's installed by running:
    ```bash
    docker-compose --version
    ```

####

- ***Build Docker Image:*** In your project root directory, open a terminal and run the following command to build a
  Docker image of application:
    ```bash
    docker build -t image-name .
    ```

####

- ***Start Docker Containers:*** Once the image is built, you can start your Docker containers using Docker Compose by
  running:
    ```bash
    docker-compose up
    ```
  This command will start the application and any required services (e.g., the database) defined in
  docker-compose.yml file.

***Or just run this command***

```bash
docker-compose up
```

***and images and container generates automatically***

####

- ***Access the Application:*** After the containers are up and running, you can access your Spring Boot application at
  the specified endpoints using Postman.
- If the application is running use next ports:
    - locally: http://localhost:8080/api/<endpoint_name>
    - using Docker (don't forget to change port in the postman collection): http://localhost:8088/api/<endpoint_name>

####

# Testing with Postman

In this section, instructions are provided for importing sample data into application, which can be particularly useful
for testing and development purposes.

- [Back to application content](#content)

### Importing Sample Data as JSON in Postman

To import the Endpoints Collection into Postman for testing, follow these steps:

1. Open Postman.

2. Click on the "Import" button in the top left corner.

3. In the "Import" dialog, select the "File" tab.

4. Click on the "Upload Files" button and select the "Postman_Endpoints_Collection" file from root project
   directory.

- ***(In the root of the application, provided configurations are utilized for local execution
  on port 8080 and for Docker usage on port 8088.)***

5. Click the "Import" button to add the collection and environment variables to Postman.

## Swagger Documentation

In this section, instructions are provided for accessing the Swagger documentation directly from your web browser,
allowing you to explore and interact with the API endpoints easily.

- [Back to application content](#content)

### Follow these steps to access the Swagger documentation and explore API endpoints using a web browser:

- Start the Application
- Launch your preferred web browser (e.g., Chrome, Firefox, or Edge)
- In the browser's address bar, enter the URL for the Swagger documentation
    - If the application is running:
        - locally: http://localhost:8080/api/swagger-ui/index.html
        - using Docker: http://localhost:8088/api/swagger-ui/index.html

## ***Enjoy Your Exploring!!!***