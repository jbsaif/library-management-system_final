package com.library.system;

import com.library.system.model.User;
import com.library.system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementSystemApplication.class, args);
    }

    /**
     * This Bean runs code once the application is started.
     * It ensures the 'admin' account is saved to the database.
     * The default 'user' account creation has been removed.
     */
    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin already exists to prevent duplicates on every run
            if (userRepository.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Password must be encoded
                admin.setRole("ROLE_ADMIN"); // Ensure the role is correctly formatted
                admin.setFullName("System Administrator");
                userRepository.save(admin);
                System.out.println("Default 'admin' user created.");
            }

            // REMOVED: The block for creating the default 'user' account.
            // New users must now use the '/register' page to create an account.
        };
    }
}