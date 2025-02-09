# simple-bookstore
This repository is a basic back-end using a Spring Boot RESTful API for a Simple Online Bookstore.

# Functionalities
1. Any user can register and login into the application (`src/test/resources/http/user-api.http`). 
2. After registration a user with role `ADMIN` can store the books into the DB (`src/test/resources/http/book-api.http`). 
3. Registered user can view the stored books and submit the books into the cart (`src/test/resources/http/cart-api.http`). 
4. A user with role `ADMIN` can order any registered user's cart with status `SUBMITTED` (`src/test/resources/http/order-api.http`).
5. A user with role `ADMIN` can update the order status with `CANCELLED` or `COMPLETED` (`src/test/resources/http/order-api.http`).

> NOTE: When the application is stopped all data will be erased.

# Authorization
1. User with `ADMIN` role can access all endpoints.
2. Anonymous user can only access `/register`
3. User with `USER` or `ADMIN` role can access `POST /register`, `GET /login`, `GET /books`,`GET /books/**`, `GET /cart` and `GET /order/**`.

# How run the application locally
Run the Spring Boot app `BookStoreApp` by default profile.
The base url will be http://localhost:8080/

# To test the application
Use the `.http` files from the location `src/test/resources/http/`.
The basic user authentication is used to authenticate the application.
Example Request:
```shell
GET http://localhost:8080/books
Authorization: Basic dXNlcjp1c2Vy
```
> NOTE: Where `dXNlcjp1c2Vy` is the base64 encoded value of `user:password`.