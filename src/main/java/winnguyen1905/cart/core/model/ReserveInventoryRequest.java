package winnguyen1905.cart.core.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import winnguyen1905.cart.core.model.response.AbstractModel;

@Data
@Builder
public class ReserveInventoryRequest implements AbstractModel {
  private UUID reservationId;
  private List<Item> items;

  @Data
  @Builder
  public static class Item {
    private UUID productId;
    private UUID variantId;
    private int quantity;
  }

  private Instant expiresAt;
}
