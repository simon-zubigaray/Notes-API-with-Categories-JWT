package jsz.myapp.categorized_notes_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "users")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Column(name = "full_name")
    private String fullName;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El email debe ser válido.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    @NotEmpty(message = "Debe tener al menos un rol.")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<NoteEntity> notes = new ArrayList<>();
}
