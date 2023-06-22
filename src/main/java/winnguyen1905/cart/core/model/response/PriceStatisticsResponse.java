package winnguyen1905.cart.core.model.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import winnguyen1905.cart.core.model.AbstractModel;

@Getter
@Setter
@Builder
public class PriceStatisticsResponse extends AbstractModel {
  private Double totalPrice;
  private Double totalShipPrice;
  private UUID discountId;
  private Double totalDiscountVoucher;

  private Double amountShipReduced;
  private Double amountProductReduced;

  private Double finalPrice;
}
