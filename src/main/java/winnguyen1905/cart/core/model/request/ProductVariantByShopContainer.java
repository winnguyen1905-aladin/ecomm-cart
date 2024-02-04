package winnguyen1905.cart.core.model.request;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import winnguyen1905.cart.core.model.response.AbstractModel;
import winnguyen1905.cart.core.model.response.ProductVariantReview;

public record ProductVariantByShopContainer(
    List<ShopProductVariant> shopProductVariants) implements AbstractModel {
  public static record ShopProductVariant(
      UUID shopId,
      List<ProductVariantReview> productVariantReviews) implements AbstractModel {
  }
}
