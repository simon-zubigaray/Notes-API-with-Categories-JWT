package jsz.myapp.categorized_notes_app.controller;

import jakarta.validation.Valid;
import jsz.myapp.categorized_notes_app.dto.RegisterRequest;
import jsz.myapp.categorized_notes_app.dto.UserDTO;
import jsz.myapp.categorized_notes_app.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Solo ADMIN puede crear usuarios con roles personalizados
    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUserWithRoles(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = adminService.createUserWithRoles(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}