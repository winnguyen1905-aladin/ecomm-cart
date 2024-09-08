package winnguyen1905.cart.core.model.response;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;

@Builder
public record CartItemResponse(
    UUID cartItemId,
    UUID productId,
    UUID productVariantId,
    String productName,
    String productSku,
    String productImageUrl,
    Object variantFeatures,
    double unitPrice,
    int quantity,
    double totalPrice,
    boolean isSelected,
    boolean isAvailable,
    int stockQuantity,
    Instant addedAt,
    Instant updatedAt) implements AbstractModel {

  @Builder
  public CartItemResponse(
      UUID cartItemId,
      UUID productId,
      UUID productVariantId,
      String productName,
      String productSku,
      String productImageUrl,
      Object variantFeatures,
      double unitPrice,
      int quantity,
      double totalPrice,
      boolean isSelected,
      boolean isAvailable,
      int stockQuantity,
      Instant addedAt,
      Instant updatedAt) {
    this.cartItemId = cartItemId;
    this.productId = productId;
    this.productVariantId = productVariantId;
    this.productName = productName;
    this.productSku = productSku;
    this.productImageUrl = productImageUrl;
    this.variantFeatures = variantFeatures;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.isSelected = isSelected;
    this.isAvailable = isAvailable;
    this.stockQuantity = stockQuantity;
    this.addedAt = addedAt;
    this.updatedAt = updatedAt;
  }
} 
