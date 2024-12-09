package winnguyen1905.cart.core.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import winnguyen1905.cart.model.request.ProductVariantByShopVm;
import winnguyen1905.cart.model.response.CartResponse;
import winnguyen1905.cart.model.response.PriceStatisticsResponse;
import winnguyen1905.cart.persistance.entity.ECartItem;

public class CartMapper {
  private record CartItemsWithTotal(List<CartResponse.CartItem> cartItems, double totalPrice) {
  }

  public static final CartResponse with(HashMap<UUID, ECartItem> mapECartItem,
      ProductVariantByShopVm cartByShopProductResponse) {
    List<CartResponse.CartByShop> cartByShops = cartByShopProductResponse.shopProductVariants().stream()
        .map(shopProductVariant -> mapToCartByShop(mapECartItem, shopProductVariant))
        .collect(Collectors.toList());

    return new CartResponse(cartByShops);
  }

  private static CartResponse.CartByShop mapToCartByShop(
      HashMap<UUID, ECartItem> mapECartItem,
      ProductVariantByShopVm.ShopProductVariant shopProductVariant) {

    var cartItemsWithTotal = calculateCartItemsAndTotal(mapECartItem, shopProductVariant);

    return CartResponse.CartByShop.builder()
        .priceStatistic(PriceStatisticsResponse.builder()
            .totalPrice(cartItemsWithTotal.totalPrice())
            .finalPrice(cartItemsWithTotal.totalPrice())
            .build())
        .cartItems(cartItemsWithTotal.cartItems())
        .shopId(shopProductVariant.shopId())
        .build();
  }

  private static CartItemsWithTotal calculateCartItemsAndTotal(
      HashMap<UUID, ECartItem> mapECartItem,
      ProductVariantByShopVm.ShopProductVariant shopProductVariant) {

    double totalPrice = 0;
    List<CartResponse.CartItem> cartItems = new ArrayList<>();

    for (var productVariantReview : shopProductVariant.productVariantReviews()) {
      ECartItem cartItem = mapECartItem.get(productVariantReview.id());
      double itemPrice = productVariantReview.price() * cartItem.getQuantity();
      totalPrice += itemPrice;

      cartItems.add(CartResponse.CartItem.builder()
          .price(itemPrice)
          .quantity(cartItem.getQuantity())
          .isSelected(cartItem.getIsSelected())
          .productVariantReview(productVariantReview)
          .build());
    }

    return new CartItemsWithTotal(cartItems, totalPrice);
  }
}
