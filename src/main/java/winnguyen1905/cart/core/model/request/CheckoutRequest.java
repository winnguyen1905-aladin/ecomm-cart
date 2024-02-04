package winnguyen1905.cart.core.model.request;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import winnguyen1905.cart.core.model.AbstractModel;

@Getter
@Setter
@Builder
public class CheckoutRequest implements AbstractModel {
  private List<CheckoutItemRequest> checkoutItems;

  @Getter
  @Setter
  @Builder
  public static class CheckoutItemRequest implements AbstractModel {
    private UUID cartId;
    private Set<UUID> discountIds;
  }
}
