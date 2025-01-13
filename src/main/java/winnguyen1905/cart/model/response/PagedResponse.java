package winnguyen1905.cart.model.response;

import java.util.List;

public record PagedResponse<T>(
    int maxPageItems,
    int page,
    int size,
    List<T> results,
    int totalElements,
    int totalPages
) implements AbstractModel {}
