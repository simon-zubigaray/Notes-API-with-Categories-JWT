package jsz.myapp.categorized_notes_app.service;

import org.springframework.transaction.annotation.Transactional;
import jsz.myapp.categorized_notes_app.dto.NoteDTO;
import jsz.myapp.categorized_notes_app.dto.UserDTO;
import jsz.myapp.categorized_notes_app.entity.CategoryEntity;
import jsz.myapp.categorized_notes_app.entity.NoteEntity;
import jsz.myapp.categorized_notes_app.entity.UserEntity;
import jsz.myapp.categorized_notes_app.repository.CategoryRepository;
import jsz.myapp.categorized_notes_app.repository.NoteRepository;
import jsz.myapp.categorized_notes_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NoteDTO> getAll() {
        return noteRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public NoteDTO save(NoteDTO noteDto, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        CategoryEntity category = categoryRepository.findById(noteDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        NoteEntity note = new NoteEntity();
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setCategory(category);
        note.setUser(user);

        NoteEntity savedNote = noteRepository.save(note);
        return convertToDTO(savedNote);
    }

    @Transactional(readOnly = true)
    public Optional<NoteDTO> findById(Long id) {
        return noteRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<NoteDTO> findByUserUsername(String username) {
        List<NoteEntity> notes = noteRepository.findByUserUsername(username);
        return notes.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public NoteDTO updateNote(NoteDTO noteDTO, Long id) {
        NoteEntity note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con id: " + id));

        CategoryEntity category = categoryRepository.findById(noteDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + noteDTO.getCategoryId()));

        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        note.setCategory(category);

        return convertToDTO(note);
    }

    @Transactional
    public NoteDTO deleteNote(Long id) {
        NoteEntity note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con id: " + id));

        NoteDTO noteDTO = convertToDTO(note);
        noteRepository.deleteById(id);

        return noteDTO;
    }

    private NoteDTO convertToDTO(NoteEntity note) {
        return NoteDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .categoryId(note.getCategory().getId())
                .userDTO(UserDTO.builder()
                        .id(note.getUser().getId())
                        .username(note.getUser().getUsername())
                        .build())
                .build();
    }
}