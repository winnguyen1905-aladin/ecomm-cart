package winnguyen1905.cart.config;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import winnguyen1905.cart.persistance.entity.ECart;
import winnguyen1905.cart.persistance.entity.ECartItem;
import winnguyen1905.cart.persistance.repository.CartItemRepository;
import winnguyen1905.cart.persistance.repository.CartRepository;

/**
 * Database initializer for Cart Service
 * Creates comprehensive seed data for testing purposes
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    log.info("Starting Cart Service database initialization...");
    
    try {
      createSeedData();
      log.info("✅ Cart Service database initialization completed successfully!");
      logSummary();
    } catch (Exception e) {
      log.error("❌ Failed to initialize Cart Service database", e);
      throw e;
    }
  }

  private void createSeedData() {
    // Create carts for different customers with various scenarios
    createCustomerCart1(); // Customer with mixed items
    createCustomerCart2(); // Customer with selected items only
    createCustomerCart3(); // Customer with large quantities
    createEmptyCart();     // Customer with empty cart
    
    log.info("Created comprehensive cart seed data");
  }

  /**
   * Customer 1: Mixed items from different shops/products (Active cart)
   */
  private void createCustomerCart1() {
    UUID customerId = UUID.fromString("11111111-1111-4111-8111-111111111111");
    
    ECart cart = ECart.builder()
        .customerId(customerId)
        .build();

    // iPhone 15 Pink 128GB - Selected
    ECartItem item1 = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("03000000-0000-4000-8000-000000000001")) // iPhone 15
        .productVariantId(UUID.fromString("04000000-0000-4000-8000-000000000001")) // Pink 128GB
        .quantity(1)
        .isSelected(true)
        .build();

    // Nike Air Max - Not selected (for testing toggle functionality)
    ECartItem item2 = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("03000000-0000-4000-8000-000000000003")) // Nike Air Max
        .productVariantId(UUID.fromString("04000000-0000-4000-8000-000000000006")) // Black US 9
        .quantity(2)
        .isSelected(false)
        .build();

    // Sony Headphones - Selected
    ECartItem item3 = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("03000000-0000-4000-8000-000000000006")) // Sony Headphones
        .productVariantId(UUID.fromString("04000000-0000-4000-8000-000000000013")) // Black
        .quantity(1)
        .isSelected(true)
        .build();

    cart.getCartItems().addAll(Arrays.asList(item1, item2, item3));
    cartRepository.save(cart);
    
    log.info("Created cart for customer {} with {} items", customerId, cart.getCartItems().size());
  }

  /**
   * Customer 2: All items selected (Ready for checkout)
   */
  private void createCustomerCart2() {
    UUID customerId = UUID.fromString("22222222-2222-4222-8222-222222222222");
    
    ECart cart = ECart.builder()
        .customerId(customerId)
        .build();

    // Samsung Galaxy S24 - Selected
    ECartItem item1 = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("03000000-0000-4000-8000-000000000002")) // Samsung Galaxy S24
        .productVariantId(UUID.fromString("04000000-0000-4000-8000-000000000004")) // Titanium Gray
        .quantity(1)
        .isSelected(true)
        .build();

    // IKEA Desk - Selected
    ECartItem item2 = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("03000000-0000-4000-8000-000000000005")) // IKEA Desk
        .productVariantId(UUID.fromString("04000000-0000-4000-8000-000000000011")) // White
        .quantity(1)
        .isSelected(true)
        .build();

    cart.getCartItems().addAll(Arrays.asList(item1, item2));
    cartRepository.save(cart);
    
    log.info("Created cart for customer {} with {} items (all selected)", customerId, cart.getCartItems().size());
  }

  /**
   * Customer 3: Large quantities and multiple variants (Bulk order scenario)
   */
  private void createCustomerCart3() {
    UUID customerId = UUID.fromString("33333333-3333-4333-8333-333333333333");
    
    ECart cart = ECart.builder()
        .customerId(customerId)
        .build();

    // iPhone 15 Blue 256GB - Large quantity
    ECartItem item1 = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("03000000-0000-4000-8000-000000000001")) // iPhone 15
        .productVariantId(UUID.fromString("04000000-0000-4000-8000-000000000002")) // Blue 256GB
        .quantity(5)
        .isSelected(true)
        .build();

    // Adidas Shoes - Multiple sizes
    ECartItem item2 = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("03000000-0000-4000-8000-000000000004")) // Adidas Ultraboost
        .productVariantId(UUID.fromString("04000000-0000-4000-8000-000000000009")) // Core Black
        .quantity(3)
        .isSelected(true)
        .build();

    ECartItem item3 = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("03000000-0000-4000-8000-000000000004")) // Adidas Ultraboost
        .productVariantId(UUID.fromString("04000000-0000-4000-8000-000000000010")) // White
        .quantity(2)
        .isSelected(false)
        .build();

    // Nike shoes - Different colors
    ECartItem item4 = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("03000000-0000-4000-8000-000000000003")) // Nike Air Max
        .productVariantId(UUID.fromString("04000000-0000-4000-8000-000000000007")) // White US 10
        .quantity(4)
        .isSelected(true)
        .build();

    cart.getCartItems().addAll(Arrays.asList(item1, item2, item3, item4));
    cartRepository.save(cart);
    
    log.info("Created cart for customer {} with {} items (bulk order)", customerId, cart.getCartItems().size());
  }

  /**
   * Customer 4: Empty cart (New customer scenario)
   */
  private void createEmptyCart() {
    UUID customerId = UUID.fromString("44444444-4444-4444-8444-444444444444");
    
    ECart cart = ECart.builder()
        .customerId(customerId)
        .build();

    cartRepository.save(cart);
    
    log.info("Created empty cart for customer {}", customerId);
  }

  private void logSummary() {
    long totalCarts = cartRepository.count();
    long totalItems = cartItemRepository.count();
    
    log.info("\n" +
        "🛒 CART SERVICE SEED DATA SUMMARY\n" +
        "================================\n" +
        "📦 Total Carts: {}\n" +
        "🛍️  Total Cart Items: {}\n" +
        "\n" +
        "👥 Test Customers:\n" +
        "   • Customer 1 (11111111-1111-4111-8111-111111111111): Mixed cart with 3 items\n" +
        "   • Customer 2 (22222222-2222-4222-8222-222222222222): Ready for checkout with 2 items\n" +
        "   • Customer 3 (33333333-3333-4333-8333-333333333333): Bulk order with 4 items\n" +
        "   • Customer 4 (44444444-4444-4444-8444-444444444444): Empty cart\n" +
        "\n" +
        "🧪 Test Scenarios Available:\n" +
        "   • Mixed selected/unselected items\n" +
        "   • Different product types (phones, shoes, furniture, headphones)\n" +
        "   • Various quantities (1-5 items per type)\n" +
        "   • Ready-to-checkout scenarios\n" +
        "   • Empty cart scenarios\n" +
        "   • Bulk order testing\n" +
        "\n" +
        "🔗 Product References:\n" +
        "   • iPhone 15 variants: Pink, Blue, Black\n" +
        "   • Samsung Galaxy S24: Titanium Gray, Violet\n" +
        "   • Nike Air Max: Black, White, Red (various sizes)\n" +
        "   • Adidas Ultraboost: Core Black, White\n" +
        "   • IKEA Desk: White, Black-brown\n" +
        "   • Sony Headphones: Black, Silver\n" +
        "================================",
        totalCarts, totalItems);
  }
}
