package winnguyen1905.cart.core.model.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record ProductVariantReview(
    UUID id,
    int stock,
    String sku,
    String name,
    double price,
    UUID productId,
    String imageUrl,
    Object features) implements AbstractModel {

  @Builder
  public ProductVariantReview(
      UUID id,
      int stock,
      String sku,
      String name,
      double price,
      UUID productId,
      String imageUrl,
      Object features) {
    this.id = id;
    this.stock = stock;
    this.sku = sku;
    this.name = name;
    this.price = price;
    this.productId = productId;
    this.imageUrl = imageUrl;
    this.features = features;
  }
}
