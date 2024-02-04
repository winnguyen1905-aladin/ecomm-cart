package winnguyen1905.cart.core.model.response;

import winnguyen1905.cart.core.model.AbstractModel;

public record RestResponse<T>(T data, String error, Object message, Integer statusCode) implements AbstractModel {}
