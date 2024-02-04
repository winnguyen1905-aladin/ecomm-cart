package winnguyen1905.cart.core.model;

import winnguyen1905.cart.core.model.response.AbstractModel;

public record ProductImage(String url, int order, String type) implements AbstractModel {}
