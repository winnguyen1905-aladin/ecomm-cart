package winnguyen1905.cart.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import winnguyen1905.cart.model.response.AbstractModel;

@Builder
public record Variant(
    UUID id,
    double price,
    String sku,
    UUID productId,
    JsonNode features,
    List<ProductImage> images,
    int stock) implements AbstractModel {
  @Builder
  public Variant(
      UUID id,
      double price,
      String sku,
      UUID productId,
      JsonNode features,
      List<ProductImage> images,
      int stock) {
    this.id = id;
    this.price = price;
    this.sku = sku;
    this.productId = productId;
    this.features = features;
    this.images = images;
    this.stock = stock;
  }
}
