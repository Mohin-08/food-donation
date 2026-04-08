# Food Donation System - Fresh Machine Setup Guide

This guide explains how to run the complete project on a new computer after extracting the folders from a ZIP file.

## Project Folders

- Backend: `c:\workspace\project1`
- Frontend: `c:\Users\mdmoh\OneDrive\Desktop\food-don\food-expresso`

If you move the project to a different location, keep both folders together and update the commands below with your new paths.

## What You Need Installed

1. Java JDK 21
2. Node.js 18 or newer
3. MySQL 8 or newer
4. A web browser

The backend already includes the Maven Wrapper, so you do not need to install Maven separately.

## Backend Setup

1. Open the backend folder:

```powershell
cd c:\workspace\project1
```

2. Create the database in MySQL:

```sql
CREATE DATABASE springdb;
```

3. Open `src/main/resources/application.properties` and verify these settings:

- `server.port=8087`
- `spring.datasource.url=jdbc:mysql://localhost:3306/springdb`
- `spring.datasource.username=<your-mysql-username>`
- `spring.datasource.password=<your-mysql-password>`

4. Start the backend:

```powershell
.\mvnw spring-boot:run
```

5. Confirm the backend is running at:

```text
http://localhost:8087
```

## Frontend Setup

1. Open the frontend folder:

```powershell
cd c:\Users\mdmoh\OneDrive\Desktop\food-don\food-expresso
```

2. Install frontend packages:

```powershell
npm install
```

3. Start the frontend:

```powershell
npm run dev
```

4. Open the frontend in the browser:

```text
http://localhost:5173
```

## JWT Login Notes

- The app uses JWT authentication.
- There is no separate session server to install.
- After login, the frontend stores the token in browser storage and sends it in API requests.
- On a new computer or a different browser profile, browser storage starts empty, so users must log in again.

## Port Details

- Backend API: `8087`
- Frontend dev server: `5173`

If you change the backend port, also update the API base URL in `src/services/api.js` on the frontend.

## MySQL Notes

- Database name used by default: `springdb`
- The backend creates and updates tables automatically because JPA is set to `ddl-auto=update`
- Audit logs are stored in the MySQL `audit_log` table

## Run Order

1. Start MySQL
2. Start the backend
3. Start the frontend
4. Open `http://localhost:5173`

## Troubleshooting

### Backend does not start

- Check that MySQL is running
- Verify the database name is `springdb`
- Verify the username and password in `application.properties`
- Make sure Java JDK 21 is installed

### Frontend cannot connect to backend

- Check that the backend is running on port `8087`
- Verify `src/services/api.js` still points to `http://localhost:8087`

### Login does not work

- Confirm the backend has users in the database
- Make sure the role is correct: `ADMIN`, `DONOR`, or `NGO`
- Clear browser storage if an old token is causing issues

## Build Commands

Backend build:

```powershell
cd c:\workspace\project1
.\mvnw -q -DskipTests compile
```

Frontend build:

```powershell
cd c:\Users\mdmoh\OneDrive\Desktop\food-don\food-expresso
npm run build
```

## Short Version

If you are setting this up on a new laptop:

1. Install Java 21, Node.js, and MySQL
2. Create the `springdb` database
3. Update MySQL username/password in `application.properties`
4. Run backend:

```powershell
.\mvnw spring-boot:run
```

5. Run frontend:

```powershell
npm install
npm run dev
```

6. Open `http://localhost:5173`
