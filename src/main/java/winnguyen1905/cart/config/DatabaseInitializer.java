package winnguyen1905.cart.config;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import winnguyen1905.cart.persistance.entity.ECart;
import winnguyen1905.cart.persistance.entity.ECartItem;
import winnguyen1905.cart.persistance.repository.CartItemRepository;
import winnguyen1905.cart.persistance.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

  private final CartRepository cartRepository;
  private final PasswordEncoder passwordEncoder;
  private final CartItemRepository cartItemRepository;

  // @Transactional
  @Override
  public void run(String... args) throws Exception {
    // EAccountCredentials user = EAccountCredentials.builder()
    // .username("1")
    // .password(this.passwordEncoder.encode("1"))
    // .status(true)
    // .accountType(AccountType.CUSTOMER)
    // .id(UUID.fromString("11111111-1111-4111-8111-111111111111"))
    // .build();

    ECart cart = ECart.builder()
        .customerId(UUID.fromString("11111111-1111-4111-8111-111111111111"))
        .build();

    ECartItem cartItem = ECartItem.builder()
        .cart(cart)
        .productId(UUID.fromString("00000001-0000-4000-8000-000000000001"))
        .productVariantId(UUID.fromString("00000001-0001-4000-8000-000000000011"))
        .quantity(10)
        .isSelected(true)
        .build();

    cart.getCartItems().add(cartItem);
    cartRepository.save(cart);
  }
}
