package winnguyen1905.cart.core.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import winnguyen1905.cart.core.model.CartItem;
import winnguyen1905.cart.exception.BadRequestException;
import winnguyen1905.cart.persistance.entity.ECartItem;
import winnguyen1905.cart.persistance.repository.CartItemRepository;
import winnguyen1905.cart.util.OptionalExtractor;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

  private final ModelMapper modelMapper;
  private final CartItemRepository cartItemRepository;

  @Override
  public CartItem handleSelectCartItem(CartItem cartItemDTO, UUID customerId) {
    ECartItem cartItem = OptionalExtractor.extractFromResource(this.cartItemRepository.findById(cartItemDTO.getId()));
    if (!cartItem.getCart().getCustomerId().equals(customerId))
      throw new BadRequestException("Not found cart-item please check your information");

    cartItem.setIsSelected(!cartItem.getIsSelected());
    cartItem = this.cartItemRepository.save(cartItem);
    return this.modelMapper.map(cartItem, CartItem.class);
  }

  @Override
  public void handleDeleteCartItem(CartItem cartItemDTO, UUID customerId) {
    ECartItem cartItem = OptionalExtractor.extractFromResource(this.cartItemRepository.findById(cartItemDTO.getId()));
    if (!cartItem.getCart().getCustomerId().equals(customerId))
      throw new BadRequestException("Not found cart-item please check your information");
    this.cartItemRepository.delete(cartItem);
  }

  @Override
  public CartItem handleUpdateQuantityCartItem(CartItem cartItemDTO, UUID customerId) {
    ECartItem cartItem = OptionalExtractor.extractFromResource(this.cartItemRepository.findById(cartItemDTO.getId()));
    Integer newQuantity = cartItem.getQuantity();
    if (!cartItem.getCart().getCustomerId().equals(customerId) || newQuantity == null || newQuantity < 0)
      throw new BadRequestException("Not found cart-item please check your information");
    cartItem.setQuantity(cartItem.getQuantity());
    cartItem = this.cartItemRepository.save(cartItem);
    return this.modelMapper.map(cartItem, CartItem.class);
  }

}
