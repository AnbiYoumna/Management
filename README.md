# Employee Records Management System

## Overview
This project is designed to manage employee records with features like role-based access with JWT Bearer, audit trails, and reporting capabilities.

## Structure
This project contains employees, users (based on the role HR, ADMIN, MANAGER), audit Trails.

## Features
- CRUD operations for employee records.
- Role-based access:
  - HR Personnel: CRUD operations.
  - Managers: Limited updates within their department.
  - Administrators: Full access.
- Audit trail for change logging.
- Search and filtering options.
- Validation rules (e.g., unique Employee ID, valid email).

## Technologies Used
- **Backend:** Java 17, Spring Boot, Oracle SQL
- **Frontend:** Swing (MigLayout, GridBagLayout)
- **Security:** Spring Security with JWT Bearer for RBAC
- **Testing:** JUnit, Mockito, Postman
- **Container:** Docker


