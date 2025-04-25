package winnguyen1905.cart.model.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class CheckoutOrderResponse {
  private List<CheckoutItemRequest> checkoutItems;

  @Builder
  @Data
  public static class CheckoutItemRequest implements AbstractModel {
    private UUID shopId;
    private List<Item> items;
    private UUID shippingDiscount;
    private UUID shopProductDiscount;
    private UUID globalProductDiscount;
    private PriceStatisticsResponse priceStatistics;
  }

  @Builder
  @Data
  public static class Item implements AbstractModel {
    private UUID productId;
    private UUID variantId;
    private int quantity;
  }
}
