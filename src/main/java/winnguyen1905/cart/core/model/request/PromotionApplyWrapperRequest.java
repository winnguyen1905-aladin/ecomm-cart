package winnguyen1905.cart.core.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import winnguyen1905.cart.secure.TAccountRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionApplyWrapperRequest {
    private TAccountRequest accountRequest;
    private PromotionApplyRequest promotionRequest;
}
