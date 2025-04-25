package winnguyen1905.cart.model.request;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import winnguyen1905.cart.model.response.AbstractModel;

@Getter
@Setter
@NoArgsConstructor
public class PromotionApplyRequest implements AbstractModel {
  private UUID shopId;
  private double total;
  private UUID shippingDiscountId;
  private UUID shopProductDiscountId;
  private UUID globalProductDiscountId;
}
