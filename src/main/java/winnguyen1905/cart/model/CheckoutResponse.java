package winnguyen1905.cart.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import winnguyen1905.cart.model.response.AbstractModel;
import winnguyen1905.cart.model.response.CartResponse;
import winnguyen1905.cart.model.response.PriceStatisticsResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record CheckoutResponse(
    PriceStatisticsResponse priceStatistics,
    List<CheckoutItem> checkoutItems) implements AbstractModel {

  public record CheckoutItem(
      CartResponse cart,
      UUID bestDiscountId,
      Set<UUID> discountIds,
      PriceStatisticsResponse priceStatistics) implements AbstractModel {
  }
}
