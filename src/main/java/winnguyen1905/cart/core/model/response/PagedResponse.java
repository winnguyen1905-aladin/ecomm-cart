package winnguyen1905.cart.core.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import winnguyen1905.cart.core.model.AbstractModel;

@Setter
@Getter
public class PagedResponse<T> extends AbstractModel {
    private int maxPageItems;

    private int page;

    private int size;

    private List<T> results;

    private int totalElements;

    private int totalPages;
}
