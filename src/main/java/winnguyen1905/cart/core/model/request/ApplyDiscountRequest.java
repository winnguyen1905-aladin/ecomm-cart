package winnguyen1905.cart.core.model.request;

import java.util.UUID;


import lombok.Builder;
import winnguyen1905.cart.core.model.request.CustomerCartDto.CustomerCartWithShop;

@Builder
public record ApplyDiscountRequest(
    UUID shopId,
    UUID discountId,
    UUID customerId,
    UUID shopDiscountId,
    UUID shippingDiscountId,
    UUID globallyDiscountId,
    CustomerCartWithShop customerCartWithShop) {

  @Builder
  public ApplyDiscountRequest(
      UUID shopId,
      UUID discountId,
      UUID customerId,
      UUID shopDiscountId,
      UUID shippingDiscountId,
      UUID globallyDiscountId,
      CustomerCartWithShop customerCartWithShop) {
    this.shopId = shopId;
    this.discountId = discountId;
    this.customerId = customerId;
    this.shopDiscountId = shopDiscountId;
    this.shippingDiscountId = shippingDiscountId;
    this.globallyDiscountId = globallyDiscountId;
    this.customerCartWithShop = customerCartWithShop;
  }
}
