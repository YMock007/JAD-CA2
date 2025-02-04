# Cleaning Service Platform Project

## Overview
This project provides the backend functionality for a cleaning service platform. The code is structured into different modules handling various aspects of the application, including user management, services, bookings, and reviews. It is written in *Java* with *JSP* for the frontend and *PostgreSQL* for the database.

---

## Setup Instructions

### 1. Configure the Application
1. Navigate to main.java/Utils/config.properties in the project directory.
2. Update the following configurations:
   - DATABASE_URL: Update with the database connection string.
   - DATABASE_USER: Database username.
   - DATABASE_PASSWORD: Database password.
   - BASE_PATH: Update the base path for your project  until JAD-CA1\(used for file uploads and other resources).

### 2. Initialize the Database
To set up the database schema:
1. Open webapp/db/initTables.jsp in your browser to reset and initialize all tables.
   > *Note:* This will drop all existing tables and recreate them, including cascading dependencies.
2. If needed, populate the database with seed data by running webapp/db/seeder.jsp.

---

## Features

### Database Schema
The database schema includes the following tables:
- *Role:* Stores user roles.
- *Status:* Tracks booking statuses.
- *Category:* Manages service categories.
- *Person:* Holds user information.
- *ApplyToBeCleaner:* Captures applications from potential cleaners.
- *Service:* Stores service details (name, price, description).
- *Booking:* Tracks service bookings, including requester and provider details.
- *Review:* Stores customer reviews for bookings.
- *Saved:* Manages user's saved services.

### Code Structure
#### Backend
- Bookings/: Handles booking logic.
- Cart/: Manages shopping cart functionality.
- Categories/: Manages service categories.
- db/: Database connection and initialization logic.
- handlingPassword/: Password hashing and validation.
- Persons/: User management logic (CRUD operations).
- profile/: Profile-related functionality (update, delete, change password).
- registrationServlets/: Registration and authentication logic.
- Reviews/: Review management.
- Roles/: Role management.
- Saved/: Saved services management.
- Services/: Service management.
- Utils/: Utility classes for configuration and file uploads.

#### Frontend (JSP Files)
- views/admin/dashboard/managing/: Admin dashboard for managing members and services.
- views/member/: Member-related pages (bookings, cart, saved services, etc.).
- views/profile/: User profile pages.
- views/registration/: Registration and login pages.
- views/Util/: Shared components (header, footer, notifications).

---

## Usage Guide

### Reset Database
1. Navigate to webapp/db/initTables.jsp in your browser.
2. This will drop all tables and recreate them as per the provided schema.

### Seed Database
1. Navigate to webapp/db/seeder.jsp in your browser.
2. This will populate the database with initial sample data.

### Key Database Functions
- *Cascade Delete:*  
  All foreign key constraints have ON DELETE CASCADE. Deleting a parent record will automatically remove associated child records.
- *Review Validation Trigger:*  
  Reviews can only be added for bookings with a status of Completed. The trg_validate_review_booking_status trigger ensures this.

---

## Development Notes

### Password Hashing
- Passwords are hashed using the passwordHashing utility.

### Configurations
- Application configurations are managed via config.properties.

### Dependencies
- PostgreSQL JDBC driver is required and included in WEB-INF/lib/.

---

## Deployment
1. Deploy the WAR file to a servlet container such as *Apache Tomcat*.
2. Configure the database connection in config.properties.
3. Initialize and seed the database if required.
4. Access the application via the base URL.

---
S
## Troubleshooting
- Ensure the database credentials and URL in config.properties are correct.
- Check for missing dependencies in WEB-INF/lib/.
- Use TestDatabaseConnection.java to verify database connectivity.

---

This document serves as a comprehensive guide to setting up and running the cleaning service platform. Feel free to reach out for further clarification!
