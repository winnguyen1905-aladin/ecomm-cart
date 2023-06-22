package winnguyen1905.cart.util;

import java.util.Optional;

import winnguyen1905.cart.exception.ResourceNotFoundException;

public class OptionalExtractor {
  public static <T> T extractFromResource(Optional<T> optional) {
    if (optional.isPresent() && optional.get() instanceof T t)
      return t;
    else
      throw new ResourceNotFoundException("Resource not found for optional extract !");
  }
}
