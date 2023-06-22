package winnguyen1905.cart.core.model;

import java.util.List;
import java.util.UUID;

import lombok.*;
import winnguyen1905.cart.core.model.response.PriceStatisticsResponse;

@Setter
@Getter
public class Cart extends BaseObject<Cart> {
  private UUID shopId;
  private List<CartItem> cartItems;
  private PriceStatisticsResponse priceStatistic;
}
