package winnguyen1905.cart.persistance.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "carts", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "customer_id" }, name = "uk_cart_customer_id") })
public class ECart {
  @Version
  private long version;

  @Id
  // @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "customer_id", unique = true)
  private UUID customerId;

  @Default
  @OneToMany(mappedBy = "cart", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
      CascadeType.REMOVE }, orphanRemoval = true)
  private List<ECartItem> cartItems = new ArrayList<>();

  @PrePersist
  public void prePersist() {
    if (this.id == null) {
      this.id = UUID.randomUUID();
    }
  }
}
