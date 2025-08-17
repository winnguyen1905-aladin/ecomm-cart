package winnguyen1905.cart.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import winnguyen1905.cart.core.service.CartService;
import winnguyen1905.cart.model.request.AddToCartRequest;
import winnguyen1905.cart.model.request.BulkUpdateCartRequest;
import winnguyen1905.cart.model.request.CheckoutRequest;
import winnguyen1905.cart.model.request.ClearCartRequest;
import winnguyen1905.cart.model.request.RemoveCartItemRequest;
import winnguyen1905.cart.model.request.UpdateCartItemRequest;
import winnguyen1905.cart.model.response.CartOperationResponse;
import winnguyen1905.cart.model.response.CartResponse;
import winnguyen1905.cart.model.response.CartSummaryResponse;
import winnguyen1905.cart.model.response.CheckoutOrderResponse;
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

/**
 * Simplified Cart Controller - Clean RESTful API for cart operations
 * Focuses on essential cart functionality with clean, consistent endpoints
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/carts")
public class CartController {

  private final CartService cartService;

  // ========== Cart Retrieval ==========

  /**
   * GET /api/v1/carts - Get customer's cart
   */
  @GetMapping
  @MetaMessage(message = "Cart retrieved successfully")
  public ResponseEntity<CartResponse> getCart(
      @AccountRequest TAccountRequest accountRequest, 
      Pageable pageable) {
    return ResponseEntity.ok(cartService.getCart(accountRequest, pageable));
  }

  /**
   * GET /api/v1/carts/summary - Get cart summary with totals
   */
  @GetMapping("/summary")
  @MetaMessage(message = "Cart summary retrieved successfully")
  public ResponseEntity<CartSummaryResponse> getCartSummary(
      @AccountRequest TAccountRequest accountRequest) {
    return ResponseEntity.ok(cartService.getCartSummary(accountRequest));
  }

  // ========== Cart Item Management ==========

  /**
   * POST /api/v1/carts/items - Add item to cart
   */
  @PostMapping("/items")
  @MetaMessage(message = "Item added to cart successfully")
  public ResponseEntity<CartOperationResponse> addToCart(
      @AccountRequest TAccountRequest accountRequest,
      @Valid @RequestBody AddToCartRequest addToCartRequest) {
    CartOperationResponse response = cartService.addToCart(accountRequest, addToCartRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * PUT /api/v1/carts/items/{itemId} - Update cart item
   */
  @PutMapping("/items/{itemId}")
  @MetaMessage(message = "Cart item updated successfully")
  public ResponseEntity<CartOperationResponse> updateCartItem(
      @AccountRequest TAccountRequest accountRequest,
      @PathVariable UUID itemId,
      @Valid @RequestBody UpdateCartItemRequest updateRequest) {
    updateRequest.setCartItemId(itemId);
    CartOperationResponse response = cartService.updateCartItem(accountRequest, updateRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * PATCH /api/v1/carts/items/bulk - Bulk update cart items
   */
  @PatchMapping("/items/bulk")
  @MetaMessage(message = "Cart items updated successfully")
  public ResponseEntity<CartOperationResponse> bulkUpdateItems(
      @AccountRequest TAccountRequest accountRequest,
      @Valid @RequestBody BulkUpdateCartRequest bulkUpdateRequest) {
    CartOperationResponse response = cartService.bulkUpdateCartItems(accountRequest, bulkUpdateRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * DELETE /api/v1/carts/items/{itemId} - Remove single item
   */
  @DeleteMapping("/items/{itemId}")
  @MetaMessage(message = "Cart item removed successfully")
  public ResponseEntity<CartOperationResponse> removeCartItem(
      @AccountRequest TAccountRequest accountRequest,
      @PathVariable UUID itemId) {
    RemoveCartItemRequest request = RemoveCartItemRequest.builder()
        .cartItemIds(java.util.List.of(itemId))
        .build();
    CartOperationResponse response = cartService.removeCartItems(accountRequest, request);
    return ResponseEntity.ok(response);
  }

  /**
   * DELETE /api/v1/carts/items - Remove multiple items
   */
  @DeleteMapping("/items")
  @MetaMessage(message = "Cart items removed successfully")
  public ResponseEntity<CartOperationResponse> removeCartItems(
      @AccountRequest TAccountRequest accountRequest,
      @Valid @RequestBody RemoveCartItemRequest removeRequest) {
    CartOperationResponse response = cartService.removeCartItems(accountRequest, removeRequest);
    return ResponseEntity.ok(response);
  }

  // ========== Cart Item Selection ==========

  /**
   * PATCH /api/v1/carts/items/{itemId}/toggle - Toggle item selection
   */
  @PatchMapping("/items/{itemId}/toggle")
  @MetaMessage(message = "Cart item selection toggled successfully")
  public ResponseEntity<CartOperationResponse> toggleItemSelection(
      @AccountRequest TAccountRequest accountRequest,
      @PathVariable UUID itemId) {
    CartOperationResponse response = cartService.toggleCartItemSelection(accountRequest, itemId);
    return ResponseEntity.ok(response);
  }

  /**
   * PATCH /api/v1/carts/select-all - Select/deselect all items
   */
  @PatchMapping("/select-all")
  @MetaMessage(message = "All cart items selection updated successfully")
  public ResponseEntity<CartOperationResponse> selectAllItems(
      @AccountRequest TAccountRequest accountRequest,
      @RequestParam(defaultValue = "true") boolean selected) {
    CartOperationResponse response = cartService.selectAllCartItems(accountRequest, selected);
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
      @RequestBody(required = false) ClearCartRequest clearRequest) {
    if (clearRequest == null) {
      clearRequest = new ClearCartRequest();
    }
    CartOperationResponse response = cartService.clearCart(accountRequest, clearRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * POST /api/v1/carts/merge/{sourceCartId} - Merge carts
   */
  @PostMapping("/merge/{sourceCartId}")
  @MetaMessage(message = "Cart merged successfully")
  public ResponseEntity<CartOperationResponse> mergeCart(
      @AccountRequest TAccountRequest accountRequest,
      @PathVariable UUID sourceCartId) {
    CartOperationResponse response = cartService.mergeCart(accountRequest, sourceCartId);
    return ResponseEntity.ok(response);
  }

  // ========== Cart Validation ==========

  /**
   * POST /api/v1/carts/validate - Validate cart items
   */
  @PostMapping("/validate")
  @MetaMessage(message = "Cart validation completed")
  public ResponseEntity<CartOperationResponse> validateCart(
      @AccountRequest TAccountRequest accountRequest) {
    CartOperationResponse response = cartService.validateCartItems(accountRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * DELETE /api/v1/carts/unavailable - Remove unavailable items
   */
  @DeleteMapping("/unavailable")
  @MetaMessage(message = "Unavailable items removed successfully")
  public ResponseEntity<CartOperationResponse> removeUnavailableItems(
      @AccountRequest TAccountRequest accountRequest) {
    CartOperationResponse response = cartService.removeUnavailableItems(accountRequest);
    return ResponseEntity.ok(response);
  }

  // ========== Checkout ==========

  /**
   * POST /api/v1/carts/checkout - Checkout cart
   */
  @PostMapping("/checkout")
  @MetaMessage(message = "Checkout completed successfully")
  public ResponseEntity<CheckoutOrderResponse> checkout(
      @AccountRequest TAccountRequest accountRequest,
      @Valid @RequestBody CheckoutRequest checkoutRequest) {
    CheckoutOrderResponse response = cartService.checkout(accountRequest, checkoutRequest);
    return ResponseEntity.ok(response);
  }
}
