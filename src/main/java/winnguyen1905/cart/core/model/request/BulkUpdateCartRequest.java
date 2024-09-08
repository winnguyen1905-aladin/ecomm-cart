package winnguyen1905.cart.core.model.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import winnguyen1905.cart.core.model.response.AbstractModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUpdateCartRequest implements AbstractModel {
  
  @NotEmpty(message = "Cart item updates cannot be empty")
  @Valid
  private List<CartItemUpdate> cartItemUpdates;
  
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CartItemUpdate {
    private UUID cartItemId;
    private Integer quantity;
    private Boolean isSelected;
  }
} 
