# Cargos Transportation Service
###

#### ***Dear Users,***

Thank you for your interest in my microservices project.
I am constantly working to improve the system, so there may be some temporary
issues in the implementation. If you encounter anything that is not working,
please feel free to contact me directly. Your feedback is very valuable and will help
make the project even better.

#### Thank you for your understanding and support!

## Content

1. [Introduction](#introduction)
2. [Delivery Service](#delivery-service)
   - [Introduction](#introduction-1)
   - [Vehicle Controller Endpoints](#vehicle-controller-endpoints)
   - [Cargo Controller Endpoints](#cargo-controller-endpoints)
3. [Email Notification](#email-notification)
   - [Introduction](#introduction-2)
   - [Email Controller Endpoints](#email-controller-endpoints)
4. [Cargo Movement](#cargo-movement)
   - [Introduction](#introduction-3)
   - [Endpoints Description](#endpoints-description)
5. [Running the Application](#running-the-application)
   - [To Run Using Docker](#to-run-using-docker)
   - [Setup SMTP](#setup-smtp)
   - [How to Run App](#how-to-run-app)
6. [Testing with Postman](#testing-with-postman)
   - [Importing Sample Data as JSON in Postman](#importing-sample-data-as-json-in-postman)

***The Postman collection will be added soon.***

## Introduction

Welcome to the Delivery Service! Project enhances multiple services to help you manage cargo items,
vehicles and cargos transportation efficiently. Whether you're a logistics company, warehouse manager, or transportation provider, this system
provides the tools you need to track and coordinate your cargo and vehicle operations effectively.

The application uses a ***Eureka server*** to register microservices to manage
them. You can access all microservices using the ***API Gateway server*** to make request using same port ***8072***. 
To check the running microservices,
visit the following URL: http://localhost:8072/actuator/gateway/routes.

# Microservices Description

## Delivery Service

### Introduction

***Delivery Service Microservice*** enhances multiple services to help you manage cargo items,
vehicles and cargos transportation efficiently. Whether you're a logistics company, warehouse manager, or transportation provider, this system
provides the tools you need to track and coordinate your cargo and vehicle operations effectively.

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

- [Back to application content](#content)

# Email Notification

### Introduction

This project enhances service by adding centralized and reliable email sending capabilities,
ensuring messages are sent after cargo was created.

***Email Microservice*** handles email notifications, receiving message details asynchronously via Kafka,
storing them in ElasticSearch, and sending them via SMTP using Spring's JavaMailSender. The service
updates the message status in the database based on the success or failure of the email delivery,
recording error messages if needed.

A retry mechanism runs every 5 minutes to reattempt sending emails with an "error" status.

Email notification logic has been integrated into an existing delivery service microservice to send notifications
when specific cargo was created.


### Email Controller Endpoints

| Method | Endpoint           | Description                              |
|--------|--------------------|------------------------------------------| 
| POST   | `/api/emails/send` | Send message to recipient email address. |


#### To receive messages via email, you need to create a Google application password and add it to the `.env` file along with your username (real email).

### Steps to Create a Google Application Password

1. **Go to your Google Account**: Navigate to [Google Account](https://myaccount.google.com/).
2. **Security**: Click on the "Security" tab.
3. **App Passwords**: Under "Signing in to Google," select "App Passwords." You might need to sign in again.
4. **Select App and Device**: Choose the app and device you want to generate the app password for.
5. **Generate**: Click on "Generate."
6. **Copy the Password**: A 16-character password will appear. Copy this password (do not include any spaces).

### Add to .env File

Add the following lines to your `.env` file, replacing `your_email@example.com` with your actual email and `your_generated_password` with the generated application password:

```env
SPRING_MAIL_USERNAME=email@gmail.com (your real email)
SPRING_MAIL_PASSWORD=email-password (application password)

TO_EMAIL=to-email@gmail.com (email where you want send message)
```
- [Back to application content](#content)


# Cargo Movement

### Introduction
This Node.js service provides a REST API for managing ***CargoMovement***,
which relates to ***Cargo*** entity from ***Delivery Service Microservice***
in a many-to-one relationship.

### Endpoints Description

### 1. POST /api/cargo-movement

Creates a new record for ***CargoMovement***.

- Validates mandatory fields.
- Automatically set movement date if absent in request body.
- The JSON object request contain the ID of ***Cargo***.
- Validates if the corresponding ***Cargo*** object exists by querying the ***Delivery Service API***.

**Request Body Example:**
```json
{
  "cargoId": 786,
  "movementDate": "2024-05-02",
  "fromLocation": "Warehouse A",
  "toLocation": "Warehouse B"
}
```
**Response Body Example:**
```json
{
  "_id": "664e4174d300b472c74bccf2",
  "cargoId": "786",
  "movementDate": "2024-05-02T00:00:00.000Z",
  "fromLocation": "Warehouse A",
  "toLocation": "Warehouse B"
}
```

### 2. GET /api/cargo-movement

Returns a list of ***CargoMovement*** objects related to one record
of ***Cargo***, sorted by descending order.

**Query Parameters:**
- `cargoId`: The ID of Cargo entity.
- `size`: Maximum number of objects to be returned in the response.
- `from`: Number of the element from which the selection will begin.
  **Request URL Example:**
```
http://localhost:8081/api/cargo-movement?cargoId=1022&size=2&from=0
```
**Response Body Example:**
```json
[
  {
    "_id": "664a00b0713511715e946ba7",
    "cargoId": "1022",
    "movementDate": "2023-05-17T00:00:00.000Z",
    "fromLocation": "Warehouse A",
    "toLocation": "Warehouse B"
  },
  {
    "_id": "664a00c6713511715e946ba9",
    "cargoId": "1022",
    "movementDate": "2023-05-17T00:00:00.000Z",
    "fromLocation": "Warehouse A",
    "toLocation": "Warehouse B"
  }
]
```

### 3. POST /api/cargo-movement/_counts

Accepts an array of ***Cargo*** IDs and returns the total count of
***CargoMovement*** items related to each ***Cargo*** item.

**Request Body Example:**
```json
{
  "cargosIds": ["1022", "172", "137", "135", "110", "222"]
}

```
**Response Body Example:**
```json
{
    "110": 1,
    "135": 1,
    "137": 2,
    "172": 1,
    "222": 1,
    "1022": 5
}
```
- [Back to application content](#content)


## Running the Application

In this section, you will find out how the process of running application.

####

### To run using Docker:


***Build application*** build each microservice before creating docker images.
- ***Use command:***
```bash 
mvn clean package
```

***Environment Variables:*** Create the **.env** file in the project root directory with the necessary environment variables. These variables should include your database connection details and any secret keys required by the application.

Use the `.env.sample` file from the application root directory as a sample data to connect with the Docker container using your custom properties.

- [Back to application content](#content)

## Setup SMTP

To receive messages via email, you need to create a Google application password and add it to the `.env` file along with your username (real email).

### Steps to Create a Google Application Password

1. **Go to your Google Account**: Navigate to [Google Account](https://myaccount.google.com/).
2. **Security**: Click on the "Security" tab.
3. **App Passwords**: Under "Signing in to Google," select "App Passwords." You might need to sign in again.
4. **Select App and Device**: Choose the app and device you want to generate the app password for.
5. **Generate**: Click on "Generate."
6. **Copy the Password**: A 16-character password will appear. Copy this password (do not include any spaces).

### Add to .env File

Add the following lines to your `.env` file, replacing `your_email@example.com` with your actual email and `your_generated_password` with the generated application password:

```env
SPRING_MAIL_USERNAME=email@gmail.com (your real email)
SPRING_MAIL_PASSWORD=password (application password)

TO_EMAIL=to-email@gmail.com (email where you want send message)
```
- [Back to application content](#content)

####

# How to run App

### Ensure that you have Docker installed on your system.

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

***Or just run this command (make build of each microservices if necessary - mvn clean package)***

```bash
docker-compose up
```

***and images and container generates automatically***
P.S. If the application can't send a request to save cargo after running the docker-compose up
command, please wait about 3-5 minutes. This happens because some microservices need time to connect to
Kafka, or the gateway server needs time to retrieve information about running microservice
instances from the Eureka server.
####

- [Back to application content](#content)

## Access Using API Gateway Server

After the containers are up and running, you can access your microservices application at the specified endpoints.

### Testing with Postman

If the application is running, use Postman to access the microservices with the following ports, or just import
the Postman collection from the application root directory:

- ***[Java]*** Delivery Service (Cargos Controller): `http://localhost:8072/delivery-service/api/cargos/**`
- ***[Java]*** Delivery Service (Vehicles Controller): `http://localhost:8072/delivery-service/api/vehicles/**`
- ***[Node.js]*** Cargo Movements Service: `http://localhost:8072/cargo-movements-service/api/cargo-movement/**`
- ***[Java]*** User Service: `http://localhost:8072/user-service/api/auth/**`

####

### Use Postman library to test

In this section, instructions are provided for importing sample data into application,
which can be particularly useful for testing and development purposes.

#### Importing Sample Data as JSON in Postman

To import the Endpoints Collection into Postman for testing, follow these steps:

1. Open Postman.

2. Click on the "Import" button in the top left corner.

3. In the "Import" dialog, select the "File" tab.

4. Click on the "Upload Files" button and select the "PostmanCollection.json" file from root
   project directory.

5. Click the "Import" button to add the collection and environment variables to Postman.


- [Back to application content](#content)

## ***Enjoy Your Exploring!!!***
