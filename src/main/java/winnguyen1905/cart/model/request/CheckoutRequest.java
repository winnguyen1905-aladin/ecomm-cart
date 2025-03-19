package winnguyen1905.cart.model.request;

import java.util.List;
import java.util.UUID;

import lombok.*;
import winnguyen1905.cart.model.response.AbstractModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest implements AbstractModel {
  private List<CheckoutItemRequest> checkoutItems;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CheckoutItemRequest implements AbstractModel {
    private UUID shopId;
    private List<Item> items;
    private UUID shippingDiscount;
    private UUID shopProductDiscount;
    private UUID globalProductDiscount;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Item implements AbstractModel {
    private UUID productId;
    private UUID variantId;
    private int quantity;
  }
}
