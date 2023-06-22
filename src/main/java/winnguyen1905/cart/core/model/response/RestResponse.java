package winnguyen1905.cart.core.model.response;

import lombok.*;
import winnguyen1905.cart.core.model.AbstractModel;

@Getter
@Setter
@Builder
public class RestResponse<T> extends AbstractModel {
  private Integer statusCode;
  private String error;
  private Object message;
  private T data;
}
