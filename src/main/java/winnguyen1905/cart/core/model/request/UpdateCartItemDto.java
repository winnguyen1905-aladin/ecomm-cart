package winnguyen1905.cart.core.model.request;

import java.util.UUID;

import winnguyen1905.cart.core.model.response.AbstractModel;

public record UpdateCartItemDto(
    UUID cartItemId,
    Integer quantity) implements AbstractModel {

}
