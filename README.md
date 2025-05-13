# REST API for an Electronics Store Application
I have developed this REST API for an Electronics Store using Spring Boot. This API performs all the fundamental CRUD operations of any e-commerce platform with user validation at every step.

# E-R Diagram for the application
<div align="center">
  <img src="src/main/java/com/lcwd/electronic/store/pictures/er diagram.png" width="800"/>
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

## Category Module

| HTTP Method | Endpoint                   | Description |
|------------|---------------------------|-------------|
| **POST**   | `/category/save`           | Create a new category |
| **PUT**    | `/category/update/{categoryId}` | Update category details |
| **DELETE** | `/category/delete/{categoryId}` | Delete a category |
| **GET**    | `/category`                 | Get all categories (with pagination & sorting) |
| **GET**    | `/category/{categoryId}`    | Get a single category by ID |

## Category Image APIs  

| HTTP Method | Endpoint                      | Description |
|------------|------------------------------|-------------|
| **POST**   | `/category/image/{categoryId}` | Upload category image |
| **GET**    | `/category/download/{categoryId}` | Serve category image |

## Product Module

| HTTP Method | Endpoint                                  | Description |
|------------|------------------------------------------|-------------|
| **POST**   | `/product/save`                         | Create a new product |
| **PUT**    | `/product/update/{productId}`           | Update product details |
| **DELETE** | `/product/delete/{productId}`           | Delete a product |
| **GET**    | `/product/{productId}`                  | Get a single product by ID |
| **GET**    | `/product`                              | Get all products (with pagination & sorting) |
| **GET**    | `/product/stock`                        | Get all available (in-stock) products |
| **GET**    | `/product/search?name={productName}`    | Search products by name |
| **GET**    | `/product/brand/{brandName}`           | Get products by brand name |
| **GET**    | `/product/range?minPrice={min}&maxPrice={max}` | Get products within a price range |
| **POST**   | `/product/upload/{productId}`          | Upload product image |
| **GET**    | `/product/download/{productId}`        | Download product image |
| **POST**   | `/product/save/category/{categoryId}`  | Create a product under a specific category |
| **PUT**    | `/product/assign/{productId}/category/{categoryId}` | Assign a product to a category |
| **GET**    | `/product/category/{categoryId}`       | Get all products under a specific category |

## Cart Module

| HTTP Method | Endpoint                          | Description |
|------------|----------------------------------|-------------|
| **POST**   | `/cart/save/{userId}`            | Add an item to the cart for a specific user |
| **PUT**    | `/cart/remove/{userId}/{cartItemId}` | Remove an item from the cart |
| **DELETE** | `/cart/clear/{userId}`           | Clear the cart for a specific user |
| **GET**    | `/cart/{userId}`                 | Get the cart details for a specific user |

| HTTP Method | Endpoint                   | Description                                                                                                                                                                              |
| ----------- | -------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **POST**    | `/orders/create`           | Create a new order (Admin only). Takes `CreateOrderRequest` in request body and returns the created order.                                                                               |
| **DELETE**  | `/orders/remove/{orderId}` | Delete an existing order by `orderId` (Admin or Normal user).                                                                                                                            |
| **GET**     | `/orders/users/{userId}`   | Get all orders placed by a specific user (Admin or Normal user).                                                                                                                         |
| **GET**     | `/orders`                  | Get a paginated list of all orders with optional query params `pageNumber`, `pageSize`, `sortBy`, and `sortDir` (Admin or Normal user).                                                  |
| **PUT**     | `/orders/update/{orderId}` | Update the status, payment status, or delivered date of an order by `orderId` (Admin only). Optional query params: `orderStatus`, `paymentStatus`, `deliveredDate` (format: yyyy-MM-dd). |



