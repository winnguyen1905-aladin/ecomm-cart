package winnguyen1905.cart.core.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import winnguyen1905.cart.core.feign.ProductServiceClient;
import winnguyen1905.cart.core.feign.PromotionServiceClient;
import winnguyen1905.cart.core.mapper.CartMapper;
import winnguyen1905.cart.core.model.ReserveInventoryRequest;
import winnguyen1905.cart.core.model.ReserveInventoryResponse;
import winnguyen1905.cart.core.model.response.AbstractModel;
import winnguyen1905.cart.core.model.response.PriceStatisticsResponse;
import winnguyen1905.cart.core.model.request.AddToCartRequest;
import winnguyen1905.cart.core.model.request.CheckoutRequest;
import winnguyen1905.cart.core.model.request.PromotionApplyRequest;
import winnguyen1905.cart.core.model.request.ClearCartRequest;
import winnguyen1905.cart.core.model.request.ProductVariantByShopVm;
import winnguyen1905.cart.core.model.request.UpdateCartItemDto;
import winnguyen1905.cart.core.model.request.CheckoutRequest.CheckoutItemRequest;
import winnguyen1905.cart.core.model.response.CartResponse;
import winnguyen1905.cart.core.model.response.CheckoutOrderResponse;
import winnguyen1905.cart.exception.BadRequestException;
import winnguyen1905.cart.persistance.entity.ECart;
import winnguyen1905.cart.persistance.entity.ECartItem;
import winnguyen1905.cart.persistance.repository.CartItemRepository;
import winnguyen1905.cart.persistance.repository.CartRepository;
import winnguyen1905.cart.secure.RestResponse;
import winnguyen1905.cart.secure.TAccountRequest;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductServiceClient productServiceClient;
  private final PromotionServiceClient promotionServiceClient;

  @Override
  @Transactional
  public void addToCart(TAccountRequest accountRequest, AddToCartRequest addToCartRequest) {
    ECart cart = cartRepository.findByCustomerId(accountRequest.id())
        .orElseGet(() -> {
          ECart newCart = ECart.builder()
              .customerId(accountRequest.id())
              .cartItems(new ArrayList<>())
              .build();
          return cartRepository.save(newCart);
        });

    if (addToCartRequest.getQuantity() <= 0) {
      throw new BadRequestException("Quantity must be greater than 0");
    }

    // Use repository to find existing cart item
    Optional<ECartItem> existingItem = cartItemRepository.findByCartAndProductVariantId(
        cart.getId(),
        addToCartRequest.getProductVariantId());

    if (existingItem.isPresent()) {
      // Update existing item
      ECartItem cartItem = existingItem.get();
      cartItem.setQuantity(cartItem.getQuantity() + addToCartRequest.getQuantity());
      cartItemRepository.save(cartItem);
    } else {
      // Create new item
      ECartItem newItem = ECartItem.builder()
          .cart(cart)
          .quantity(addToCartRequest.getQuantity())
          .productVariantId(addToCartRequest.getProductVariantId())
          .isSelected(false)
          .build();
      cartItemRepository.save(newItem);
    }
  }

  @Override
  public CartResponse getCart(TAccountRequest accountRequest, Pageable pageable) {
    ECart cart = this.cartRepository.findByCustomerId(accountRequest.id())
        .orElseThrow(() -> new EntityNotFoundException("Cart not found for customer id: " + accountRequest.id()));

    HashMap<UUID, ECartItem> mapECartItem = cart.getCartItems().stream()
        .collect(Collectors.toMap(ECartItem::getProductVariantId,
            Function.identity(), (existing, replacement) -> replacement, HashMap::new));

    Object responseEntity = this.productServiceClient
        .getProductCartDetail(mapECartItem.keySet());

    ProductVariantByShopVm cartByShopProductResponse = Optional
        .ofNullable(this.productServiceClient
            .getProductCartDetail(mapECartItem.keySet()).data())
        .orElseThrow(() -> new EntityNotFoundException("Product details not found for cart items"));

    return CartMapper.with(mapECartItem, cartByShopProductResponse);
  }

  @Override
  @Transactional
  public void deleteCartItem(TAccountRequest accountRequest, UUID itemId) {
    ECart cart = cartRepository.findByCustomerId(accountRequest.id())
        .orElseThrow(() -> new EntityNotFoundException("Cart not found for customer"));

    cartItemRepository.findByCustomerAndItemId(accountRequest.id(), itemId)
        .ifPresent(cartItem -> {
          cartItemRepository.delete(cartItem);
          cart.getCartItems().removeIf(item -> item.getId().equals(itemId));
        });
  }

  @Override
  @Transactional
  public void selectCartItem(TAccountRequest accountRequest, UUID itemId) {
    ECartItem cartItem = cartItemRepository.findByCustomerAndItemId(accountRequest.id(), itemId)
        .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
    cartItem.setIsSelected(!cartItem.getIsSelected());
    cartItemRepository.save(cartItem);
  }

  @Override
  public void updateCartItem(TAccountRequest accountRequest, UpdateCartItemDto updateCartItemDto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateCartItem'");
  }

  @Override
  public void applyDiscount(TAccountRequest accountRequest, CheckoutRequest checkoutRequest) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'applyDiscount'");
  }

  @Override
  public void clearCart(TAccountRequest accountRequest, ClearCartRequest clearCartRequest) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'clearCart'");
  }

  @Override
  public CheckoutOrderResponse checkout(TAccountRequest accountRequest, CheckoutRequest checkoutRequest) {
    // B1. Reserve inventory
    List<ReserveInventoryRequest.Item> items = new ArrayList<>();

    checkoutRequest.getCheckoutItems().stream().forEach(checkoutItem -> {
      checkoutItem.getItems().stream().forEach(item -> {
        items.add(ReserveInventoryRequest.Item.builder()
            .productId(item.getProductId())
            .variantId(item.getVariantId())
            .quantity(item.getQuantity())
            .build());
      });
    });

    ReserveInventoryRequest reserveInventoryRequest = ReserveInventoryRequest.builder()
        .reservationId(null)
        .items(items)
        .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
        .build();
    RestResponse<ReserveInventoryResponse> responseEntity = this.productServiceClient
        .reserveInventory(reserveInventoryRequest);
    if (!responseEntity.data().isStatus()) {
      throw new BadRequestException("Failed to reserve inventory");
    }

    List<CheckoutOrderResponse.CheckoutItemRequest> checkoutItems = new ArrayList<>();

    // Process each shop's items and apply promotions
    for (CheckoutRequest.CheckoutItemRequest checkoutItem : checkoutRequest.getCheckoutItems()) {
      // Get all variant IDs for the current checkout item
      Set<UUID> variantIds = checkoutItem.getItems().stream()
          .map(CheckoutRequest.Item::getVariantId)
          .collect(Collectors.toSet());

      // Fetch product details from the product service
      RestResponse<ProductVariantByShopVm> response = this.productServiceClient.getProductCartDetail(variantIds);
      if (response == null || response.data() == null) {
        throw new RuntimeException("Failed to get product details for shop: " + checkoutItem.getShopId());
      }

      // Create a map of variantId to quantity for quick lookup
      Map<UUID, Integer> variantQuantityMap = checkoutItem.getItems().stream()
          .collect(Collectors.toMap(
              CheckoutRequest.Item::getVariantId,
              CheckoutRequest.Item::getQuantity));

      // Calculate subtotal for this shop's items
      double subtotal = response.data().shopProductVariants().stream()
          .flatMap(shop -> shop.productVariantReviews().stream())
          .filter(variant -> variantQuantityMap.containsKey(variant.id()))
          .mapToDouble(variant -> {
            int quantity = variantQuantityMap.getOrDefault(variant.id(), 0);
            return variant.price() * quantity;
          })
          .sum();

      // Build checkout item response
      CheckoutOrderResponse.CheckoutItemRequest checkoutItemResponse = CheckoutOrderResponse.CheckoutItemRequest
          .builder()
          .shopId(checkoutItem.getShopId())
          .items(checkoutItem.getItems().stream()
              .map(item -> CheckoutOrderResponse.Item.builder()
                  .productId(item.getProductId())
                  .variantId(item.getVariantId())
                  .quantity(item.getQuantity())
                  .build())
              .collect(Collectors.toList()))
          .shippingDiscount(checkoutItem.getShippingDiscount())
          .shopProductDiscount(checkoutItem.getShopProductDiscount())
          .globalProductDiscount(checkoutItem.getGlobalProductDiscount())
          .build();

      // Apply promotions if any discount IDs are provided
      if (checkoutItem.getShippingDiscount() != null ||
          checkoutItem.getShopProductDiscount() != null ||
          checkoutItem.getGlobalProductDiscount() != null) {

        PromotionApplyRequest promotionRequest = new PromotionApplyRequest();
        promotionRequest.setShopId(checkoutItem.getShopId());
        promotionRequest.setTotal(subtotal);
        promotionRequest.setShippingDiscountId(checkoutItem.getShippingDiscount());
        promotionRequest.setShopProductDiscountId(checkoutItem.getShopProductDiscount());
        promotionRequest.setGlobalProductDiscountId(checkoutItem.getGlobalProductDiscount());

        try {
          RestResponse<PriceStatisticsResponse> promotionResponse = this.promotionServiceClient
              .applyDiscountShop(promotionRequest);

          if (promotionResponse != null && promotionResponse.data() != null) {
            PriceStatisticsResponse stats = promotionResponse.data();
            System.out.println(String.format(
                "Shop %s - Original: %.2f, Discounted: %.2f (Saved: %.2f)",
                checkoutItem.getShopId(),
                stats.totalPrice(),
                stats.finalPrice(),
                stats.totalDiscountVoucher()));

            // Set the price statistics in the response
            checkoutItemResponse.setPriceStatistics(stats);
          }
        } catch (Exception e) {
          System.err.println("Failed to apply promotions for shop " + checkoutItem.getShopId() + ": " + e.getMessage());
          // If promotion fails, still continue with the original price
          checkoutItemResponse.setPriceStatistics(PriceStatisticsResponse.builder()
              .totalPrice(subtotal)
              .finalPrice(subtotal)
              .totalDiscountVoucher(0.0)
              .build());
        }
      } else {
        // No promotions to apply, use the subtotal
        System.out.println(String.format(
            "Shop %s - No promotions applied. Total: %.2f",
            checkoutItem.getShopId(),
            subtotal));

        checkoutItemResponse.setPriceStatistics(PriceStatisticsResponse.builder()
            .totalPrice(subtotal)
            .finalPrice(subtotal)
            .totalDiscountVoucher(0.0)
            .build());
      }

      checkoutItems.add(checkoutItemResponse);
    }

    // Build and return the final response
    return CheckoutOrderResponse.builder()
        .checkoutItems(checkoutItems)
        .build();
  }

}
