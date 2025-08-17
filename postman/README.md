# Cart Service - Complete Testing Setup

This directory contains comprehensive testing resources for the Cart Service, including Postman collections, environment variables, and detailed testing scenarios.

## 📋 Overview

The Cart Service has been enhanced with:

- **Simplified Clean Controller**: Focused RESTful API with consistent endpoints
- **Comprehensive Seed Data**: 4 test customers with different cart scenarios
- **Complete API Coverage**: Postman collections covering all controller endpoints
- **Testing Scenarios**: Detailed test cases and validation procedures

## 🗂️ Files in This Directory

- `Cart-Service-API.postman_collection.json` - Complete API collection with 30+ requests
- `Cart-Service-Environment.postman_environment.json` - Environment variables for testing
- `README.md` - This file

## 🚀 Quick Start

### 1. Import Postman Collections

1. Open Postman
2. Click "Import" → "Upload Files"
3. Import both JSON files:
   - `Cart-Service-API.postman_collection.json`
   - `Cart-Service-Environment.postman_environment.json`
4. Select the "Cart Service Environment" in Postman

### 2. Start the Application

```bash
# Navigate to the cart service directory
cd cart/

# Start the application (this will automatically run DatabaseInitializer)
./mvnw spring-boot:run

# The application will:
# - Create comprehensive seed data (4 customers with different cart scenarios)
# - Display a summary of created data in the logs
```

### 3. Verify Setup

Run these requests in Postman to confirm everything is working:

1. **Get Cart - Customer 1**: `Cart Retrieval > Get Cart - Customer 1 (Mixed Items)`
2. **Get Cart Summary**: `Cart Retrieval > Get Cart Summary - Customer 2 (Ready for Checkout)`
3. **Check Empty Cart**: `Cart Retrieval > Get Cart - Customer 4 (Empty Cart)`

## 📊 Seed Data Summary

### Test Customers Created

```
✓ Customer 1 (11111111-1111-4111-8111-111111111111): Mixed cart with 3 items
  • iPhone 15 Pink 128GB (qty: 1, selected: true)
  • Nike Air Max Black US 9 (qty: 2, selected: false)  
  • Sony WH-1000XM5 Black (qty: 1, selected: true)

✓ Customer 2 (22222222-2222-4222-8222-222222222222): Ready for checkout with 2 items
  • Samsung Galaxy S24 Titanium Gray (qty: 1, selected: true)
  • IKEA BEKANT White Desk (qty: 1, selected: true)

✓ Customer 3 (33333333-3333-4333-8333-333333333333): Bulk order with 4 items
  • iPhone 15 Blue 256GB (qty: 5, selected: true)
  • Adidas Ultraboost Core Black (qty: 3, selected: true)
  • Adidas Ultraboost White (qty: 2, selected: false)
  • Nike Air Max White US 10 (qty: 4, selected: true)

✓ Customer 4 (44444444-4444-4444-8444-444444444444): Empty cart for testing
```

## 🎯 API Endpoints Overview

### Simplified Clean Architecture

The CartController has been completely redesigned with:

- **Clean RESTful Design**: Consistent HTTP methods and clear endpoint paths
- **Organized Operations**: Grouped by functionality (retrieval, management, selection, operations, validation, checkout)
- **No Deprecated Endpoints**: Removed all legacy endpoints for cleaner API surface
- **Consistent Response Format**: All operations return appropriate response DTOs

### Endpoint Categories

1. **Cart Retrieval**
   - `GET /api/v1/carts` - Get customer's cart
   - `GET /api/v1/carts/summary` - Get cart summary with totals

2. **Cart Item Management**
   - `POST /api/v1/carts/items` - Add item to cart
   - `PUT /api/v1/carts/items/{itemId}` - Update cart item
   - `PATCH /api/v1/carts/items/bulk` - Bulk update cart items
   - `DELETE /api/v1/carts/items/{itemId}` - Remove single item
   - `DELETE /api/v1/carts/items` - Remove multiple items

3. **Cart Item Selection**
   - `PATCH /api/v1/carts/items/{itemId}/toggle` - Toggle item selection
   - `PATCH /api/v1/carts/select-all` - Select/deselect all items

4. **Cart Operations**
   - `DELETE /api/v1/carts` - Clear entire cart
   - `POST /api/v1/carts/merge/{sourceCartId}` - Merge carts

5. **Cart Validation**
   - `POST /api/v1/carts/validate` - Validate cart items
   - `DELETE /api/v1/carts/unavailable` - Remove unavailable items

6. **Checkout**
   - `POST /api/v1/carts/checkout` - Checkout cart

## 🧪 Test Scenarios

### 1. Basic Cart Operations

#### 1.1 Cart Retrieval
```
Test: Get Cart - Customer 1 (Mixed Items)
Header: X-User-Id: 11111111-1111-4111-8111-111111111111
Expected: Returns cart with 3 items (mixed selection status)
```

#### 1.2 Cart Summary
```
Test: Get Cart Summary - Customer 2 (Ready for Checkout)
Header: X-User-Id: 22222222-2222-4222-8222-222222222222
Expected: Returns summary with total pricing for selected items
```

### 2. Item Management

#### 2.1 Add Items to Cart
```
Test: Add Item to Cart - iPhone 15 Black
Customer: Customer 4 (Empty Cart)
Payload: {shopId, productId, productVariantId, quantity}
Expected: Creates cart item and returns operation response
```

#### 2.2 Update Item Quantities
```
Test: Update Cart Item Quantity
Method: PUT /api/v1/carts/items/{itemId}
Payload: {quantity: 3, isSelected: true}
Expected: Updates item quantity and selection status
```

#### 2.3 Bulk Operations
```
Test: Bulk Update Cart Items
Customer: Customer 3 (Bulk Order)
Payload: Array of updates with itemId, quantity, isSelected
Expected: Updates multiple items in single request
```

### 3. Selection Management

#### 3.1 Toggle Selection
```
Test: Toggle Item Selection
Method: PATCH /api/v1/carts/items/{itemId}/toggle
Expected: Switches item selection status
```

#### 3.2 Select All Items
```
Test: Select All Items
Method: PATCH /api/v1/carts/select-all?selected=true
Expected: Selects all items in cart
```

### 4. Cart Operations

#### 4.1 Clear Cart
```
Test: Clear Cart
Method: DELETE /api/v1/carts
Customer: Customer 4
Expected: Removes all items from cart
```

#### 4.2 Merge Carts
```
Test: Merge Cart from Customer 3 to Customer 1
Method: POST /api/v1/carts/merge/{sourceCartId}
Expected: Combines items from source cart into destination cart
```

### 5. Validation and Checkout

#### 5.1 Validate Cart
```
Test: Validate Cart Items
Method: POST /api/v1/carts/validate
Expected: Returns validation results (availability, pricing, etc.)
```

#### 5.2 Checkout
```
Test: Checkout Cart - Customer 2 (Ready)
Payload: {shippingAddress, paymentMethod, promoCode, notes}
Expected: Processes checkout and returns order response
```

## 🎨 Test Workflow Examples

### Complete Customer Journey

#### New Customer (Customer 4)
1. **Check Empty Cart** → Should return empty cart
2. **Add First Product** → iPhone 15 Pink
3. **Add Second Product** → Nike Air Max Black
4. **View Updated Cart** → Should show 2 items
5. **Checkout** → Complete purchase

#### Bulk Order Management (Customer 3)
1. **View Bulk Cart** → See 4 items with large quantities
2. **Select All Items** → Ensure all items are selected
3. **Validate Cart** → Check availability and pricing
4. **Bulk Update** → Modify quantities and selections
5. **Checkout** → Process bulk order

### Error Testing Scenarios

1. **Add Invalid Item**
   - Invalid product ID
   - Invalid variant ID
   - Zero or negative quantity

2. **Update Non-existent Item**
   - Invalid cart item ID
   - Update item from different customer's cart

3. **Checkout Invalid Cart**
   - Empty cart checkout
   - Cart with no selected items
   - Invalid shipping address

## 🛠️ Environment Variables

The environment includes all necessary variables:

```json
{
  "baseUrl": "http://localhost:8081",
  "customer1Id": "11111111-1111-4111-8111-111111111111",
  "customer2Id": "22222222-2222-4222-8222-222222222222", 
  "customer3Id": "33333333-3333-4333-8333-333333333333",
  "customer4Id": "44444444-4444-4444-8444-444444444444",
  "shopId": "11111111-1111-4111-8111-111111111111",
  // ... and 20+ product/variant IDs
}
```

## 📖 Testing Workflow

### Basic Workflow
1. **Verify Setup** → Run cart retrieval tests
2. **Test Item Management** → Add, update, remove items
3. **Test Selection** → Toggle selections, select all
4. **Test Operations** → Clear cart, merge carts
5. **Test Checkout** → Complete purchase flow

### Advanced Workflow
1. **Bulk Operations** → Test with Customer 3's large cart
2. **Cross-Customer Testing** → Test isolation between customers
3. **Error Handling** → Test invalid inputs and edge cases
4. **Performance Testing** → Large quantities and concurrent operations

## 🔧 Product Types and Variants

### Electronics
- **iPhone 15**: Pink 128GB, Blue 256GB, Black 512GB
- **Samsung Galaxy S24**: Titanium Gray, Violet
- **Sony WH-1000XM5**: Black, Silver

### Fashion
- **Nike Air Max**: Black US 9, White US 10, Red US 11
- **Adidas Ultraboost**: Core Black, White

### Furniture
- **IKEA BEKANT Desk**: White, Black-brown

## 🛠️ Troubleshooting

### Common Issues

**Cart Not Found**
```bash
# Verify customer ID exists in seed data
GET {{baseUrl}}/api/v1/carts
Headers: X-User-Id: {customerId}
```

**Item Not Added**
```bash
# Check if product/variant IDs are valid
# Verify required fields in request body
# Check application logs for validation errors
```

**Database Issues**
```bash
# Restart application to reinitialize seed data
# Check database connectivity
# Verify H2 console at http://localhost:8081/h2-console
```

## 📈 Performance Considerations

### Large Cart Testing
- Customer 3 has bulk quantities for performance testing
- Test pagination with large cart items
- Concurrent operations on same cart
- Bulk update performance

### Memory Usage
- Monitor memory with large cart operations
- Test with many concurrent customers
- Validate database connection pooling

## 🔐 Security Testing

### Authentication Testing
- Customer isolation (customers can't access each other's carts)
- Header validation (X-User-Id, X-Account-Type)
- Invalid customer ID handling

### Data Validation
- Input validation on all create/update operations
- UUID format validation
- Business rule enforcement (positive quantities, etc.)

## 📝 Extending the Tests

### Adding New Test Cases
1. Copy existing requests as templates
2. Modify payloads for specific test scenarios
3. Use environment variables for consistency

### Custom Scenarios
1. Create new folders in the collection
2. Chain requests using Postman tests
3. Set dynamic variables from responses

### Integration Testing
1. Combine multiple operations in workflows
2. Use Postman's test scripts for validation
3. Set up automated test runs

## 🎯 Next Steps

1. **Run Basic Tests**: Start with cart retrieval and item management
2. **Explore Scenarios**: Try the provided test scenarios
3. **Create Custom Tests**: Modify requests for your specific needs
4. **Performance Testing**: Use bulk operations and concurrent requests
5. **Integration Testing**: Combine multiple operations in workflows

This complete testing setup provides everything needed to thoroughly validate the Cart Service functionality, from basic CRUD operations to complex cart management and checkout flows. 
