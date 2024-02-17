package winnguyen1905.cart.persistance.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "cart_items", schema = "public")
public class ECartItem {
  @Version
  private long version;

  @Id
  private UUID id;
  
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

  @PrePersist 
  public void prePersist() {
    if (this.id == null) {
      this.id = UUID.randomUUID();
    }
    this.isSelected = true;
  }
}
