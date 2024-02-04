package winnguyen1905.cart.core.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import winnguyen1905.cart.core.model.response.AbstractModel;

@Builder
public record ProductDetail(
    UUID id,
    String name,
    String slug,
    String brand,
    String thumb,
    Double price,
    String category,
    JsonNode features,
    Boolean isDeleted,
    String productType,
    String description,
    String createdDate,
    List<Variant> variations,
    List<Inventory> inventories,
    String updatedDate) implements AbstractModel {
  @Builder
  public ProductDetail(
      UUID id,
      String name,
      String slug,
      String brand,
      String thumb,
      Double price,
      String category,
      JsonNode features,
      Boolean isDeleted,
      String productType,
      String description,
      String createdDate,
      List<Variant> variations,
      List<Inventory> inventories,
      String updatedDate) {
    this.id = id;
    this.name = name;
    this.slug = slug;
    this.brand = brand;
    this.thumb = thumb;
    this.price = price;
    this.category = category;
    this.features = features;
    this.isDeleted = isDeleted;
    this.productType = productType;
    this.description = description;
    this.createdDate = createdDate;
    this.variations = variations;
    this.inventories = inventories;
    this.updatedDate = updatedDate;
  }
}
