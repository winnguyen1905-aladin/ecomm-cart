package winnguyen1905.cart.core.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

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
import winnguyen1905.cart.model.response.EnhancedCartResponse;
import winnguyen1905.cart.secure.TAccountRequest;

public interface CartService {
  
  // Cart retrieval operations
  CartResponse getCart(TAccountRequest accountRequest, Pageable pageable);
  EnhancedCartResponse getEnhancedCart(TAccountRequest accountRequest, Pageable pageable);
  CartSummaryResponse getCartSummary(TAccountRequest accountRequest);
  
  // Cart item management operations
  CartOperationResponse addToCart(TAccountRequest accountRequest, AddToCartRequest addCartRequest);
  CartOperationResponse updateCartItem(TAccountRequest accountRequest, UpdateCartItemRequest updateCartItemRequest);
  CartOperationResponse bulkUpdateCartItems(TAccountRequest accountRequest, BulkUpdateCartRequest bulkUpdateRequest);
  CartOperationResponse removeCartItems(TAccountRequest accountRequest, RemoveCartItemRequest removeCartItemRequest);
  CartOperationResponse toggleCartItemSelection(TAccountRequest accountRequest, UUID cartItemId);
  CartOperationResponse selectAllCartItems(TAccountRequest accountRequest, boolean selected);
  
  // Cart operations
  CartOperationResponse clearCart(TAccountRequest accountRequest, ClearCartRequest clearCartRequest);
  CartOperationResponse mergeCart(TAccountRequest accountRequest, UUID sourceCartId);
  
  // Legacy methods (deprecated but kept for backward compatibility)
  @Deprecated
  void deleteCartItem(TAccountRequest accountRequest, UUID cartItemId);
  @Deprecated
  void selectCartItem(TAccountRequest accountRequest, UUID cartItemId);
  
  // Checkout and discount operations
  void applyDiscount(TAccountRequest accountRequest, CheckoutRequest checkoutRequest);
  CheckoutOrderResponse checkout(TAccountRequest accountRequest, CheckoutRequest checkoutRequest);
  
  // Cart validation and maintenance
  CartOperationResponse validateCartItems(TAccountRequest accountRequest);
  CartOperationResponse removeUnavailableItems(TAccountRequest accountRequest);
}
