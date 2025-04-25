package winnguyen1905.cart.model;

import winnguyen1905.cart.model.response.AbstractModel;

public record ProductImage(String url, int order, String type) implements AbstractModel {}
