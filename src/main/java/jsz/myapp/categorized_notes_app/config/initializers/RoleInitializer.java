package jsz.myapp.categorized_notes_app.config.initializers;

import jsz.myapp.categorized_notes_app.entity.RoleEntity;
import jsz.myapp.categorized_notes_app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        // Crear roles si no existen
        createRoleIfNotExists("USER");
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("MODERATOR"); // opcional
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            RoleEntity role = new RoleEntity();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.println("Rol creado: " + roleName);
        }
    }
}