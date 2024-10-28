# Project Quick Start Guide

## Prerequisites
Before running the project, ensure the following software is installed:
- **Java JDK 17** or later
- **Maven** 3.6 or later
- **MySQL** database
- **Git** (for version control)
- **Node.js** (version 14 or later)
- **npm** (comes with Node.js)

## Project Structure
This project consists of two parts:
- **Backend**: Based on Spring Boot, providing API services and database connections.
- **Frontend**: Based on React, handling user interface interactions.

## Steps

### 1. Clone the Project

Use Git to clone the project repository to your local machine.
```bash
git clone <your-project-repo-url>
cd <project-folder-name>
```
### 2. Configure the Database

	1.	Start your MySQL database.
	2.	Create a new database, for example, project_db.
	3.	Configure the database connection details in the application.properties or application.yml file within the project. Example:


### 3.Install Dependencies
```bash
# Navigate to backend directory
cd backend

# Build and start backend server
mvn clean install
mvn spring-boot:run

# Navigate to frontend directory
cd frontend

# Install frontend dependencies
npm install
```
### 4. Start the Frontend Server
```bash
npm start
```
By default, the frontend will be available at http://localhost:3000. Open this URL in your browser to view the frontend interface.

### 5.Backend Build
```bash
# Navigate to backend directory
cd backend

# Package the backend
mvn clean package
```
### 6.Frontend Build
```bash
# Navigate to frontend directory
cd frontend

# Build for production
npm run build
```