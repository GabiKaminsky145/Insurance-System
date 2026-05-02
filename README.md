# Insurance System

Spring Boot REST API for insured client identification and product management.
No database — everything is stored in memory using `ConcurrentHashMap`.

## Requirements

- Java 17+
- Maven 3.8+

## How to run

```bash
mvn spring-boot:run
```

App starts on `http://localhost:8080`

Swagger UI available at `http://localhost:8080/swagger-ui.html`

---

## ERD

```
CLIENT
- id (PK)
- name

CONTACT_METHOD  (embedded in Client — no separate table in this implementation)
- type  (EMAIL / PHONE / MAIL)
- value
- belongs to one CLIENT via clientId FK (added by ORM in a real DB)

PRODUCT
- id (PK)
- name
- description

CLIENT_PRODUCT
- clientId  (FK)
- productId (FK)
- UNIQUE(clientId, productId)
```

Relations:
- One client → many contact methods
- Client ↔ Product is many-to-many
- Each client can own each product only once — enforced by `Set<Product>`

---

## Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| `POST` | `/api/clients/identify` | Create or authenticate a client, returns their product list |
| `POST` | `/api/clients/{clientId}/products/{productId}/buy` | Assign a product to an authenticated client |
| `POST` | `/api/products` | Add a new product to the catalog |
| `PUT`  | `/api/products/{id}` | Update an existing product |
| `GET`  | `/api/products` | List all products |
| `GET`  | `/api/products/{id}` | Get a single product |

---

## What's missing in the original diagrams

Diagram 1 shows: create client → authenticate → get product list

Diagram 2 shows: add product to catalog → update product

Neither diagram shows how a product actually gets linked to a client.
Without this, a new client always gets an empty product list — the two diagrams are disconnected.

The missing flow is the `/buy` endpoint — an authenticated client selects a product
from the catalog and it gets saved to their account.

---

## Postman

Import `Insurance_System_API_postman_collection.json` into Postman.

Run in this order:
1. Create Product
2. Identify NEW Client
3. Assign Product to Client
4. Identify EXISTING Client → should now return the product in the list
5. Duplicate Product Assignment → expect `409 Conflict`
6. Unauthorized Client → expect `401 Unauthorized`

---

## Tests

```bash
mvn test
```

12 unit tests covering: client creation, authentication flows, product purchase, and all error cases.
