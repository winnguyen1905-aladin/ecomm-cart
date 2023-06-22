package winnguyen1905.cart.persistance.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "cart_items")
public class ECartItem extends EBaseAudit {
  @ManyToOne
  @JoinColumn(name = "cart_id")
  private ECart cart;

  @Column(name = "product_variant_id")
  private UUID productVariantId;

  @Column(name = "product_id")
  private UUID productId;
  
  @Default
  @Column(name = "quantity")
  private Integer quantity = 0;

  @Column(name = "is_selected")
  private Boolean isSelected;
}
