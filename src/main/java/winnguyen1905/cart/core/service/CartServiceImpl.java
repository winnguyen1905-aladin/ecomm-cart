package winnguyen1905.cart.core.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import winnguyen1905.cart.core.feign.ProductServiceClient;
import winnguyen1905.cart.core.feign.PromotionServiceClient;
import winnguyen1905.cart.core.mapper.CartMapper;
import winnguyen1905.cart.core.model.ReserveInventoryRequest;
import winnguyen1905.cart.core.model.ReserveInventoryResponse;
import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.request.BulkUpdateCartRequest;
import winnguyen1905.cart.core.model.request.CheckoutRequest;
import winnguyen1905.cart.core.model.request.ClearCartRequest;
import winnguyen1905.cart.core.model.request.ProductVariantByShopVm;
import winnguyen1905.cart.core.model.request.PromotionApplyRequest;
import winnguyen1905.cart.core.model.request.RemoveCartItemRequest;
import winnguyen1905.cart.core.model.request.UpdateCartItemRequest;
import winnguyen1905.cart.core.model.response.CartItemResponse;
import winnguyen1905.cart.core.model.response.CartOperationResponse;
import winnguyen1905.cart.core.model.response.CartResponse;
import winnguyen1905.cart.core.model.response.CartSummaryResponse;
import winnguyen1905.cart.core.model.response.CheckoutOrderResponse;
import winnguyen1905.cart.core.model.response.EnhancedCartResponse;
import winnguyen1905.cart.core.model.response.PriceStatisticsResponse;
import winnguyen1905.cart.core.model.response.ProductVariantReviewVm;
import winnguyen1905.cart.exception.BadRequestException;
import winnguyen1905.cart.exception.BusinessLogicException;
import winnguyen1905.cart.exception.ResourceNotFoundException;
import winnguyen1905.cart.persistance.entity.ECart;
import winnguyen1905.cart.persistance.entity.ECartItem;
import winnguyen1905.cart.persistance.repository.CartItemRepository;
import winnguyen1905.cart.persistance.repository.CartRepository;
import winnguyen1905.cart.secure.RestResponse;
import winnguyen1905.cart.secure.TAccountRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductServiceClient productServiceClient;
  private final PromotionServiceClient promotionServiceClient;

  // Constants
  private static final int MAX_CART_ITEMS = 100;
  private static final int MAX_ITEM_QUANTITY = 999;
  private static final double DEFAULT_TAX_RATE = 0.08;
  private static final double DEFAULT_SHIPPING_COST = 10.0;

  // ========== Cart Retrieval Operations ==========

  @Override
  @Transactional(readOnly = true)
  public CartResponse getCart(TAccountRequest accountRequest, Pageable pageable) {
    ECart cart = findCartByCustomerId(accountRequest.id());
    
    HashMap<UUID, ECartItem> mapECartItem = cart.getCartItems().stream()
        .collect(Collectors.toMap(ECartItem::getProductVariantId,
            Function.identity(), (existing, replacement) -> replacement, HashMap::new));

    ProductVariantByShopVm cartByShopProductResponse = getProductDetails(mapECartItem.keySet());
    return CartMapper.with(mapECartItem, cartByShopProductResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public EnhancedCartResponse getEnhancedCart(TAccountRequest accountRequest, Pageable pageable) {
    ECart cart = findCartByCustomerId(accountRequest.id());
    
    // Get product details for all cart items
    Set<UUID> variantIds = cart.getCartItems().stream()
        .map(ECartItem::getProductVariantId)
        .collect(Collectors.toSet());
    
    ProductVariantByShopVm productDetails = getProductDetails(variantIds);
    
    // Build enhanced response
    List<EnhancedCartResponse.CartByShop> cartByShops = buildEnhancedCartByShops(cart, productDetails);
    CartSummaryResponse summary = calculateCartSummary(cart, productDetails);
    
    return EnhancedCartResponse.builder()
        .cartId(cart.getId())
        .customerId(cart.getCustomerId())
        .cartByShops(cartByShops)
        .summary(summary)
        .version(cart.getVersion())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public CartSummaryResponse getCartSummary(TAccountRequest accountRequest) {
    ECart cart = findCartByCustomerId(accountRequest.id());
    
    Set<UUID> variantIds = cart.getCartItems().stream()
        .map(ECartItem::getProductVariantId)
        .collect(Collectors.toSet());
    
    ProductVariantByShopVm productDetails = getProductDetails(variantIds);
    return calculateCartSummary(cart, productDetails);
  }

  // ========== Cart Item Management Operations ==========

  @Override
  @Transactional
  @Retryable(value = {ObjectOptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 100))
  public CartOperationResponse addToCart(TAccountRequest accountRequest, AddToCartRequest addToCartRequest) {
    log.info("Adding item to cart for customer: {}", accountRequest.id());
    
    // Validate request
    validateAddToCartRequest(addToCartRequest);
    
    ECart cart = findOrCreateCart(accountRequest.id());
    
    // Check cart limits
    if (cart.getCartItems().size() >= MAX_CART_ITEMS) {
      throw new BusinessLogicException("Cart has reached maximum capacity of " + MAX_CART_ITEMS + " items");
    }

    // Check if item already exists in cart
    Optional<ECartItem> existingItem = cartItemRepository.findByCartAndProductVariantId(
        cart.getId(), addToCartRequest.getProductVariantId());

    List<UUID> affectedItemIds = new ArrayList<>();
    
    if (existingItem.isPresent()) {
      // Update existing item
      ECartItem cartItem = existingItem.get();
      int newQuantity = cartItem.getQuantity() + addToCartRequest.getQuantity();
      
      if (newQuantity > MAX_ITEM_QUANTITY) {
        throw new BusinessLogicException("Item quantity cannot exceed " + MAX_ITEM_QUANTITY);
      }
      
      cartItem.setQuantity(newQuantity);
      cartItemRepository.save(cartItem);
      affectedItemIds.add(cartItem.getId());
      
      log.info("Updated existing cart item {} with new quantity: {}", cartItem.getId(), newQuantity);
    } else {
      // Create new item
      ECartItem newItem = ECartItem.builder()
          .cart(cart)
          .quantity(addToCartRequest.getQuantity())
          .productId(addToCartRequest.getProductId())
          .productVariantId(addToCartRequest.getProductVariantId())
          .isSelected(true)
          .build();
      
      ECartItem savedItem = cartItemRepository.save(newItem);
      affectedItemIds.add(savedItem.getId());
      
      log.info("Created new cart item {} with quantity: {}", savedItem.getId(), addToCartRequest.getQuantity());
    }
    
    // Get updated summary
    CartSummaryResponse summary = getCartSummary(accountRequest);
    
    return CartOperationResponse.builder()
        .success(true)
        .message("Item added to cart successfully")
        .cartId(cart.getId())
        .affectedItemIds(affectedItemIds)
        .updatedSummary(summary)
        .build();
  }

  @Override
  @Transactional
  @Retryable(value = {ObjectOptimisticLockingFailureException.class}, maxAttempts = 3, backoff = @Backoff(delay = 100))
  public CartOperationResponse updateCartItem(TAccountRequest accountRequest, UpdateCartItemRequest updateRequest) {
    log.info("Updating cart item {} for customer: {}", updateRequest.getCartItemId(), accountRequest.id());
    
    ECartItem cartItem = cartItemRepository.findByCustomerAndItemId(accountRequest.id(), updateRequest.getCartItemId())
        .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
    
    List<String> warnings = new ArrayList<>();
    
    // Update quantity if provided
    if (updateRequest.getQuantity() != null) {
      if (updateRequest.getQuantity() > MAX_ITEM_QUANTITY) {
        throw new BusinessLogicException("Item quantity cannot exceed " + MAX_ITEM_QUANTITY);
      }
      cartItem.setQuantity(updateRequest.getQuantity());
    }
    
    // Update selection if provided
    if (updateRequest.getIsSelected() != null) {
      cartItem.setIsSelected(updateRequest.getIsSelected());
    }
    
    cartItemRepository.save(cartItem);
    
    CartSummaryResponse summary = getCartSummary(accountRequest);
    
    return CartOperationResponse.builder()
        .success(true)
        .message("Cart item updated successfully")
        .cartId(cartItem.getCart().getId())
        .affectedItemIds(List.of(cartItem.getId()))
        .updatedSummary(summary)
        .warnings(warnings)
        .build();
  }

  @Override
  @Transactional
  public CartOperationResponse bulkUpdateCartItems(TAccountRequest accountRequest, BulkUpdateCartRequest bulkUpdateRequest) {
    log.info("Bulk updating {} cart items for customer: {}", bulkUpdateRequest.getCartItemUpdates().size(), accountRequest.id());
    
    List<UUID> affectedItemIds = new ArrayList<>();
    List<String> warnings = new ArrayList<>();
    
    for (BulkUpdateCartRequest.CartItemUpdate update : bulkUpdateRequest.getCartItemUpdates()) {
      try {
        UpdateCartItemRequest singleUpdate = UpdateCartItemRequest.builder()
            .cartItemId(update.getCartItemId())
            .quantity(update.getQuantity())
            .isSelected(update.getIsSelected())
            .build();
        
        CartOperationResponse result = updateCartItem(accountRequest, singleUpdate);
        affectedItemIds.addAll(result.affectedItemIds());
      } catch (Exception e) {
        warnings.add("Failed to update item " + update.getCartItemId() + ": " + e.getMessage());
        log.warn("Failed to update cart item {}: {}", update.getCartItemId(), e.getMessage());
      }
    }
    
    CartSummaryResponse summary = getCartSummary(accountRequest);
    
    return CartOperationResponse.builder()
        .success(true)
        .message("Bulk update completed")
        .affectedItemIds(affectedItemIds)
        .updatedSummary(summary)
        .warnings(warnings)
        .build();
  }

  @Override
  @Transactional
  public CartOperationResponse removeCartItems(TAccountRequest accountRequest, RemoveCartItemRequest removeRequest) {
    log.info("Removing {} cart items for customer: {}", removeRequest.getCartItemIds().size(), accountRequest.id());
    
    List<UUID> affectedItemIds = new ArrayList<>();
    
    for (UUID itemId : removeRequest.getCartItemIds()) {
      Optional<ECartItem> cartItem = cartItemRepository.findByCustomerAndItemId(accountRequest.id(), itemId);
      if (cartItem.isPresent()) {
        cartItemRepository.delete(cartItem.get());
        affectedItemIds.add(itemId);
      }
    }
    
    CartSummaryResponse summary = getCartSummary(accountRequest);
    
    return CartOperationResponse.builder()
        .success(true)
        .message("Cart items removed successfully")
        .affectedItemIds(affectedItemIds)
        .updatedSummary(summary)
        .build();
  }

  @Override
  @Transactional
  public CartOperationResponse toggleCartItemSelection(TAccountRequest accountRequest, UUID cartItemId) {
    ECartItem cartItem = cartItemRepository.findByCustomerAndItemId(accountRequest.id(), cartItemId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
    
    cartItem.setIsSelected(!cartItem.getIsSelected());
    cartItemRepository.save(cartItem);
    
    CartSummaryResponse summary = getCartSummary(accountRequest);
    
    return CartOperationResponse.builder()
        .success(true)
        .message("Cart item selection toggled successfully")
        .cartId(cartItem.getCart().getId())
        .affectedItemIds(List.of(cartItemId))
        .updatedSummary(summary)
        .build();
  }

  @Override
  @Transactional
  public CartOperationResponse selectAllCartItems(TAccountRequest accountRequest, boolean selected) {
    List<ECartItem> cartItems = cartItemRepository.findAllByCustomerId(accountRequest.id());
    
    List<UUID> affectedItemIds = cartItems.stream()
        .peek(item -> item.setIsSelected(selected))
        .map(ECartItem::getId)
        .collect(Collectors.toList());
    
    cartItemRepository.saveAll(cartItems);
    
    CartSummaryResponse summary = getCartSummary(accountRequest);
    
    return CartOperationResponse.builder()
        .success(true)
        .message(selected ? "All items selected" : "All items deselected")
        .affectedItemIds(affectedItemIds)
        .updatedSummary(summary)
        .build();
  }

  // ========== Cart Operations ==========

  @Override
  @Transactional
  public CartOperationResponse clearCart(TAccountRequest accountRequest, ClearCartRequest clearCartRequest) {
    ECart cart = findCartByCustomerId(accountRequest.id());
    
    List<UUID> affectedItemIds = cart.getCartItems().stream()
        .map(ECartItem::getId)
        .collect(Collectors.toList());
    
    cartItemRepository.deleteCustomerItems(accountRequest.id(), affectedItemIds);
    
    CartSummaryResponse summary = getCartSummary(accountRequest);
    
    return CartOperationResponse.builder()
        .success(true)
        .message("Cart cleared successfully")
        .cartId(cart.getId())
        .affectedItemIds(affectedItemIds)
        .updatedSummary(summary)
        .build();
  }

  @Override
  @Transactional
  public CartOperationResponse mergeCart(TAccountRequest accountRequest, UUID sourceCartId) {
    // Implementation for merging carts - placeholder for now
    throw new UnsupportedOperationException("Cart merge functionality not yet implemented");
  }

  // ========== Cart Validation and Maintenance ==========

  @Override
  @Transactional(readOnly = true)
  public CartOperationResponse validateCartItems(TAccountRequest accountRequest) {
    ECart cart = findCartByCustomerId(accountRequest.id());
    
    Set<UUID> variantIds = cart.getCartItems().stream()
        .map(ECartItem::getProductVariantId)
        .collect(Collectors.toSet());
    
    ProductVariantByShopVm productDetails = getProductDetails(variantIds);
    
    List<String> warnings = new ArrayList<>();
    
    // Validate each item
    for (ECartItem item : cart.getCartItems()) {
      // Check if product variant still exists and is available
      boolean found = productDetails.shopProductVariants().stream()
          .flatMap(shop -> shop.productVariantReviews().stream())
          .anyMatch(variant -> variant.id().equals(item.getProductVariantId()));
      
      if (!found) {
        warnings.add("Product variant " + item.getProductVariantId() + " is no longer available");
      }
    }
    
    CartSummaryResponse summary = calculateCartSummary(cart, productDetails);
    
    return CartOperationResponse.builder()
        .success(true)
        .message("Cart validation completed")
        .cartId(cart.getId())
        .updatedSummary(summary)
        .warnings(warnings)
        .build();
  }

  @Override
  @Transactional
  public CartOperationResponse removeUnavailableItems(TAccountRequest accountRequest) {
    CartOperationResponse validation = validateCartItems(accountRequest);
    
    if (validation.warnings().isEmpty()) {
      return CartOperationResponse.builder()
          .success(true)
          .message("No unavailable items found")
          .updatedSummary(validation.updatedSummary())
          .build();
    }
    
    // Remove unavailable items - implementation would go here
    // For now, return validation result
    return validation;
  }

  // ========== Legacy Methods ==========

  @Override
  @Transactional
  @Deprecated
  public void deleteCartItem(TAccountRequest accountRequest, UUID cartItemId) {
    RemoveCartItemRequest request = RemoveCartItemRequest.builder()
        .cartItemIds(List.of(cartItemId))
        .build();
    removeCartItems(accountRequest, request);
  }

  @Override
  @Transactional
  @Deprecated
  public void selectCartItem(TAccountRequest accountRequest, UUID cartItemId) {
    toggleCartItemSelection(accountRequest, cartItemId);
  }

  // ========== Existing Methods (Updated) ==========

  @Override
  public void applyDiscount(TAccountRequest accountRequest, CheckoutRequest checkoutRequest) {
    // Implementation remains the same as in the original file
    throw new UnsupportedOperationException("Unimplemented method 'applyDiscount'");
  }

  @Override
  public CheckoutOrderResponse checkout(TAccountRequest accountRequest, CheckoutRequest checkoutRequest) {
    // Keep the existing checkout implementation
    // B1. Reserve inventory
    List<ReserveInventoryRequest.Item> items = new ArrayList<>();

    checkoutRequest.getCheckoutItems().stream().forEach(checkoutItem -> {
      checkoutItem.getItems().stream().forEach(item -> {
        items.add(ReserveInventoryRequest.Item.builder()
            .productId(item.getProductId())
            .variantId(item.getVariantId())
            .quantity(item.getQuantity())
            .build());
      });
    });

    ReserveInventoryRequest reserveInventoryRequest = ReserveInventoryRequest.builder()
        .reservationId(null)
        .items(items)
        .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
        .build();
    RestResponse<ReserveInventoryResponse> responseEntity = this.productServiceClient
        .reserveInventory(reserveInventoryRequest);
    if (!responseEntity.data().isStatus()) {
      throw new BadRequestException("Failed to reserve inventory");
    }

    List<CheckoutOrderResponse.CheckoutItemRequest> checkoutItems = new ArrayList<>();

    // Process each shop's items and apply promotions
    for (CheckoutRequest.CheckoutItemRequest checkoutItem : checkoutRequest.getCheckoutItems()) {
      // Get all variant IDs for the current checkout item
      Set<UUID> variantIds = checkoutItem.getItems().stream()
          .map(CheckoutRequest.Item::getVariantId)
          .collect(Collectors.toSet());

      // Fetch product details from the product service
      RestResponse<ProductVariantByShopVm> response = this.productServiceClient.getProductCartDetail(variantIds);
      if (response == null || response.data() == null) {
        throw new RuntimeException("Failed to get product details for shop: " + checkoutItem.getShopId());
      }

      // Create a map of variantId to quantity for quick lookup
      Map<UUID, Integer> variantQuantityMap = checkoutItem.getItems().stream()
          .collect(Collectors.toMap(
              CheckoutRequest.Item::getVariantId,
              CheckoutRequest.Item::getQuantity));

      // Calculate subtotal for this shop's items
      double subtotal = response.data().shopProductVariants().stream()
          .flatMap(shop -> shop.productVariantReviews().stream())
          .filter(variant -> variantQuantityMap.containsKey(variant.id()))
          .mapToDouble(variant -> {
            int quantity = variantQuantityMap.getOrDefault(variant.id(), 0);
            return variant.price() * quantity;
          })
          .sum();

      // Build checkout item response
      CheckoutOrderResponse.CheckoutItemRequest checkoutItemResponse = CheckoutOrderResponse.CheckoutItemRequest
          .builder()
          .shopId(checkoutItem.getShopId())
          .items(checkoutItem.getItems().stream()
              .map(item -> CheckoutOrderResponse.Item.builder()
                  .productId(item.getProductId())
                  .variantId(item.getVariantId())
                  .quantity(item.getQuantity())
                  .build())
              .collect(Collectors.toList()))
          .shippingDiscount(checkoutItem.getShippingDiscount())
          .shopProductDiscount(checkoutItem.getShopProductDiscount())
          .globalProductDiscount(checkoutItem.getGlobalProductDiscount())
          .build();

      // Apply promotions if any discount IDs are provided
      if (checkoutItem.getShippingDiscount() != null ||
          checkoutItem.getShopProductDiscount() != null ||
          checkoutItem.getGlobalProductDiscount() != null) {

        PromotionApplyRequest promotionRequest = new PromotionApplyRequest();
        promotionRequest.setShopId(checkoutItem.getShopId());
        promotionRequest.setTotal(subtotal);
        promotionRequest.setShippingDiscountId(checkoutItem.getShippingDiscount());
        promotionRequest.setShopProductDiscountId(checkoutItem.getShopProductDiscount());
        promotionRequest.setGlobalProductDiscountId(checkoutItem.getGlobalProductDiscount());

        try {
          RestResponse<PriceStatisticsResponse> promotionResponse = this.promotionServiceClient
              .applyDiscountShop(promotionRequest);

          if (promotionResponse != null && promotionResponse.data() != null) {
            PriceStatisticsResponse stats = promotionResponse.data();
            System.out.println(String.format(
                "Shop %s - Original: %.2f, Discounted: %.2f (Saved: %.2f)",
                checkoutItem.getShopId(),
                stats.totalPrice(),
                stats.finalPrice(),
                stats.totalDiscountVoucher()));

            // Set the price statistics in the response
            checkoutItemResponse.setPriceStatistics(stats);
          }
        } catch (Exception e) {
          System.err.println("Failed to apply promotions for shop " + checkoutItem.getShopId() + ": " + e.getMessage());
          // If promotion fails, still continue with the original price
          checkoutItemResponse.setPriceStatistics(PriceStatisticsResponse.builder()
              .totalPrice(subtotal)
              .finalPrice(subtotal)
              .totalDiscountVoucher(0.0)
              .build());
        }
      } else {
        // No promotions to apply, use the subtotal
        System.out.println(String.format(
            "Shop %s - No promotions applied. Total: %.2f",
            checkoutItem.getShopId(),
            subtotal));

        checkoutItemResponse.setPriceStatistics(PriceStatisticsResponse.builder()
            .totalPrice(subtotal)
            .finalPrice(subtotal)
            .totalDiscountVoucher(0.0)
            .build());
      }

      checkoutItems.add(checkoutItemResponse);
    }

    // Build and return the final response
    return CheckoutOrderResponse.builder()
        .checkoutItems(checkoutItems)
        .build();
  }

  // ========== Helper Methods ==========

  private ECart findCartByCustomerId(UUID customerId) {
    return cartRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new EntityNotFoundException("Cart not found for customer id: " + customerId));
  }

  private ECart findOrCreateCart(UUID customerId) {
    return cartRepository.findByCustomerId(customerId)
        .orElseGet(() -> {
          ECart newCart = ECart.builder()
              .customerId(customerId)
              .cartItems(new ArrayList<>())
              .build();
          return cartRepository.save(newCart);
        });
  }

  private ProductVariantByShopVm getProductDetails(Set<UUID> variantIds) {
    if (variantIds.isEmpty()) {
      return ProductVariantByShopVm.builder()
          .shopProductVariants(new ArrayList<>())
          .build();
    }
    
    return Optional.ofNullable(productServiceClient.getProductCartDetail(variantIds).data())
        .orElseThrow(() -> new EntityNotFoundException("Product details not found for cart items"));
  }

  private void validateAddToCartRequest(AddToCartRequest request) {
    if (request.getQuantity() <= 0) {
      throw new BadRequestException("Quantity must be greater than 0");
    }
    if (request.getQuantity() > MAX_ITEM_QUANTITY) {
      throw new BadRequestException("Quantity cannot exceed " + MAX_ITEM_QUANTITY);
    }
  }

  private CartSummaryResponse calculateCartSummary(ECart cart, ProductVariantByShopVm productDetails) {
    Map<UUID, ProductVariantReviewVm> variantMap = productDetails.shopProductVariants().stream()
        .flatMap(shop -> shop.productVariantReviews().stream())
        .collect(Collectors.toMap(ProductVariantReviewVm::id, Function.identity()));

    int totalItems = cart.getCartItems().size();
    int selectedItems = (int) cart.getCartItems().stream().filter(ECartItem::getIsSelected).count();
    
    double totalPrice = cart.getCartItems().stream()
        .mapToDouble(item -> {
          ProductVariantReviewVm variant = variantMap.get(item.getProductVariantId());
          return variant != null ? variant.price() * item.getQuantity() : 0.0;
        })
        .sum();
    
    double selectedItemsPrice = cart.getCartItems().stream()
        .filter(ECartItem::getIsSelected)
        .mapToDouble(item -> {
          ProductVariantReviewVm variant = variantMap.get(item.getProductVariantId());
          return variant != null ? variant.price() * item.getQuantity() : 0.0;
        })
        .sum();
    
    double estimatedShipping = selectedItems > 0 ? DEFAULT_SHIPPING_COST : 0.0;
    double estimatedTax = selectedItemsPrice * DEFAULT_TAX_RATE;
    double estimatedTotal = selectedItemsPrice + estimatedShipping + estimatedTax;
    
    boolean hasOutOfStockItems = cart.getCartItems().stream()
        .anyMatch(item -> {
          ProductVariantReviewVm variant = variantMap.get(item.getProductVariantId());
          return variant != null && variant.stock() < item.getQuantity();
        });
    
    boolean hasUnavailableItems = cart.getCartItems().stream()
        .anyMatch(item -> !variantMap.containsKey(item.getProductVariantId()));

    return CartSummaryResponse.builder()
        .cartId(cart.getId())
        .customerId(cart.getCustomerId())
        .totalItems(totalItems)
        .selectedItems(selectedItems)
        .totalPrice(totalPrice)
        .selectedItemsPrice(selectedItemsPrice)
        .estimatedShipping(estimatedShipping)
        .estimatedTax(estimatedTax)
        .estimatedTotal(estimatedTotal)
        .hasOutOfStockItems(hasOutOfStockItems)
        .hasUnavailableItems(hasUnavailableItems)
        .build();
  }

  private List<EnhancedCartResponse.CartByShop> buildEnhancedCartByShops(ECart cart, ProductVariantByShopVm productDetails) {
    Map<UUID, ProductVariantReviewVm> variantMap = productDetails.shopProductVariants().stream()
        .flatMap(shop -> shop.productVariantReviews().stream())
        .collect(Collectors.toMap(ProductVariantReviewVm::id, Function.identity()));

    Map<UUID, List<ECartItem>> itemsByShop = new HashMap<>();
    
    // Group cart items by shop
    for (ECartItem item : cart.getCartItems()) {
      ProductVariantReviewVm variant = variantMap.get(item.getProductVariantId());
      if (variant != null) {
        // For now, we'll use a default shop ID since the variant doesn't contain shop info
        UUID shopId = UUID.randomUUID(); // This should come from the product service
        itemsByShop.computeIfAbsent(shopId, k -> new ArrayList<>()).add(item);
      }
    }

    return itemsByShop.entrySet().stream()
        .map(entry -> {
          UUID shopId = entry.getKey();
          List<ECartItem> items = entry.getValue();
          
          List<CartItemResponse> cartItemResponses = items.stream()
              .map(item -> buildCartItemResponse(item, variantMap.get(item.getProductVariantId())))
              .collect(Collectors.toList());
          
          double totalPrice = cartItemResponses.stream()
              .mapToDouble(CartItemResponse::totalPrice)
              .sum();
          
          PriceStatisticsResponse priceStats = PriceStatisticsResponse.builder()
              .totalPrice(totalPrice)
              .finalPrice(totalPrice)
              .build();
          
          return EnhancedCartResponse.CartByShop.builder()
              .shopId(shopId)
              .shopName("Shop " + shopId) // This should come from shop service
              .cartItems(cartItemResponses)
              .priceStatistics(priceStats)
              .hasUnavailableItems(false)
              .totalItems(items.size())
              .selectedItems((int) items.stream().filter(ECartItem::getIsSelected).count())
              .build();
        })
        .collect(Collectors.toList());
  }

  private CartItemResponse buildCartItemResponse(ECartItem item, ProductVariantReviewVm variant) {
    if (variant == null) {
      // Handle case where variant is not found
      return CartItemResponse.builder()
          .cartItemId(item.getId())
          .productId(item.getProductId())
          .productVariantId(item.getProductVariantId())
          .productName("Unavailable Product")
          .quantity(item.getQuantity())
          .isSelected(item.getIsSelected())
          .isAvailable(false)
          .build();
    }
    
    return CartItemResponse.builder()
        .cartItemId(item.getId())
        .productId(item.getProductId())
        .productVariantId(item.getProductVariantId())
        .productName(variant.name())
        .productSku(variant.sku())
        .productImageUrl(variant.imageUrl())
        .variantFeatures(variant.features())
        .unitPrice(variant.price())
        .quantity(item.getQuantity())
        .totalPrice(variant.price() * item.getQuantity())
        .isSelected(item.getIsSelected())
        .isAvailable(variant.stock() >= item.getQuantity())
        .stockQuantity(variant.stock())
        .build();
  }
}

