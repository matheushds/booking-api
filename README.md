
# Booking Application

## Overview

The Booking Application is designed to manage and facilitate reservations and was built as a technical challenge for a job application. The application allows users to browse and book services, as well as manage their bookings. Administrators can manage blocks and bookings.

## Features

- User Authentication
- Block Creation and Management
- Booking Creation and Management

## Technologies Used

- **Backend:** Java, Spring Boot
- **Database:** in memory database HSQLDB
- **Build Tool:** Maven
- **Version Control:** Git
- **Testing:** JUnit, Mockito

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

## Installation

1. **Clone the repository:**

   ```sh
   git clone https://github.com/matheushds/booking-api.git
   cd booking-api
   ```

2. **Backend Setup:**

    - Build and run the backend:

      ```sh
      mvn clean install
      mvn spring-boot:run
      ```
    - The backend will be accessible at `http://localhost:8080`.
## Usage

## Note: The sistems has two types of users: Admin and User created by default.
### The default admin credentials are: 
 `username: manager, password: password.`
### The default user credentials are: username: 
 `username: common_user, password: password`
### We also have 3 properties created by default:
 `Property 1: id: 0`
 `Property 2: id: 1`
 `Property 3: id: 2`
 ### To access swagger page:
 `localhost:8080/swagger-ui.html`


1. **Access the application:**

   You can use the Collection provided in the `postman` folder to interact with the backend API.

2. **User Operations:**

    - **Login** with your credentials.
    - **Create a Booking**.
    - **Update** your bookings.
    - **Cancel** your bookings.
    - **Rebook** your bookings.
    - **Delete** your bookings.
    - **View** Your bookings by using property id.

3. **Admin Operations:**

    - **Login** with an admin account.
    - **Create** a Block.
    - **Update and delete** your blocks.

## Testing

Run the backend tests using Maven:

```sh
cd backend
mvn test
```

- Matheus Henrique dos Santos - [henriquematheuusg@gmail.com](mailto:)
- GitHub: [matheushds](https://github.com/matheushds)
