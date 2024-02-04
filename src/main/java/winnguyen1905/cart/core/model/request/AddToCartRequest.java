package winnguyen1905.cart.core.model.request;

import java.util.UUID;

import org.apache.commons.pool2.BaseObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest extends BaseObject {
  private UUID shopId;
  private UUID productId;
  private Integer quantity;
  private UUID productVariantId;
}
