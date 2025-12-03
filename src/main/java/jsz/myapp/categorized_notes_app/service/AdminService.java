package jsz.myapp.categorized_notes_app.service;

import jsz.myapp.categorized_notes_app.dto.RegisterRequest;
import jsz.myapp.categorized_notes_app.dto.UserDTO;
import jsz.myapp.categorized_notes_app.entity.RoleEntity;
import jsz.myapp.categorized_notes_app.entity.UserEntity;
import jsz.myapp.categorized_notes_app.repository.RoleRepository;
import jsz.myapp.categorized_notes_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createUserWithRoles(RegisterRequest request) {

        UserEntity user = new UserEntity();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<RoleEntity> roles = new HashSet<>();

        List<String> rolesString = request.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .toList();

        for (String roleName : rolesString) {
            RoleEntity role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);

        UserEntity savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    private UserDTO convertToDTO(UserEntity user){
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}