package winnguyen1905.cart.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.client.RpcClient.Response;
import com.thoughtworks.xstream.core.SecurityUtils;

import co.elastic.clients.elasticsearch.ml.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.request.CheckoutRequest;
import winnguyen1905.cart.core.model.request.ClearCartRequest;
import winnguyen1905.cart.core.model.request.UpdateCartItemDto;
import winnguyen1905.cart.core.model.response.CartResponse;
import winnguyen1905.cart.core.model.response.CheckoutOrderResponse;
import winnguyen1905.cart.core.service.CartService;
import winnguyen1905.cart.secure.AccountRequest;
import winnguyen1905.cart.secure.TAccountRequest;
import winnguyen1905.cart.util.MetaMessage;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("carts")
public class CartController {

  private final CartService cartService;

  @GetMapping
  public ResponseEntity<CartResponse> getCart(@AccountRequest TAccountRequest accountRequest, Pageable pageable) {
    return ResponseEntity.ok(this.cartService.getCart(accountRequest, pageable));
  }

  @PostMapping("/add")
  @MetaMessage(message = "Add product to cart successfully")
  public ResponseEntity<?> addToCart(@AccountRequest TAccountRequest accountRequest,
      @RequestBody AddToCartRequest addToCartRequest) {
    this.cartService.addToCart(accountRequest, addToCartRequest);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/item/{id}/update")
  @MetaMessage(message = "Update cart successfully")
  public ResponseEntity<?> updateCartItem(@AccountRequest TAccountRequest accountRequest,
      @RequestBody UpdateCartItemDto updateCartItemDto) {
    this.cartService.updateCartItem(accountRequest, updateCartItemDto);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/item/{id}/delete")
  @MetaMessage(message = "Delete cart item successfully")
  public ResponseEntity<Void> deleteCartItem(@AccountRequest TAccountRequest accountRequest,
      @RequestParam UUID cartItemId) {
    this.cartService.deleteCartItem(accountRequest, cartItemId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/item/{id}/select")
  @MetaMessage(message = "Select cart item successfully")
  public ResponseEntity<Void> selectCartItem(@AccountRequest TAccountRequest accountRequest,
      @RequestParam UUID cartItemId) {
    this.cartService.selectCartItem(accountRequest, cartItemId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/checkout")
  @MetaMessage(message = "Checkout successfully")
  public ResponseEntity<CheckoutOrderResponse> checkout(@AccountRequest TAccountRequest accountRequest,
      @RequestBody CheckoutRequest checkoutRequest) {
    return ResponseEntity.ok(this.cartService.checkout(accountRequest, checkoutRequest));
  }


  @PostMapping("/clear-cart")
  @MetaMessage(message = "Clear cart successfully")
  public ResponseEntity<Void> clearCart(@AccountRequest TAccountRequest accountRequest, ClearCartRequest clearCartRequest) {
    this.cartService.clearCart(accountRequest, clearCartRequest);
    return ResponseEntity.noContent().build();
  }
}
