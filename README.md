# REST API for an E-Commerce Application
I have developed this REST API for an e-commerce application using Spring Boot. This API performs all the fundamental CRUD operations of any e-commerce platform with user validation at every step.

# E-R Diagram for the application
<div align="center">
  <img src="src/main/java/com/lcwd/electronic/store/pictures/E-R electronic store.png" width="800"/>
  <p></p>
</div>

# Tech Stack
- Java
- Spring Framework
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL

# Modules
- User Module 
- Product Module
- Cart Module
- Order Module
- Category Module

# API Endpoints
## Root Module
- http://localhost:9091

## User Module

| HTTP Method | Endpoint                  | Description |
|------------|--------------------------|-------------|
| **POST**   | `/users/save`              | Create a new user |
| **PUT**    | `/users/update/{userId}`   | Update user details |
| **DELETE** | `/users/delete/{userId}`   | Delete a user |
| **GET**    | `/users`                   | Get all users (with pagination & sorting) |
| **GET**    | `/users/{userId}`          | Get a single user by ID |
| **GET**    | `/users/mail/{email}`      | Get a user by email |
| **GET**    | `/users/phone/{phoneNo}`   | Get a user by phone number |
| **GET**    | `/users/search/{name}`     | Search users by name (with pagination & sorting) |

## User Image APIs  

| HTTP Method | Endpoint                    | Description |
|------------|----------------------------|-------------|
| **POST**   | `/users/image/{userID}`      | Upload user profile image |
| **GET**    | `/users/image/{userID}`      | Serve user profile image |

