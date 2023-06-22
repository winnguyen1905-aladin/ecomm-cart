package winnguyen1905.cart.persistance.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import winnguyen1905.cart.persistance.entity.ECartItem;

@Repository
public interface CartItemRepository extends JpaRepository<ECartItem, UUID> {
  Optional<ECartItem> findByCreatedBy(String createdBy);
  Optional<ECartItem> findByIdAndCustomerId(UUID id, UUID customerId);
  Optional<ECartItem> findByCartIdAndVariationId(UUID cartId, UUID productVariantId);
}
