# Cart Service API Documentation

## Overview

The Cart Service provides comprehensive RESTful APIs for managing shopping carts in the microservice architecture. It implements proper optimistic locking, validation, and error handling following microservice patterns.

## Base URL
```
http://localhost:8080/api/v1/carts
```

## Authentication
All endpoints require JWT authentication. The customer ID is extracted from the JWT token using the `@AccountRequest` annotation.

## API Endpoints

### Cart Retrieval Operations

#### 1. Get Cart
**GET** `/api/v1/carts`

Retrieve the customer's cart with basic information.

**Query Parameters:**
- `page` (optional): Page number for pagination
- `size` (optional): Number of items per page

**Response:**
```json
{
  "statusCode": 200,
  "message": "Cart retrieved successfully",
  "data": {
    "cartByShops": [
      {
        "shopId": "uuid",
        "cartItems": [
          {
            "price": 29.99,
            "quantity": 2,
            "isSelected": true,
            "productVariantReview": {
              "id": "uuid",
              "name": "Product Name",
              "price": 29.99,
              "imageUrl": "https://...",
              "stock": 100
            }
          }
        ],
        "priceStatistic": {
          "totalPrice": 59.98,
          "finalPrice": 59.98
        }
      }
    ]
  }
}
```

#### 2. Get Enhanced Cart
**GET** `/api/v1/carts/enhanced`

Retrieve the customer's cart with detailed information including timestamps and enhanced product details.

**Response:**
```json
{
  "statusCode": 200,
  "message": "Enhanced cart retrieved successfully",
  "data": {
    "cartId": "uuid",
    "customerId": "uuid",
    "cartByShops": [...],
    "summary": {
      "totalItems": 5,
      "selectedItems": 3,
      "totalPrice": 149.95,
      "selectedItemsPrice": 89.97,
      "estimatedShipping": 10.00,
      "estimatedTax": 7.20,
      "estimatedTotal": 107.17
    },
    "version": 1
  }
}
```

#### 3. Get Cart Summary
**GET** `/api/v1/carts/summary`

Get a summary of the cart with pricing calculations and totals.

**Response:**
```json
{
  "statusCode": 200,
  "message": "Cart summary retrieved successfully",
  "data": {
    "cartId": "uuid",
    "customerId": "uuid",
    "totalItems": 5,
    "selectedItems": 3,
    "totalPrice": 149.95,
    "selectedItemsPrice": 89.97,
    "estimatedShipping": 10.00,
    "estimatedTax": 7.20,
    "estimatedTotal": 107.17,
    "hasOutOfStockItems": false,
    "hasUnavailableItems": false
  }
}
```

### Cart Item Management Operations

#### 4. Add Item to Cart
**POST** `/api/v1/carts/items`

Add a new item to the cart or update quantity if item already exists.

**Request Body:**
```json
{
  "shopId": "uuid",
  "productId": "uuid",
  "productVariantId": "uuid",
  "quantity": 2
}
```

**Response:**
```json
{
  "statusCode": 201,
  "message": "Item added to cart successfully",
  "data": {
    "success": true,
    "message": "Item added to cart successfully",
    "cartId": "uuid",
    "affectedItemIds": ["uuid"],
    "updatedSummary": { ... }
  }
}
```

#### 5. Update Cart Item
**PUT** `/api/v1/carts/items/{itemId}`

Update a specific cart item's quantity and/or selection status.

**Path Parameters:**
- `itemId`: UUID of the cart item

**Request Body:**
```json
{
  "quantity": 3,
  "isSelected": true
}
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "Cart item updated successfully",
  "data": {
    "success": true,
    "message": "Cart item updated successfully",
    "cartId": "uuid",
    "affectedItemIds": ["uuid"],
    "updatedSummary": { ... }
  }
}
```

#### 6. Bulk Update Cart Items
**PATCH** `/api/v1/carts/items/bulk`

Update multiple cart items in a single request.

**Request Body:**
```json
{
  "cartItemUpdates": [
    {
      "cartItemId": "uuid",
      "quantity": 2,
      "isSelected": true
    },
    {
      "cartItemId": "uuid",
      "quantity": 1,
      "isSelected": false
    }
  ]
}
```

#### 7. Remove Single Cart Item
**DELETE** `/api/v1/carts/items/{itemId}`

Remove a single item from the cart.

**Path Parameters:**
- `itemId`: UUID of the cart item to remove

#### 8. Remove Multiple Cart Items
**DELETE** `/api/v1/carts/items`

Remove multiple items from the cart.

**Request Body:**
```json
{
  "cartItemIds": ["uuid1", "uuid2", "uuid3"],
  "quantity": null
}
```

#### 9. Toggle Item Selection
**PATCH** `/api/v1/carts/items/{itemId}/toggle-selection`

Toggle the selection status of a cart item.

#### 10. Select/Deselect All Items
**PATCH** `/api/v1/carts/select-all?selected=true`

Select or deselect all items in the cart.

**Query Parameters:**
- `selected`: boolean (default: true)

### Cart Operations

#### 11. Clear Cart
**DELETE** `/api/v1/carts`

Remove all items from the cart.

**Request Body (optional):**
```json
{}
```

#### 12. Merge Cart
**POST** `/api/v1/carts/merge/{sourceCartId}`

Merge another cart into the current user's cart.

**Path Parameters:**
- `sourceCartId`: UUID of the cart to merge from

### Cart Validation and Maintenance

#### 13. Validate Cart Items
**POST** `/api/v1/carts/validate`

Validate all cart items for availability, stock, and pricing.

**Response:**
```json
{
  "statusCode": 200,
  "message": "Cart validation completed",
  "data": {
    "success": true,
    "warnings": [
      "Product variant xyz is no longer available"
    ],
    "updatedSummary": { ... }
  }
}
```

#### 14. Remove Unavailable Items
**DELETE** `/api/v1/carts/unavailable-items`

Remove items that are no longer available from the cart.

### Checkout Operations

#### 15. Checkout
**POST** `/api/v1/carts/checkout`

Process cart checkout with inventory reservation and discount application.

**Request Body:**
```json
{
  "checkoutItems": [
    {
      "shopId": "uuid",
      "items": [
        {
          "productId": "uuid",
          "variantId": "uuid",
          "quantity": 2
        }
      ],
      "shippingDiscount": "uuid",
      "shopProductDiscount": "uuid",
      "globalProductDiscount": "uuid"
    }
  ]
}
```

## Error Handling

### Common Error Responses

#### 400 Bad Request
```json
{
  "statusCode": "400",
  "title": "Validation failed",
  "detail": "Invalid request content",
  "fieldErrors": [
    "Quantity must be greater than 0"
  ]
}
```

#### 404 Not Found
```json
{
  "statusCode": "404",
  "title": "Resource not found",
  "detail": "Cart item not found"
}
```

#### 409 Conflict
```json
{
  "statusCode": "409",
  "title": "Resource already exists",
  "detail": "Item already exists in cart"
}
```

#### 500 Internal Server Error
```json
{
  "statusCode": "500",
  "title": "An unexpected error occurred",
  "detail": "Database connection failed"
}
```

## Business Rules

### Cart Limits
- Maximum 100 items per cart
- Maximum 999 quantity per item
- Automatic cart creation if not exists

### Optimistic Locking
- All cart modifications use optimistic locking with retry mechanism
- Maximum 3 retry attempts with 100ms backoff
- Handles concurrent modifications gracefully

### Validation Rules
- Quantity must be positive integers
- Product and variant IDs must be valid UUIDs
- Cart items are validated against product service

### Pricing and Taxes
- Default tax rate: 8%
- Default shipping cost: $10.00
- Real-time price validation with product service
- Support for discount application via promotion service

## Legacy Endpoints (Deprecated)

The following endpoints are deprecated but maintained for backward compatibility:

- `POST /add` → Use `POST /items`
- `POST /item/{id}/select` → Use `PATCH /items/{itemId}/toggle-selection`
- `POST /item/{id}/delete` → Use `DELETE /items/{itemId}`
- `POST /clear-cart` → Use `DELETE /`

## Integration Points

### Product Service
- Validates product and variant availability
- Retrieves current pricing and stock information
- Reserves inventory during checkout

### Promotion Service  
- Applies discounts during checkout
- Calculates final pricing with promotions

### Authentication Service
- JWT token validation
- Customer identification via token claims

## Performance Considerations

- Uses optimistic locking for concurrent access
- Implements caching for frequently accessed data
- Pagination support for large carts
- Efficient bulk operations for multiple items
- Database query optimization with proper indexing 
