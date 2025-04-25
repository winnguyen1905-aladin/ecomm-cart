package winnguyen1905.cart.model.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record CartOperationResponse(
    boolean success,
    String message,
    UUID cartId,
    List<UUID> affectedItemIds,
    CartSummaryResponse updatedSummary,
    List<String> warnings) implements AbstractModel {

  @Builder
  public CartOperationResponse(
      boolean success,
      String message,
      UUID cartId,
      List<UUID> affectedItemIds,
      CartSummaryResponse updatedSummary,
      List<String> warnings) {
    this.success = success;
    this.message = message;
    this.cartId = cartId;
    this.affectedItemIds = affectedItemIds;
    this.updatedSummary = updatedSummary;
    this.warnings = warnings;
  }
} 
