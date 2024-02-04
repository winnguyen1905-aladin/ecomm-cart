package winnguyen1905.cart.core.service;

import java.util.UUID;

import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.response.CartResponse;

public interface CartService {
  CartResponse getCartByCustomerId(UUID customerId);
  void deleteCartItem(UUID customerId, UUID carItemtId);
  void selectCartItem(UUID customerId, UUID carItemtId);
  void addToCart(UUID customerId, AddToCartRequest adCartRequest);
  // PriceStatisticsResponse getPriceStatisticsOfCart(UUID customerId, UUID cartId);
}
