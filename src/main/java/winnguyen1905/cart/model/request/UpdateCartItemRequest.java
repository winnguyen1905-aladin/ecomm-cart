package winnguyen1905.cart.model.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import winnguyen1905.cart.model.response.AbstractModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemRequest implements AbstractModel {
  
  @NotNull(message = "Cart item ID cannot be null")
  private UUID cartItemId;
  
  @Min(value = 1, message = "Quantity must be at least 1")
  private Integer quantity;
  
  private Boolean isSelected;
} 
