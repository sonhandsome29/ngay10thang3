package com.example.sondeptraidemo;

import com.example.sondeptraidemo.model.Account;
import com.example.sondeptraidemo.model.Category;
import com.example.sondeptraidemo.model.Product;
import com.example.sondeptraidemo.model.Role;
import com.example.sondeptraidemo.repository.AccountRepository;
import com.example.sondeptraidemo.repository.CategoryRepository;
import com.example.sondeptraidemo.repository.ProductRepository;
import com.example.sondeptraidemo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            AccountRepository accountRepository,
            RoleRepository roleRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        createOrUpdateAccount("user", "123456", Set.of(userRole));
        createOrUpdateAccount("admin", "admin123", Set.of(adminRole));

        Category laptop = categoryRepository.findByNameIgnoreCase("Laptop")
                .orElseGet(() -> categoryRepository.save(new Category(null, "Laptop")));

        Category phone = categoryRepository.findByNameIgnoreCase("Điện thoại")
                .orElseGet(() -> categoryRepository.save(new Category(null, "Điện thoại")));

        if (productRepository.count() == 0) {
            productRepository.save(new Product(
                    null,
                    "Lenovo ThinkPad T15",
                    27000,
                    "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=600&q=80",
                    laptop
            ));

            productRepository.save(new Product(
                    null,
                    "iPhone 16 Pro Max 1TB",
                    41990,
                    "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?auto=format&fit=crop&w=600&q=80",
                    phone
            ));
        }
    }

    private void createOrUpdateAccount(String loginName, String rawPassword, Set<Role> roles) {
        Account account = accountRepository.findByLoginName(loginName)
                .orElseGet(Account::new);

        account.setLoginName(loginName);
        account.setPassword(passwordEncoder.encode(rawPassword));
        account.setRoles(new HashSet<>(roles));
        accountRepository.save(account);
    }
}
