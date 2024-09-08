package winnguyen1905.cart.core.model.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import winnguyen1905.cart.core.model.response.AbstractModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveCartItemRequest implements AbstractModel {
  
  @NotEmpty(message = "Cart item IDs cannot be empty")
  private List<UUID> cartItemIds;
  
  // Optional: remove specific quantity instead of entire item
  private Integer quantity;
} 
