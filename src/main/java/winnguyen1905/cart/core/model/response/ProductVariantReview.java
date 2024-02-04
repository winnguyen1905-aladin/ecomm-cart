package winnguyen1905.cart.core.model.response;

import java.util.UUID;

import lombok.Builder;
import winnguyen1905.cart.core.model.AbstractModel;

@Builder
public record ProductVariantReview(
    UUID id,
    int stock,
    String sku,
    String name,
    double price,
    UUID productId,
    String imageUrl,
    Object features) implements AbstractModel {
}
