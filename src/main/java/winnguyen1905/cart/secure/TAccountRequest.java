package winnguyen1905.cart.secure;

import java.util.UUID;

import lombok.Builder;
import winnguyen1905.cart.core.model.response.AbstractModel;

@Builder
public record TAccountRequest(
    UUID id,
    String username,
    AccountType accountType,
    UUID socketClientId, RegionPartition region) implements AbstractModel {

  @Builder
  public TAccountRequest(
      UUID id,
      String username,
      AccountType accountType,
      UUID socketClientId, RegionPartition region) {
    this.id = id;
    this.username = username;
    this.accountType = accountType;
    this.region = region;
    this.socketClientId = socketClientId;
  }
}
