package winnguyen1905.cart.core.model.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import winnguyen1905.cart.core.model.AbstractModel;

@Getter
@Setter
public class CheckoutResponse extends AbstractModel {
  private PriceStatisticsResponse priceStatistics;
  private List<CheckoutItemReponse> checkoutItems = new ArrayList<>();

  @Getter
  @Setter
  public static class CheckoutItemReponse extends AbstractModel {
    private UUID cartId;
    private PriceStatisticsResponse priceStatistics;
  }
}
