package winnguyen1905.cart.persistance.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.LockModeType;
import winnguyen1905.cart.persistance.entity.ECart;

@Repository
@Transactional(readOnly = true)
public interface CartRepository extends JpaRepository<ECart, UUID> {

  @EntityGraph(attributePaths = { "cartItems" })
  @Query("SELECT c FROM ECart c WHERE c.customerId = :customerId")
  Optional<ECart> findByCustomerId(@Param("customerId") UUID customerId);

  @EntityGraph(attributePaths = { "cartItems" })
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c FROM ECart c WHERE c.customerId = :customerId")
  Optional<ECart> findByCustomerIdForUpdate(@Param("customerId") UUID customerId);

  // Kept for backward compatibility
  @Deprecated
  @EntityGraph(attributePaths = { "cartItems" })
  @Query("SELECT c FROM ECart c WHERE c.customerId = :customerId")
  default Optional<ECart> findCartsByCustomerId(@Param("customerId") UUID customerId) {
    return findByCustomerId(customerId);
  }

  @EntityGraph(attributePaths = { "cartItems" })
  @Query("SELECT c FROM ECart c WHERE c.id = :id AND c.customerId = :customerId")
  Optional<ECart> findByIdAndCustomerId(
      @Param("id") UUID id,
      @Param("customerId") UUID customerId);

  @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ECart c WHERE c.customerId = :customerId")
  boolean existsByCustomerId(@Param("customerId") UUID customerId);
}
