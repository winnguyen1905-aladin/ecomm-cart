package winnguyen1905.cart.core.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.request.CheckoutRequest;
import winnguyen1905.cart.core.model.request.ClearCartRequest;
import winnguyen1905.cart.core.model.request.UpdateCartItemDto;
import winnguyen1905.cart.core.model.response.CartResponse;
import winnguyen1905.cart.core.model.response.CheckoutOrderResponse;
import winnguyen1905.cart.secure.TAccountRequest;

public interface CartService {
  CartResponse getCart(TAccountRequest accountRequest, Pageable pageable);
  void deleteCartItem(TAccountRequest accountRequest, UUID cartItemId);
  void selectCartItem(TAccountRequest accountRequest, UUID cartItemId);
  void addToCart(TAccountRequest accountRequest, AddToCartRequest addCartRequest);
  void updateCartItem(TAccountRequest accountRequest, UpdateCartItemDto updateCartItemDto);
  void applyDiscount(TAccountRequest accountRequest, CheckoutRequest checkoutRequest);
  void clearCart(TAccountRequest accountRequest, ClearCartRequest clearCartRequest);


  CheckoutOrderResponse checkout(TAccountRequest accountRequest, CheckoutRequest checkoutRequest);
}
