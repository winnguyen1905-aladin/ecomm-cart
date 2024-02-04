package winnguyen1905.cart.core.model.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record PriceStatisticsResponse(
    UUID discountId,
    Double totalPrice,
    Double totalShipPrice,

    Double amountShipReduced,
    Double totalDiscountVoucher,
    Double amountProductReduced,
    Double finalPrice) implements AbstractModel {
  @Builder
  public PriceStatisticsResponse(
      UUID discountId,
      Double totalPrice,
      Double totalShipPrice,

      Double amountShipReduced,
      Double totalDiscountVoucher,
      Double amountProductReduced,
      Double finalPrice) {
    this.discountId = discountId;
    this.totalPrice = totalPrice;
    this.totalShipPrice = totalShipPrice;
    this.amountShipReduced = amountShipReduced;
    this.totalDiscountVoucher = totalDiscountVoucher;
    this.amountProductReduced = amountProductReduced;
    this.finalPrice = finalPrice;
  }
}
