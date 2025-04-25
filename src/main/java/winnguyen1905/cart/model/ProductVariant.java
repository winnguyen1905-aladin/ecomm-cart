package winnguyen1905.cart.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import winnguyen1905.cart.model.response.AbstractModel;

@Builder
public record ProductVariant(
    UUID id,
    UUID productId,
    String name,
    String description,
    double price,
    String url,
    Object features,
    int stock) implements AbstractModel {
  @Builder
  public ProductVariant(
      @NonNull UUID id,
      @NonNull UUID productId,
      @NonNull String name,
      @NonNull String description,
      double price,
      @NonNull String url,
      Object features,
      int stock) {
    this.id = id;
    this.productId = productId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.url = url;
    this.features = features;
    this.stock = stock;
  }
}
