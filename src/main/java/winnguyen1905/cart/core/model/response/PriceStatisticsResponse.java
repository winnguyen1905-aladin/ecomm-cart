package winnguyen1905.cart.core.model.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import winnguyen1905.cart.core.model.AbstractModel;

@Builder
public record PriceStatisticsResponse(
    UUID discountId,
    Double totalPrice,
    Double totalShipPrice,

    Double amountShipReduced,
    Double totalDiscountVoucher,
    Double amountProductReduced,
    Double finalPrice) implements AbstractModel {}
