package winnguyen1905.cart.core.feign;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import winnguyen1905.cart.core.model.request.ProductVariantByShopContainer;

@FeignClient(name = "PRODUCT-SERVICE", url = "http://localhost:8090")
public interface ProductServiceClient {

  @GetMapping("products/variant-detail/{ids}")
  ProductVariantByShopContainer getProductCartDetail(@PathVariable Set<UUID> ids);

  // @PostMapping("inventory/update-redis")
  // ResponseEntity<?> updateInventory(
  // @RequestBody Object inventory, @RequestHeader("service") String service);

  // @GetMapping("inventory/check")
  // ResponseEntity<ProductInventory> checkInventory(
  // @RequestParam("id") int variantId, @RequestHeader("service") String service);
}
