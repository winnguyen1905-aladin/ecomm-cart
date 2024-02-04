package winnguyen1905.cart.core.model.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import winnguyen1905.cart.core.model.AbstractModel;

@Builder
public record CartResponse(
    List<CartByShop> cartByShops) implements AbstractModel {

  @Builder
  public record CartByShop(
      UUID shopId,
      List<CartItem> cartItems,
      PriceStatisticsResponse priceStatistic) {
  }

  @Builder
  public record CartItem(
      double price,
      int quantity,
      Boolean isSelected,
      ProductVariantReview productVariantReview) implements AbstractModel {
  }

}
