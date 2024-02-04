package winnguyen1905.cart.core.model.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
  private UUID shopId;
  private UUID productId;
  private Integer quantity;
  private UUID productVariantId;
}
