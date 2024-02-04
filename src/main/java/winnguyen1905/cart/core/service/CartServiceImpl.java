package winnguyen1905.cart.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import winnguyen1905.cart.core.feign.ProductServiceClient;
import winnguyen1905.cart.core.mapper.CartMapper;
import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.request.ProductVariantByShopContainer;
import winnguyen1905.cart.core.model.response.CartResponse;
import winnguyen1905.cart.exception.BadRequestException;
import winnguyen1905.cart.persistance.entity.ECart;
import winnguyen1905.cart.persistance.entity.ECartItem;
import winnguyen1905.cart.persistance.repository.CartItemRepository;
import winnguyen1905.cart.persistance.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductServiceClient productServiceClient;

  @Override
  public void addToCart(UUID customerId, AddToCartRequest addToCartRequest) {
    ECart cart = cartRepository.findByCustomerId(customerId)
        .orElseGet(() -> ECart.builder()
            .customerId(customerId)
            .cartItems(new ArrayList<>())
            .build());

    if (addToCartRequest.getQuantity() <= 0) {
      throw new BadRequestException("Quantity must be greater than 0");
    }

    ECartItem cartItem = cart.getCartItems().stream()
        .filter(item -> item.getProductVariantId().equals(addToCartRequest.getProductVariantId()))
        .findFirst()
        .orElseGet(() -> {
          ECartItem newItem = ECartItem.builder()
              .cart(cart)
              .quantity(0)
              .productVariantId(addToCartRequest.getProductVariantId()).build();
          cart.getCartItems().add(newItem);
          return newItem;
        });

    cartItem.setQuantity(cartItem.getQuantity() + addToCartRequest.getQuantity());
    cartRepository.save(cart);
  }

  @Override
  public CartResponse getCartByCustomerId(UUID customerId) {
    ECart cart = this.cartRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new EntityNotFoundException("Cart not found for customer id: " + customerId));

    HashMap<UUID, ECartItem> mapECartItem = cart.getCartItems().stream()
        .collect(Collectors.toMap(ECartItem::getProductVariantId,
            Function.identity(), (existing, replacement) -> replacement, HashMap::new));

    ProductVariantByShopContainer cartByShopProductResponse = Optional
        .ofNullable(this.productServiceClient.getProductCartDetail(mapECartItem.keySet()))
        .orElseThrow(() -> new EntityNotFoundException("Product details not found for cart items"));

    return CartMapper.with(mapECartItem, cartByShopProductResponse);
  }

  @Override
  public void deleteCartItem(UUID customerId, UUID cartId) {
    ECartItem cartItem = this.cartItemRepository.findByIdAndCustomerId(cartId, customerId)
        .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
    this.cartItemRepository.delete(cartItem);
  }

  @Override
  public void selectCartItem(UUID customerId, UUID cartId) {
    ECartItem cartItem = this.cartItemRepository.findByIdAndCustomerId(cartId, customerId)
        .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
    cartItem.setIsDeleted(!cartItem.getIsDeleted());
    this.cartItemRepository.save(cartItem);
  }

}
