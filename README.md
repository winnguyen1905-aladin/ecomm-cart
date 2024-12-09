# Cart Service

## Overview

The Cart Service manages shopping cart operations for the e-commerce platform. It handles product addition/removal, quantity updates, price calculations, promotion applications, and cart persistence. The service maintains both guest carts and user-associated carts with seamless conversion between them.

## Key Features

- **Cart Management**: Create, update, and manage shopping carts
- **Product Operations**: Add, remove, and update products in cart
- **Pricing**: Real-time price calculation with tax and shipping
- **Promotion Integration**: Apply discount codes and promotions
- **Inventory Validation**: Real-time inventory checks
- **Guest Cart Support**: Anonymous shopping with conversion to user cart
- **Cart Persistence**: Redis-based cart storage with PostgreSQL backup
- **Multi-region Support**: Region-specific cart management
- **Wishlist Management**: Save items for later

## Technical Stack

- Spring Boot 3.x
- Spring Data JPA/Redis
- Spring Security with JWT authentication
- Resilience4j for fault tolerance
- Feign clients for service communication
- PostgreSQL/CockroachDB for persistent storage
- Redis for cart caching

## Project Structure

- `/src/main/java/winnguyen1905/cart/`
  - `/config/`: Configuration classes
  - `/core/`: Core business logic
    - `/controller/`: REST controllers
    - `/mapper/`: DTO-Entity mapping
    - `/model/`: Domain models
    - `/service/`: Service interfaces and implementations
  - `/exception/`: Exception handling
  - `/persistance/`: Data access layer
    - `/entity/`: JPA entities
    - `/repository/`: JPA repositories
  - `/secure/`: Security configuration
  - `/service/`: Additional services
  - `/util/`: Utility classes

## API Endpoints

See the [CART_API_DOCUMENTATION.md](./CART_API_DOCUMENTATION.md) for detailed API documentation.

### Cart Operations

- `POST /api/v1/carts`: Create new cart
- `GET /api/v1/carts/{id}`: Get cart by ID
- `GET /api/v1/carts/current`: Get current user's cart
- `DELETE /api/v1/carts/{id}`: Delete cart
- `POST /api/v1/carts/{id}/merge`: Merge guest cart into user cart

### Cart Items

- `POST /api/v1/carts/{id}/items`: Add item to cart
- `PUT /api/v1/carts/{id}/items/{itemId}`: Update cart item
- `DELETE /api/v1/carts/{id}/items/{itemId}`: Remove cart item
- `GET /api/v1/carts/{id}/items`: Get all cart items

### Promotions and Pricing

- `POST /api/v1/carts/{id}/coupons`: Apply coupon
- `DELETE /api/v1/carts/{id}/coupons/{code}`: Remove coupon
- `GET /api/v1/carts/{id}/totals`: Get cart totals

### Checkout Process

- `POST /api/v1/carts/{id}/checkout`: Initiate checkout
- `POST /api/v1/carts/{id}/shipping-methods`: Update shipping method
- `POST /api/v1/carts/{id}/shipping-address`: Update shipping address

## Integration with Other Services

The Cart Service integrates with several other microservices:

- **Product Service**: Product information and inventory validation
- **Promotion Service**: Discount and promotion application
- **User Service**: User authentication and profile information
- **Region Service**: Regional pricing and availability
- **Inventory Service**: Stock availability and reservation

## Security

- JWT-based authentication
- Role-based access control
- Guest cart token management
- Cart ownership validation

## Caching Strategy

- Redis-based cart caching
- TTL configuration for abandoned carts
- Cache invalidation on updates

## Getting Started

### Prerequisites

- Java 21
- Maven 3.8+
- Docker and Docker Compose
- PostgreSQL/CockroachDB
- Redis

### Setup

1. Configure database and Redis in `application.yaml`
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Docker

```bash
# Build Docker image
docker build -t cart-service .

# Run with Docker
docker run -p 8080:8080 cart-service
```

## Documentation

For more detailed information, see:
- [CART_API_DOCUMENTATION.md](./CART_API_DOCUMENTATION.md): Complete API documentation
- Swagger UI: `/swagger-ui.html` (when application is running) 
