package winnguyen1905.cart.core.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record ProductVariant(
    UUID id,
    UUID productId,
    String name,
    String description,
    double price,
    String url,
    Object features,
    int stock) implements AbstractModel {
}
