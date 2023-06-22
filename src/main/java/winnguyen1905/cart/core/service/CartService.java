package winnguyen1905.cart.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import winnguyen1905.cart.core.model.Cart;
import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.response.PriceStatisticsResponse;

public interface CartService {
  List<Cart> handleGetCarts(UUID customerId);

  void handleAddToCart(UUID customerId, AddToCartRequest adCartRequest);

  Cart handleGetCartById(UUID cartId, UUID customerId);

  Boolean handleValidateCart(Cart cart, UUID customerId);

  PriceStatisticsResponse handleGetPriceStatisticsOfCart(UUID customerId, UUID cartId);
}
