package winnguyen1905.cart.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import winnguyen1905.cart.core.model.Cart;
import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.response.PriceStatisticsResponse;
import winnguyen1905.cart.exception.BadRequestException;
import winnguyen1905.cart.persistance.entity.ECart;
import winnguyen1905.cart.persistance.entity.ECartItem;
import winnguyen1905.cart.persistance.repository.CartItemRepository;
import winnguyen1905.cart.persistance.repository.CartRepository;
import winnguyen1905.cart.util.OptionalExtractor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final ModelMapper mapper;
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  public void handleAddToCart(UUID customerId, AddToCartRequest addToCartRequest) {
    ECart cart = this.cartRepository
        .findByShopIdAndCustomerId(addToCartRequest.getShopId(), customerId).orElse(null);

    if (cart == null) {
      cart = new ECart();
      cart.setCustomerId(customerId);
      cart.setShopId(addToCartRequest.getShopId());
    }

    ECartItem newCartItem = this.cartItemRepository
        .findByCartIdAndVariationId(cart.getId(), addToCartRequest.getVariationId()).orElse(null);

    if (newCartItem == null) {
      newCartItem = new ECartItem();
      newCartItem.setCart(cart);
      newCartItem.setVariationId(addToCartRequest.getVariationId());
    }

    newCartItem.setQuantity(newCartItem.getQuantity() + addToCartRequest.getQuantity());
    cart.getCartItems().add(newCartItem);

    cart = this.cartRepository.save(cart);
  }

  // not fixed yet
  // ----------------------------------------------------------------------------------------------
  // public List<Cart> handleGetCartReview(UUID customerId, Pageable page) {
  //   List<ECart> carts = this.cartRepository.findAllByCustomerIdOrderByCreatedDateDesc(customerId);
  //   List<Cart> cartDTOs = carts.stream().map(cart -> {
  //     Cart cartDTO = this.mapper.map(cart, Cart.class);
  //     return cartDTO;
  //   }).toList();
  //   return cartDTOs;
  // }

  @Override
  public Cart handleGetCartById(UUID cartId, UUID customerId) {
    ECart cart = OptionalExtractor.extractFromResource(this.cartRepository.findById(cartId));
    if (!cart.getCustomerId().equals(customerId))
      throw new BadRequestException("Not found cart id " + cartId);
    return this.mapper.map(cart, Cart.class);
  }

  @Override
  public PriceStatisticsResponse handleGetPriceStatisticsOfCart(UUID customerId, UUID cartId) {
    // ECart cart =
    // OptionalExtractor.extractFromResource(this.cartRepository.findById(cartId));

    // if (!cart.getCustomer().getId().equals(customerId))
    // throw new BadRequestException("Not found cart id " + cartId);

    // PriceStatisticsResponse priceStatisticsDTO =
    // CartUtils.getPriceStatisticsOfCart(cart);
    return null;
  }

  @Override
  public List<Cart> handleGetCarts(UUID customerId) {

    List<ECart> cartDetails = this.cartRepository.findAllByCustomerId(customerId);
    // cartPage.getContent().stream().forEach(cart -> {
    // Cart cartDTO = this.mapper.map(cart, Cart.class);
    // PriceStatisticsResponse PriceStatistic =
    // CartUtils.getPriceStatisticsOfCart(cart);
    // cartDTO.setPriceStatistic(PriceStatistic);
    // cartDTOs.add(cartDTO);
    // });
    // cartResponse.setResults(cartDTOs);

    return null;
  }

  @Override
  public Boolean handleValidateCart(Cart cartDTO, UUID customerId) {
    return true;
  }

}
