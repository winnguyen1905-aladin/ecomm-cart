package winnguyen1905.cart.core.model.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import winnguyen1905.cart.core.model.response.AbstractModel;

@Getter
@Setter
public class AddToCartRequest implements AbstractModel {
  private UUID shopId;
  private UUID productId;
  private Integer quantity;
  private UUID productVariantId;
}
