package com.example.skillforge.config;

import com.example.skillforge.model.entity.User;
import com.example.skillforge.model.enums.Role;
import com.example.skillforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Migrate legacy admin email/name if present
        Optional<User> legacyAdmin = userRepository.findByEmail("selvaraj06061974@gmail.com");
        if (legacyAdmin.isPresent()) {
            User admin = legacyAdmin.get();
            admin.setName("Hariharan");
            admin.setEmail("khariharan.personal@gmail.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);
            admin.setVerified(true);
            userRepository.save(admin);
            System.out.println("Admin account migrated to: Hariharan (khariharan.personal@gmail.com)");
        }

        // 2. Ensure existing Admin account has the updated credentials
        Optional<User> currentAdmin = userRepository.findByEmail("khariharan.personal@gmail.com");
        if (currentAdmin.isPresent()) {
            User admin = currentAdmin.get();
            admin.setName("Hariharan");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);
            admin.setVerified(true);
            userRepository.save(admin);
            System.out.println("Admin password updated to Admin@123 for khariharan.personal@gmail.com");
        } else if (userRepository.countByRole(Role.ADMIN) == 0) {
            // 3. Seed Admin account if no ADMIN exists
            User admin = new User();
            admin.setName("Hariharan");
            admin.setEmail("khariharan.personal@gmail.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);
            admin.setVerified(true);
            userRepository.save(admin);
            System.out.println("Default Admin created: Hariharan (khariharan.personal@gmail.com)");
        }
    }
}
