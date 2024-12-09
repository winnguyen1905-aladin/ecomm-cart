package winnguyen1905.cart.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import winnguyen1905.cart.model.request.PromotionApplyRequest;
import winnguyen1905.cart.model.response.PriceStatisticsResponse;
import winnguyen1905.cart.secure.AccountRequest;
import winnguyen1905.cart.secure.RestResponse;
import winnguyen1905.cart.secure.TAccountRequest;

@Service
@FeignClient(name = "PROMOTION-SERVICE", url = "http://localhost:8095")
public interface PromotionServiceClient {
  @PostMapping("discounts/apply-discount-shop")
  RestResponse<PriceStatisticsResponse> applyDiscountShop(
      @RequestBody PromotionApplyRequest promotionApplyRequest);
}
