# Invoice Service (Spring Boot)

## Run (H2 default)
```bash
mvn spring-boot:run
```
Open API:
- `GET http://localhost:8080/api/clients`
- `POST http://localhost:8080/api/clients`
- `GET http://localhost:8080/api/invoices`
- `POST http://localhost:8080/api/invoices`
- `GET http://localhost:8080/api/invoices/{id}/pdf`

### Example `POST /api/invoices` body
```json
{
  "clientId": 1,
  "dueDate": "2025-09-01",
  "taxPercent": 18,
  "items": [
    {"description":"Design work","quantity":10,"unitPrice":1200},
    {"description":"Hosting (1 mo)","quantity":1,"unitPrice":800}
  ]
}
```

## Switch to MySQL
Run with profile and ensure DB exists:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```
Configure credentials in `src/main/resources/application-mysql.properties`.
