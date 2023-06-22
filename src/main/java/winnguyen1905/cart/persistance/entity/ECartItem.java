package winnguyen1905.cart.persistance.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class ECartItem extends EBaseAudit {

  @ManyToOne
  @JoinColumn(name = "cart_id")
  private ECart cart;

  @Column(name = "variation_id")
  private UUID variationId;

  @Column(name = "product_id")
  private UUID productId;

  @Column(name = "quantity")
  private Integer quantity = 0;

  @Column(name = "is_selected")
  private Boolean isSelected;

}
