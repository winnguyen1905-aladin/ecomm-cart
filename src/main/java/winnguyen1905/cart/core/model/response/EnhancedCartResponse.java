package winnguyen1905.cart.core.model.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record EnhancedCartResponse(
    UUID cartId,
    UUID customerId,
    List<CartByShop> cartByShops,
    CartSummaryResponse summary,
    Instant createdAt,
    Instant updatedAt,
    long version) implements AbstractModel {

  @Builder
  public EnhancedCartResponse(
      UUID cartId,
      UUID customerId,
      List<CartByShop> cartByShops,
      CartSummaryResponse summary,
      Instant createdAt,
      Instant updatedAt,
      long version) {
    this.cartId = cartId;
    this.customerId = customerId;
    this.cartByShops = cartByShops;
    this.summary = summary;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.version = version;
  }

  @Builder
  public record CartByShop(
      UUID shopId,
      String shopName,
      List<CartItemResponse> cartItems,
      PriceStatisticsResponse priceStatistics,
      boolean hasUnavailableItems,
      int totalItems,
      int selectedItems) implements AbstractModel {

    @Builder
    public CartByShop(
        UUID shopId,
        String shopName,
        List<CartItemResponse> cartItems,
        PriceStatisticsResponse priceStatistics,
        boolean hasUnavailableItems,
        int totalItems,
        int selectedItems) {
      this.shopId = shopId;
      this.shopName = shopName;
      this.cartItems = cartItems;
      this.priceStatistics = priceStatistics;
      this.hasUnavailableItems = hasUnavailableItems;
      this.totalItems = totalItems;
      this.selectedItems = selectedItems;
    }
  }
} 
