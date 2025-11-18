package jsz.myapp.categorized_notes_app.config;

import jsz.myapp.categorized_notes_app.entity.UserEntity;
import jsz.myapp.categorized_notes_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Convertir los roles a GrantedAuthority
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> {
                    // Asegurar que el rol tenga el prefijo ROLE_
                    String roleName = role.getName().startsWith("ROLE_")
                            ? role.getName()
                            : "ROLE_" + role.getName().toUpperCase();
                    return new SimpleGrantedAuthority(roleName);
                })
                .collect(Collectors.toList());

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}