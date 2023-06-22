package winnguyen1905.cart.core.service;

import java.util.UUID;

import winnguyen1905.cart.core.model.CartItem;

public interface CartItemService {
  CartItem handleSelectCartItem(CartItem cartItem, UUID customerId);

  void handleDeleteCartItem(CartItem cartItemDTO, UUID customerId);

  CartItem handleUpdateQuantityCartItem(CartItem cartItemDTO, UUID customerId);
}
