package winnguyen1905.cart.core.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItem extends BaseObject {
  private UUID productId;
  private UUID variationId;
  private Integer oldQuantity;
  private Integer quantity;
  private Boolean isSelected;
}
