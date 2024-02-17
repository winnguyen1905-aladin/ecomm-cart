package winnguyen1905.cart.persistance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.LockModeType;
import winnguyen1905.cart.persistance.entity.ECartItem;

@Repository
@Transactional(readOnly = true)
public interface CartItemRepository extends JpaRepository<ECartItem, UUID> {
    
    @Query("SELECT ci FROM ECartItem ci " +
           "JOIN FETCH ci.cart c " +
           "WHERE c.customerId = :customerId AND ci.productVariantId = :productVariantId")
    Optional<ECartItem> findByCustomerAndProductVariantId(
        @Param("customerId") UUID customerId,
        @Param("productVariantId") UUID productVariantId);
    
    @Query("SELECT ci FROM ECartItem ci " +
           "JOIN FETCH ci.cart c " +
           "WHERE c.customerId = :customerId AND ci.id = :itemId")
    Optional<ECartItem> findByCustomerAndItemId(
        @Param("customerId") UUID customerId,
        @Param("itemId") UUID itemId);
        
    // Kept for backward compatibility
    @Deprecated
    @Query("SELECT ci FROM ECartItem ci " +
           "JOIN ci.cart c " +
           "WHERE ci.id = :id AND c.customerId = :customerId")
    default Optional<ECartItem> findByIdAndCustomerId(
        @Param("id") UUID id,
        @Param("customerId") UUID customerId) {
        return findByCustomerAndItemId(customerId, id);
    }
    
    @Query("SELECT ci FROM ECartItem ci " +
           "JOIN FETCH ci.cart c " +
           "WHERE c.customerId = :customerId")
    List<ECartItem> findAllByCustomerId(@Param("customerId") UUID customerId);
    
    @Query("SELECT ci FROM ECartItem ci " +
           "JOIN FETCH ci.cart c " +
           "WHERE c.id = :cartId AND ci.productVariantId = :productVariantId")
    Optional<ECartItem> findByCartAndProductVariantId(
        @Param("cartId") UUID cartId,
        @Param("productVariantId") UUID productVariantId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ECartItem ci " +
           "WHERE ci.cart.id IN (" +
           "  SELECT c.id FROM ECart c WHERE c.customerId = :customerId" +
           ") AND ci.id IN :itemIds")
    int deleteCustomerItems(
        @Param("customerId") UUID customerId,
        @Param("itemIds") List<UUID> itemIds);
    
    @Modifying
    @Transactional
    @Query("UPDATE ECartItem ci " +
           "SET ci.quantity = :quantity, " +
           "    ci.isSelected = :isSelected " +
           "WHERE ci.id IN (" +
           "  SELECT ci2.id FROM ECartItem ci2 " +
           "  JOIN ci2.cart c " +
           "  WHERE c.customerId = :customerId AND ci2.id = :itemId" +
           ")")
    int updateCustomerItem(
        @Param("customerId") UUID customerId,
        @Param("itemId") UUID itemId,
        @Param("quantity") Integer quantity,
        @Param("isSelected") Boolean isSelected);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ci FROM ECartItem ci " +
           "JOIN FETCH ci.cart c " +
           "WHERE c.customerId = :customerId AND ci.id = :itemId")
    Optional<ECartItem> findByCustomerAndItemIdForUpdate(
        @Param("customerId") UUID customerId,
        @Param("itemId") UUID itemId);
}
