# Automated Invoice Generator (Full Stack)

Chosen topic from the provided list: **33. Automated Invoice Generator**.

## Stack
- **Backend**: Spring Boot (Java 17), Spring Web, Spring Data JPA, H2 (default) / MySQL (profile), OpenPDF for PDF generation.
- **Frontend**: Angular (minimal, standalone components) built with Vite for simplicity.

## Quick Start
1. Backend
   ```bash
   cd invoice-service
   mvn spring-boot:run
   ```
   API available at `http://localhost:8080/api`.

2. Frontend
   ```bash
   cd invoice-ui
   npm install
   npm start
   ```
   UI at `http://localhost:5173`.

## What you can do
- Create clients
- Create invoices with items and tax
- List invoices
- Download professional PDF for each invoice

## MySQL (optional)
Update `invoice-service/src/main/resources/application-mysql.properties` and run:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```
