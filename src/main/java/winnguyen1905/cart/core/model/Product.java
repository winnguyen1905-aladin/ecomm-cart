package winnguyen1905.cart.core.model;

import java.util.List;

import lombok.*;

@Getter
@Setter
public class Product extends BaseObject<Product> {
  private String name;

  private String thumb;

  private String productType;

  private String description;

  private String slug;

  private List<Variation> variations;
}
