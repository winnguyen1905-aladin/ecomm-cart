package winnguyen1905.cart.core.model.response;

import java.util.List;

import winnguyen1905.cart.core.model.AbstractModel;

public record PagedResponse<T>(
    int maxPageItems,
    int page,
    int size,
    List<T> results,
    int totalElements,
    int totalPages
) implements AbstractModel {}
