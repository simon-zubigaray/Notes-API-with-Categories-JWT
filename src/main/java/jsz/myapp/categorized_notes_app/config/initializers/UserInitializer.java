package jsz.myapp.categorized_notes_app.config.initializers;

import jsz.myapp.categorized_notes_app.entity.RoleEntity;
import jsz.myapp.categorized_notes_app.entity.UserEntity;
import jsz.myapp.categorized_notes_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            log.info("🚀 Iniciando precarga de datos...");

            // Verificar si ya existen usuarios
            if (userRepository.count() == 0) {
                // Crear usuario ADMIN
                UserEntity admin = new UserEntity();
                admin.setFullName("Administrador del Sistema");
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(new HashSet<RoleEntity>(Set.of(
                        RoleEntity.builder()
                                .name("ADMIN")
                                .build(),
                        RoleEntity.builder()
                                .name("USER")
                                .build()
                )));
                userRepository.save(admin);
                log.info("✅ Usuario ADMIN creado: username=admin, password=admin123");

                // Crear usuario normal
                UserEntity user = new UserEntity();
                user.setFullName("Usuario Normal");
                user.setUsername("user");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRoles(new HashSet<RoleEntity>(Set.of(
                        RoleEntity.builder()
                                .name("USER")
                                .build()
                )));
                userRepository.save(user);
                log.info("✅ Usuario USER creado: username=user, password=user123");

                // Crear usuario adicional
                UserEntity john = new UserEntity();
                john.setFullName("John Doe");
                john.setUsername("john");
                john.setEmail("john@example.com");
                john.setPassword(passwordEncoder.encode("john123"));
                john.setRoles(new HashSet<RoleEntity>(Set.of(
                        RoleEntity.builder()
                                .name("USER")
                                .build()
                )));
                userRepository.save(john);
                log.info("✅ Usuario USER creado: username=john, password=john123");

                log.info("🎉 Precarga de datos completada exitosamente!");
            } else {
                log.info("ℹ️ La base de datos ya contiene usuarios, omitiendo precarga.");
            }
        };
    }
}