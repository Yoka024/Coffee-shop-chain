package com.offee.Coffee.shop.chain.config;

import com.offee.Coffee.shop.chain.entity.*;
import com.offee.Coffee.shop.chain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Створюємо тестову кав'ярню (якщо ще немає)
        if (cafeRepository.count() == 0) {
            Cafe cafe = new Cafe();
            cafe.setName("Central Coffee");
            cafe.setAddress("вул. Шевченка, 1");
            cafe.setPhone("+380123456789");
            cafeRepository.save(cafe);

            // Додаємо елементи меню
            MenuItem espresso = new MenuItem();
            espresso.setName("Еспресо");
            espresso.setPrice(45.0);
            espresso.setCategory("Кава");
            espresso.setCafe(cafe);
            menuItemRepository.save(espresso);

            MenuItem cappuccino = new MenuItem();
            cappuccino.setName("Капучино");
            cappuccino.setPrice(65.0);
            cappuccino.setCategory("Кава");
            cappuccino.setCafe(cafe);
            menuItemRepository.save(cappuccino);

            MenuItem croissant = new MenuItem();
            croissant.setName("Круасан");
            croissant.setPrice(35.0);
            croissant.setCategory("Випічка");
            croissant.setCafe(cafe);
            menuItemRepository.save(croissant);

            System.out.println("✅ Створено тестову кав'ярню з елементами меню");
        }

        System.out.println("🎉 Додаток готовий! Зареєструйтеся та увійдіть для доступу до дашборду.");
    }
}
