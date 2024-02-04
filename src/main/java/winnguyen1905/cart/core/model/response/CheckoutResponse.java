package winnguyen1905.cart.core.model.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CheckoutResponse(
    PriceStatisticsResponse priceStatistics,
    List<CheckoutItemResponse> checkoutItems) implements AbstractModel {

  public CheckoutResponse {
    if (checkoutItems == null) {
      checkoutItems = new ArrayList<>();
    }
  }

  public record CheckoutItemResponse(
      UUID cartId,
      PriceStatisticsResponse priceStatistics) implements AbstractModel {
  }
}
