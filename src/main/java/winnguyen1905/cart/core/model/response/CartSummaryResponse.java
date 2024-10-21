package winnguyen1905.cart.core.model.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record CartSummaryResponse(
    UUID cartId,
    UUID customerId,
    int totalItems,
    int selectedItems,
    double totalPrice,
    double selectedItemsPrice,
    double estimatedShipping,
    double estimatedTax,
    double estimatedTotal,
    boolean hasOutOfStockItems,
    boolean hasUnavailableItems) implements AbstractModel {

  @Builder
  public CartSummaryResponse(
      UUID cartId,
      UUID customerId,
      int totalItems,
      int selectedItems,
      double totalPrice,
      double selectedItemsPrice,
      double estimatedShipping,
      double estimatedTax,
      double estimatedTotal,
      boolean hasOutOfStockItems,
      boolean hasUnavailableItems) {
    this.cartId = cartId;
    this.customerId = customerId;
    this.totalItems = totalItems;
    this.selectedItems = selectedItems;
    this.totalPrice = totalPrice;
    this.selectedItemsPrice = selectedItemsPrice;
    this.estimatedShipping = estimatedShipping;
    this.estimatedTax = estimatedTax;
    this.estimatedTotal = estimatedTotal;
    this.hasOutOfStockItems = hasOutOfStockItems;
    this.hasUnavailableItems = hasUnavailableItems;
  }
} 
