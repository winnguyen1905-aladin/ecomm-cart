package winnguyen1905.cart.core.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import winnguyen1905.cart.core.model.response.PriceStatisticsResponse;

@Getter
@Setter
@Builder
public class CheckoutDTO extends AbstractModel {
  private PriceStatisticsResponse PriceStatistics;
  private List<CheckoutItemDTO> checkoutItems;

  @Getter
  @Setter
  @Builder
  public static class CheckoutItemDTO extends AbstractModel {
    private Cart cart;
    private Set<UUID> discountIds;
    private UUID bestDiscountId;
    private PriceStatisticsResponse PriceStatistics;
  }
}
