package winnguyen1905.cart.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thoughtworks.xstream.core.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.response.CartResponse;
import winnguyen1905.cart.core.service.CartService;
import winnguyen1905.cart.util.MetaMessage;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("${release.api.prefix}/carts")
public class CartController {

  private final CartService cartService;

  @PostMapping
  @MetaMessage(message = "Add product to cart successfully")
  public ResponseEntity<Void> addToCart(@RequestBody @Valid AddToCartRequest addToCartRequest) {
    this.cartService.addToCart(null, addToCartRequest);
    return ResponseEntity.noContent().build();
  }

  // @PatchMapping
  // @MetaMessage(message = "Update cart successfully")
  // public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItemDTO) {
  //   UUID customerId = SecurityUtils.getCurrentUserId().orElseThrow(() -> new CustomRuntimeException("Not found user"));
  //   return ResponseEntity.ok().body(this.cartItemService.handleUpdateCartItem(cartItemDTO, customerId));
  // }

  // @GetMapping
  // @MetaMessage(message = "Get my card successfully")
  // public ResponseEntity<Cart> getMethodName(Pageable pageable) {
  //   UUID customerId = SecurityUtils.getCurrentUserId().orElseThrow(() -> new CustomRuntimeException("Not found user"));
  //   return ResponseEntity.ok().body(this.cartService.handleGetMyCartDetails(customerId, pageable));
  // }

}
