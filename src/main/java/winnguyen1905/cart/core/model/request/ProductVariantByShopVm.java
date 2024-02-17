package winnguyen1905.cart.core.model.request;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import winnguyen1905.cart.core.model.response.AbstractModel;
import winnguyen1905.cart.core.model.response.ProductVariantReviewVm;


@Builder
public record ProductVariantByShopVm(
    List<ShopProductVariant> shopProductVariants) implements AbstractModel {
  @Builder
  public ProductVariantByShopVm(
      List<ShopProductVariant> shopProductVariants) {
    this.shopProductVariants = shopProductVariants;
  }

  public record ShopProductVariant(
      UUID shopId,
      List<ProductVariantReviewVm> productVariantReviews) implements AbstractModel {

    @Builder
    public ShopProductVariant(
        UUID shopId,
        List<ProductVariantReviewVm> productVariantReviews) {
      this.shopId = shopId;
      this.productVariantReviews = productVariantReviews;
    }

  }

}

