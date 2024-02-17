package winnguyen1905.cart.core.model.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record CartResponse(

    List<CartByShop> cartByShops) implements AbstractModel {

  @Builder
  public CartResponse(
      List<CartByShop> cartByShops) {
    this.cartByShops = cartByShops;
  }
  
  @Builder
  public record CartByShop(
      UUID shopId,
      List<CartItem> cartItems,
      PriceStatisticsResponse priceStatistic) {

    @Builder
    public CartByShop(
        UUID shopId,
        List<CartItem> cartItems,
        PriceStatisticsResponse priceStatistic) {
      this.shopId = shopId;
      this.cartItems = cartItems;
      this.priceStatistic = priceStatistic;
    }
  }

  @Builder
  public record CartItem(
      double price,
      int quantity,
      Boolean isSelected,
      ProductVariantReviewVm productVariantReview) implements AbstractModel {

    @Builder
    public CartItem(
        double price,
        int quantity,
        Boolean isSelected,
        ProductVariantReviewVm productVariantReview) {
      this.price = price;
      this.quantity = quantity;
      this.isSelected = isSelected;
      this.productVariantReview = productVariantReview;
    }
  }

}
