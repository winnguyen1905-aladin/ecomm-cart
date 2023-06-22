package winnguyen1905.cart.core.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Variation extends AbstractModel {
  private String detail;
  private Double price;
}
