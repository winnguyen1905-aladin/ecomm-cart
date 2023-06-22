package winnguyen1905.cart.util;

import java.util.stream.Collectors;

import winnguyen1905.cart.core.model.response.PriceStatisticsResponse;
import winnguyen1905.cart.persistance.entity.ECart; 

public class CartUtils {
    // public static PriceStatisticsResponse getPriceStatisticsOfCart(ECart cart) {
    //     Double totalPriceOfAllProduct = cart.getCartItems().stream().filter(item -> {
    //         if (item.getProductVariation().getInventories().stream().allMatch(inven -> inven.getStock() == 0)) 
    //             throw new CustomRuntimeException("out of stock of product id: " + item.getProductVariation().getProduct().getId());
    //         return item.getIsSelected();
    //     }).collect(Collectors.summingDouble(cartItem -> cartItem.getQuantity() * cartItem.getProductVariation().getPrice()));

    //     return PriceStatisticsResponse.builder()
    //             .amountProductReduced(0.0)
    //             .amountShipReduced(0.0)
    //             .finalPrice(totalPriceOfAllProduct)
    //             .totalPrice(totalPriceOfAllProduct)
    //             .totalShipPrice(0.0)
    //             .totalDiscountVoucher(0.0).build();
    // }

    // public static Double getPriceOfAllProductSelectedInCart(ECart cart) {
    //     Double totalPriceOfAllProduct = cart.getCartItems().stream().filter(item -> {
    //         if (item.getProductVariation().getInventories().stream().allMatch(inven -> inven.getStock() == 0))
    //             throw new CustomRuntimeException("Out of stock of product id: " + item.getProductVariation().getProduct().getId());
    //         return item.getIsSelected();
    //     }).collect(Collectors.summingDouble(cartItem -> cartItem.getQuantity() * cartItem.getProductVariation().getPrice()));
    //     return totalPriceOfAllProduct;
    // }
}
