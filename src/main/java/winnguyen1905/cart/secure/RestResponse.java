package winnguyen1905.cart.secure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import winnguyen1905.cart.model.response.AbstractModel;

@Builder
@JsonInclude(value = Include.NON_NULL)
public record RestResponse<T>(
    Integer statusCode,
    String error,
    Object message,
    T data)
    implements AbstractModel {
  @Builder
  public RestResponse(Integer statusCode, String error, Object message, T data) {
    this.statusCode = statusCode;
    this.error = error;
    this.message = message;
    this.data = data;
  }
}
