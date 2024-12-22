# MonkMaze API Documentation

This is the API documentation for the MonkMaze project, which includes various functionalities such as user signup, login, product management, and web socket integration for real-time updates.

## Endpoints

### 1. **SignUp**
- **Method**: POST
- **URL**: `localhost:8080/api/signup`
- **Body**:
  ```json
  {
    "name": "John Doe",
    "email": "johndoe@example.com",
    "password": "abc"
  }
  ```
- **Description**: Registers a new user by providing a name, email, and password.

---

### 2. **SignIn**
- **Method**: POST
- **URL**: `localhost:8080/api/signin`
- **Body**:
  ```json
  {
    "email": "johndoe@example.com",
    "password": "abc"
  }
  ```
- **Description**: Authenticates the user and returns an authentication token.

---

### 3. **Refresh Token**
- **Method**: POST
- **URL**: `localhost:8080/api/refresh_token`
- **Body**:
  ```json
  {
    "refresh_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0ODU4MTM1LCJleHAiOjE3MzQ4NzYxMzV9.A4h9Crpy17jlRAUwXcBDyuHiLTjT7AUDf2Hn-CT3xcE"
  }
  ```
- **Description**: Refreshes the access token using a valid refresh token.

---

### 4. **Create Product**
- **Method**: POST
- **URL**: `localhost:8080/api/products`
- **Authorization**: Bearer Token
- **Body**:
  ```json
  {
    "name": "Product 2",
    "price": 100,
    "description": "description"
  }
  ```
- **Description**: Creates a new product with the given name, price, and description.

---

### 5. **Get All Products**
- **Method**: GET
- **URL**: `localhost:8080/api/products`
- **Authorization**: Bearer Token
- **Description**: Retrieves a list of all products.

---

### 6. **Buy Product**
- **Method**: POST
- **URL**: `localhost:8080/api/buy/1`
- **Authorization**: Bearer Token
- **Description**: Buys the product with ID `1`.

---

## Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/Vivekkr0311/MonkMaze.git
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Access the API at `localhost:8080`.

---

## Notes

- Use the Bearer token for authenticated endpoints like `Create Product`, `Get All Products`, and `Buy Product`.

---
