package winnguyen1905.cart.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.request.BulkUpdateCartRequest;
import winnguyen1905.cart.core.model.request.CheckoutRequest;
import winnguyen1905.cart.core.model.request.ClearCartRequest;
import winnguyen1905.cart.core.model.request.RemoveCartItemRequest;
import winnguyen1905.cart.core.model.request.UpdateCartItemRequest;
import winnguyen1905.cart.core.model.response.CartOperationResponse;
import winnguyen1905.cart.core.model.response.CartResponse;
import winnguyen1905.cart.core.model.response.CartSummaryResponse;
import winnguyen1905.cart.core.model.response.CheckoutOrderResponse;
import winnguyen1905.cart.core.model.response.EnhancedCartResponse;
import winnguyen1905.cart.core.service.CartService;
import winnguyen1905.cart.secure.AccountRequest;
import winnguyen1905.cart.secure.TAccountRequest;
import winnguyen1905.cart.util.MetaMessage;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/carts")
public class CartController {

  private final CartService cartService;

  // ========== Cart Retrieval Operations ==========

  /**
   * GET /api/v1/carts - Retrieve customer's cart
   */
  @GetMapping
  @MetaMessage(message = "Cart retrieved successfully")
  public ResponseEntity<CartResponse> getCart(@AccountRequest TAccountRequest accountRequest, Pageable pageable) {
    return ResponseEntity.ok(this.cartService.getCart(accountRequest, pageable));
  }

  /**
   * GET /api/v1/carts/enhanced - Retrieve customer's enhanced cart with detailed information
   */
  @GetMapping("/enhanced")
  @MetaMessage(message = "Enhanced cart retrieved successfully")
  public ResponseEntity<EnhancedCartResponse> getEnhancedCart(
      @AccountRequest TAccountRequest accountRequest, 
      Pageable pageable) {
    return ResponseEntity.ok(this.cartService.getEnhancedCart(accountRequest, pageable));
  }

  /**
   * GET /api/v1/carts/summary - Get cart summary with totals
   */
  @GetMapping("/summary")
  @MetaMessage(message = "Cart summary retrieved successfully")
  public ResponseEntity<CartSummaryResponse> getCartSummary(@AccountRequest TAccountRequest accountRequest) {
    return ResponseEntity.ok(this.cartService.getCartSummary(accountRequest));
  }

  // ========== Cart Item Management Operations ==========

  /**
   * POST /api/v1/carts/items - Add item to cart
   */
  @PostMapping("/items")
  @MetaMessage(message = "Item added to cart successfully")
  public ResponseEntity<CartOperationResponse> addToCart(
      @AccountRequest TAccountRequest accountRequest,
      @Valid @RequestBody AddToCartRequest addToCartRequest) {
    CartOperationResponse response = this.cartService.addToCart(accountRequest, addToCartRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * PUT /api/v1/carts/items/{itemId} - Update cart item quantity and selection
   */
  @PutMapping("/items/{itemId}")
  @MetaMessage(message = "Cart item updated successfully")
  public ResponseEntity<CartOperationResponse> updateCartItem(
      @AccountRequest TAccountRequest accountRequest,
      @PathVariable UUID itemId,
      @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
    // Set the item ID from path variable
    updateCartItemRequest.setCartItemId(itemId);
    CartOperationResponse response = this.cartService.updateCartItem(accountRequest, updateCartItemRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * PATCH /api/v1/carts/items/bulk - Bulk update multiple cart items
   */
  @PatchMapping("/items/bulk")
  @MetaMessage(message = "Cart items updated successfully")
  public ResponseEntity<CartOperationResponse> bulkUpdateCartItems(
      @AccountRequest TAccountRequest accountRequest,
      @Valid @RequestBody BulkUpdateCartRequest bulkUpdateRequest) {
    CartOperationResponse response = this.cartService.bulkUpdateCartItems(accountRequest, bulkUpdateRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * DELETE /api/v1/carts/items/{itemId} - Remove single item from cart
   */
  @DeleteMapping("/items/{itemId}")
  @MetaMessage(message = "Cart item removed successfully")
  public ResponseEntity<CartOperationResponse> removeCartItem(
      @AccountRequest TAccountRequest accountRequest,
      @PathVariable UUID itemId) {
    RemoveCartItemRequest request = RemoveCartItemRequest.builder()
        .cartItemIds(java.util.List.of(itemId))
        .build();
    CartOperationResponse response = this.cartService.removeCartItems(accountRequest, request);
    return ResponseEntity.ok(response);
  }

  /**
   * DELETE /api/v1/carts/items - Remove multiple items from cart
   */
  @DeleteMapping("/items")
  @MetaMessage(message = "Cart items removed successfully")
  public ResponseEntity<CartOperationResponse> removeCartItems(
      @AccountRequest TAccountRequest accountRequest,
      @Valid @RequestBody RemoveCartItemRequest removeCartItemRequest) {
    CartOperationResponse response = this.cartService.removeCartItems(accountRequest, removeCartItemRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * PATCH /api/v1/carts/items/{itemId}/toggle-selection - Toggle item selection
   */
  @PatchMapping("/items/{itemId}/toggle-selection")
  @MetaMessage(message = "Cart item selection toggled successfully")
  public ResponseEntity<CartOperationResponse> toggleCartItemSelection(
      @AccountRequest TAccountRequest accountRequest,
      @PathVariable UUID itemId) {
    CartOperationResponse response = this.cartService.toggleCartItemSelection(accountRequest, itemId);
    return ResponseEntity.ok(response);
  }

  /**
   * PATCH /api/v1/carts/select-all - Select or deselect all cart items
   */
  @PatchMapping("/select-all")
  @MetaMessage(message = "All cart items selection updated successfully")
  public ResponseEntity<CartOperationResponse> selectAllCartItems(
      @AccountRequest TAccountRequest accountRequest,
      @RequestParam(defaultValue = "true") boolean selected) {
    CartOperationResponse response = this.cartService.selectAllCartItems(accountRequest, selected);
    return ResponseEntity.ok(response);
  }

  // ========== Cart Operations ==========

  /**
   * DELETE /api/v1/carts - Clear entire cart
   */
  @DeleteMapping
  @MetaMessage(message = "Cart cleared successfully")
  public ResponseEntity<CartOperationResponse> clearCart(
      @AccountRequest TAccountRequest accountRequest, 
      @RequestBody(required = false) ClearCartRequest clearCartRequest) {
    if (clearCartRequest == null) {
      clearCartRequest = new ClearCartRequest();
    }
    CartOperationResponse response = this.cartService.clearCart(accountRequest, clearCartRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * POST /api/v1/carts/merge/{sourceCartId} - Merge another cart into current cart
   */
  @PostMapping("/merge/{sourceCartId}")
  @MetaMessage(message = "Cart merged successfully")
  public ResponseEntity<CartOperationResponse> mergeCart(
      @AccountRequest TAccountRequest accountRequest,
      @PathVariable UUID sourceCartId) {
    CartOperationResponse response = this.cartService.mergeCart(accountRequest, sourceCartId);
    return ResponseEntity.ok(response);
  }

  // ========== Cart Validation and Maintenance ==========

  /**
   * POST /api/v1/carts/validate - Validate cart items (check availability, prices, etc.)
   */
  @PostMapping("/validate")
  @MetaMessage(message = "Cart validation completed")
  public ResponseEntity<CartOperationResponse> validateCartItems(@AccountRequest TAccountRequest accountRequest) {
    CartOperationResponse response = this.cartService.validateCartItems(accountRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * DELETE /api/v1/carts/unavailable-items - Remove unavailable items from cart
   */
  @DeleteMapping("/unavailable-items")
  @MetaMessage(message = "Unavailable items removed successfully")
  public ResponseEntity<CartOperationResponse> removeUnavailableItems(@AccountRequest TAccountRequest accountRequest) {
    CartOperationResponse response = this.cartService.removeUnavailableItems(accountRequest);
    return ResponseEntity.ok(response);
  }

  // ========== Checkout Operations ==========

  /**
   * POST /api/v1/carts/checkout - Checkout cart
   */
  @PostMapping("/checkout")
  @MetaMessage(message = "Checkout completed successfully")
  public ResponseEntity<CheckoutOrderResponse> checkout(
      @AccountRequest TAccountRequest accountRequest,
      @Valid @RequestBody CheckoutRequest checkoutRequest) {
    return ResponseEntity.ok(this.cartService.checkout(accountRequest, checkoutRequest));
  }

  // ========== Legacy Endpoints (Deprecated) ==========

  /**
   * @deprecated Use POST /api/v1/carts/items instead
   */
  @PostMapping("/add")
  @MetaMessage(message = "Add product to cart successfully")
  @Deprecated
  public ResponseEntity<?> addToCartLegacy(@AccountRequest TAccountRequest accountRequest,
      @RequestBody AddToCartRequest addToCartRequest) {
    this.cartService.addToCart(accountRequest, addToCartRequest);
    return ResponseEntity.ok().build();
  }

  /**
   * @deprecated Use PATCH /api/v1/carts/items/{itemId}/toggle-selection instead
   */
  @PostMapping("/item/{id}/select")
  @MetaMessage(message = "Select cart item successfully")
  @Deprecated
  public ResponseEntity<Void> selectCartItemLegacy(@AccountRequest TAccountRequest accountRequest,
      @RequestParam UUID cartItemId) {
    this.cartService.selectCartItem(accountRequest, cartItemId);
    return ResponseEntity.noContent().build();
  }

  /**
   * @deprecated Use DELETE /api/v1/carts/items/{itemId} instead
   */
  @PostMapping("/item/{id}/delete")
  @MetaMessage(message = "Delete cart item successfully")
  @Deprecated
  public ResponseEntity<Void> deleteCartItemLegacy(@AccountRequest TAccountRequest accountRequest,
      @RequestParam UUID cartItemId) {
    this.cartService.deleteCartItem(accountRequest, cartItemId);
    return ResponseEntity.noContent().build();
  }

  /**
   * @deprecated Use DELETE /api/v1/carts instead
   */
  @PostMapping("/clear-cart")
  @MetaMessage(message = "Clear cart successfully")
  @Deprecated
  public ResponseEntity<Void> clearCartLegacy(@AccountRequest TAccountRequest accountRequest, 
      ClearCartRequest clearCartRequest) {
    this.cartService.clearCart(accountRequest, clearCartRequest);
    return ResponseEntity.noContent().build();
  }
}
