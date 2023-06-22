package winnguyen1905.cart.persistance.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@Table(name = "carts")
public class ECart extends EBaseAudit {
  @Column(name = "customer_id")
  private UUID customerId;

  @Default
  @OneToMany(mappedBy = "cart", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  private List<ECartItem> cartItems = new ArrayList<>();
}
