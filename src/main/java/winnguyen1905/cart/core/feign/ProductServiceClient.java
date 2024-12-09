package winnguyen1905.cart.core.feign;

import java.util.Set;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import winnguyen1905.cart.model.ReserveInventoryRequest;
import winnguyen1905.cart.model.ReserveInventoryResponse;
import winnguyen1905.cart.model.request.ProductVariantByShopVm;
import winnguyen1905.cart.secure.RestResponse;

@Service
@FeignClient(name = "PRODUCT-SERVICE", url = "http://localhost:8086")
public interface ProductServiceClient {

  @GetMapping("products/variant-details/{ids}")
  RestResponse<ProductVariantByShopVm> getProductCartDetail(@PathVariable("ids") Set<UUID> ids);

  @PostMapping("products/reserve-inventory")
  RestResponse<ReserveInventoryResponse> reserveInventory(@RequestBody ReserveInventoryRequest reserveInventoryRequest);

  // @Component
  // public static class ProductClientFallback implements ProductServiceClient {
  // @Override
  // public RestResponse<ProductVariantByShopVm>
  // getProductCartDetail(@PathVariable("ids") Set<UUID> ids) {
  // return RestResponse.ok(ProductVariantByShopVm.builder().build());
  // }
  // }
}
