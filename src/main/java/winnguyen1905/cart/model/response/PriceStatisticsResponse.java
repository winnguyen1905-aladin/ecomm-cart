package winnguyen1905.cart.model.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record PriceStatisticsResponse(
    UUID discountId,
    double totalPrice,
    double totalShipPrice,

    double amountShipReduced,
    double totalDiscountVoucher,
    double amountProductReduced,
    double finalPrice) implements AbstractModel {
  @Builder
  public PriceStatisticsResponse(
      UUID discountId,
      double totalPrice,
      double totalShipPrice,

      double amountShipReduced,
      double totalDiscountVoucher,
      double amountProductReduced,
      double finalPrice) {
    this.discountId = discountId;
    this.totalPrice = totalPrice;
    this.totalShipPrice = totalShipPrice;
    this.amountShipReduced = amountShipReduced;
    this.totalDiscountVoucher = totalDiscountVoucher;
    this.amountProductReduced = amountProductReduced;
    this.finalPrice = finalPrice;
  }
}
