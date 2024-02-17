package winnguyen1905.cart.persistance.entity;

import java.time.Instant;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@SQLRestriction("is_deleted <> true")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE products SET is_deleted = TRUE WHERE ID=? and VERSION=?")
public abstract class EBaseAudit extends EBase {
  // @Version
  // private long version;

  // @Column(name = "updated_by", nullable = true, updatable = true)
  // private String updatedBy;

  // @Column(name = "created_by", nullable = false, updatable = false)
  // private String createdBy;

  // @CreationTimestamp
  // @Column(name = "created_date", updatable = false)
  // private Instant createdDate;

  // @UpdateTimestamp
  // @Column(name = "updated_date", updatable = true)
  // private Instant updatedDate;

  // @Override
  // public boolean equals(Object o) {
  //   if (this == o)
  //     return true;
  //   if (!(o instanceof EBaseAudit))
  //     return false;
  //   if (!super.equals(o))
  //     return false;
  //   EBaseAudit that = (EBaseAudit) o;
  //   return createdBy.equals(that.createdBy) && updatedBy.equals(that.updatedBy) &&
  //       createdDate.equals(that.createdDate) && updatedDate.equals(that.updatedDate);
  // }

  // @Override
  // public int hashCode() {
  //   return Objects.hash(super.hashCode(), createdBy, updatedBy, createdDate, updatedDate);
  // }

  // public String findCurrentUser() {
  // return SecurityHolderUtils.getCurrentUserLogin().orElse("Unknown");
  // }

  // @PrePersist
  // protected void prePersist() {
  // this.setIsDeleted(false);
  // this.setCreatedBy(findCurrentUser());
  // }

  // @PreUpdate
  // protected void preUpdate() {
  // this.setUpdatedBy(findCurrentUser());
  // }

  // @PreRemove
  // protected void preRemove() {
  // this.setUpdatedBy(findCurrentUser());
  // }
}
