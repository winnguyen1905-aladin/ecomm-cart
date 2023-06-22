package winnguyen1905.cart.persistance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import winnguyen1905.cart.persistance.entity.ECart;

@Repository
public interface CartRepository extends JpaRepository<ECart, UUID> {
  Optional<ECart> findByCreatedBy(String createdBy);
  Optional<ECart> findByCustomerId(UUID customerId);
  Optional<ECart> findByIdAndCustomerId(UUID id, UUID customerId);
  List<ECart> findAllByCustomerIdOrderByCreatedDateDesc(UUID customerId);
}
