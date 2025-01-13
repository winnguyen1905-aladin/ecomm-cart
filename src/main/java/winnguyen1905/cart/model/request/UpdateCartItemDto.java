package winnguyen1905.cart.model.request;

import java.util.UUID;

import winnguyen1905.cart.model.response.AbstractModel;

public record UpdateCartItemDto(
    UUID cartItemId,
    Integer quantity) implements AbstractModel {

}
