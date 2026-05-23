# GuestHouse Booking System

A web application for managing guest house reservations, built with Spring Boot and Thymeleaf.

## Features
* **Customers:** Register, update, and delete customers.
* **Rooms:** Manage single and double rooms with optional extra beds.
* **Bookings:** Create and manage reservations with automatic double-booking prevention.
* **Search:** Find available rooms by date range and guest capacity.

## Tech Stack
* Java 21
* Spring Boot 4.0.6
* Spring Data JPA & Hibernate
* MySQL
* Thymeleaf & Bootstrap 5

## Architecture
* **Controllers:** Handle HTTP requests and serve views using DTOs.
* **Services:** Manage business logic, validation, and Entity-to-DTO mapping.
* **Repositories:** Execute database operations via Spring Data JPA.

## Setup and Installation

### 1. Clone Repository
```bash
git clone [https://github.com/Karwana/GuestHouse-Booking-System.git](https://github.com/Karwana/GuestHouse-Booking-System.git)
cd GuestHouse-Booking-System
```

### 2. Create Database
Run the following in MySQL Workbench or terminal:
```sql
CREATE DATABASE guesthouse_db;
```

### 3. Configure Local Properties
Create the file `src/main/resources/application-local.properties` and fill in your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/guesthouse_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
```
Or run `GuestHouseBookingSystemApplication.java` directly in IntelliJ.

### 5. Open in Browser
```bash
http://localhost:8080/
```
