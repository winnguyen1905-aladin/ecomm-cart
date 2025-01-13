package winnguyen1905.cart.model;

import java.util.UUID;

import lombok.Builder;
import winnguyen1905.cart.model.response.AbstractModel;

@Builder
public record Inventory(
    UUID id,
    String createdDate,
    String updatedDate,
    Boolean isDeleted,
    int quantitySold,
    int quantityReserved,
    int quantityAvailable) implements AbstractModel {
  @Builder
  public Inventory(
      UUID id,
      String createdDate,
      String updatedDate,
      Boolean isDeleted,
      int quantitySold,
      int quantityReserved,
      int quantityAvailable) {
    this.id = id;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
    this.isDeleted = isDeleted;
    this.quantitySold = quantitySold;
    this.quantityReserved = quantityReserved;
    this.quantityAvailable = quantityAvailable;
  }
}
