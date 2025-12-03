package jsz.myapp.categorized_notes_app.controller;

import jakarta.validation.Valid;
import jsz.myapp.categorized_notes_app.dto.NoteDTO;
import jsz.myapp.categorized_notes_app.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    // SOLO ADMIN - Ver todas las notas del sistema
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NoteDTO>> getAll() {
        List<NoteDTO> notes = noteService.getAll();
        return ResponseEntity.ok(notes);
    }

    // USER y ADMIN - Crear su propia nota
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<NoteDTO> save(@Valid @RequestBody NoteDTO note) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        NoteDTO savedNote = noteService.save(note, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    // USER y ADMIN - Ver UNA nota (solo si es suya, o es ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<NoteDTO> findById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        NoteDTO note = noteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con id: " + id));

        // Verificar que la nota pertenezca al usuario (o sea admin)
        if (!isAdmin && !note.getUserDTO().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para ver esta nota");
        }

        return ResponseEntity.ok(note);
    }

    // USER y ADMIN - Ver sus propias notas
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<NoteDTO>> findByAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        List<NoteDTO> notes = noteService.findByUserUsername(username);
        return ResponseEntity.ok(notes);
    }

    // USER y ADMIN - Actualizar nota (solo si es suya, o es ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<NoteDTO> updateNote(@Valid @RequestBody NoteDTO noteDTO, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Verificar que la nota exista y pertenezca al usuario
        NoteDTO existingNote = noteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con id: " + id));

        if (!isAdmin && !existingNote.getUserDTO().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para modificar esta nota");
        }

        NoteDTO updatedNote = noteService.updateNote(noteDTO, id);
        return ResponseEntity.ok(updatedNote);
    }

    // USER y ADMIN - Eliminar nota (solo si es suya, o es ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Verificar que la nota exista y pertenezca al usuario
        NoteDTO existingNote = noteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con id: " + id));

        if (!isAdmin && !existingNote.getUserDTO().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para eliminar esta nota");
        }

        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
