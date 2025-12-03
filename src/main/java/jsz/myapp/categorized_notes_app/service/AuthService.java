package jsz.myapp.categorized_notes_app.service;

import jsz.myapp.categorized_notes_app.config.CustomUserDetailsService;
import jsz.myapp.categorized_notes_app.dto.AuthRequest;
import jsz.myapp.categorized_notes_app.dto.AuthResponse;
import jsz.myapp.categorized_notes_app.dto.RegisterRequest;
import jsz.myapp.categorized_notes_app.entity.RoleEntity;
import jsz.myapp.categorized_notes_app.entity.UserEntity;
import jsz.myapp.categorized_notes_app.repository.RoleRepository;
import jsz.myapp.categorized_notes_app.repository.UserRepository;
import jsz.myapp.categorized_notes_app.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar que el username no exista
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        // Validar que el email no exista
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear nuevo usuario
        UserEntity user = new UserEntity();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        RoleEntity defaultRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity();
                    newRole.setName("USER");
                    return roleRepository.save(newRole);
                });

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        UserEntity savedUser = userRepository.save(user);

        // Generar tokens
        return getAuthResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Cargar usuario
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar tokens
        return getAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(refreshToken, userDetails)) {
            throw new RuntimeException("Token inválido o expirado");
        }

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return getAuthResponse(user);
    }

    private AuthResponse getAuthResponse(UserEntity user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        // Extraer roles
        List<String> roles = user.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toList());

        // Construir claims
        Map<String, Object> claims = buildClaims(user, roles);

        // Generar tokens
        String accessToken = jwtUtil.generateToken(claims, userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .build();
    }

    private Map<String, Object> buildClaims(UserEntity user, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("email", user.getEmail());
        claims.put("fullName", user.getFullName());
        return claims;
    }
}