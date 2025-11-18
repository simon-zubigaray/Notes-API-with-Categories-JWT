package jsz.myapp.categorized_notes_app.service;

import jsz.myapp.categorized_notes_app.dto.NoteDTO;
import jsz.myapp.categorized_notes_app.dto.UserDTO;
import jsz.myapp.categorized_notes_app.entity.CategoryEntity;
import jsz.myapp.categorized_notes_app.entity.NoteEntity;
import jsz.myapp.categorized_notes_app.entity.UserEntity;
import jsz.myapp.categorized_notes_app.repository.CategoryRepository;
import jsz.myapp.categorized_notes_app.repository.NoteRepository;
import jsz.myapp.categorized_notes_app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, CategoryRepository categoryRepository, UserRepository userRepository){
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<NoteEntity> getAll() {
        return noteRepository.findAll();
    }

    public void save(NoteDTO noteDto, String username) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        CategoryEntity categoryEntity = categoryRepository.findById(noteDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        NoteEntity note = new NoteEntity(
                noteDto.getId(),
                noteDto.getTitle(),
                noteDto.getContent(),
                categoryEntity,
                user
        );

        noteRepository.save(note);
    }

    public Optional<NoteDTO> findById(Long id) {
        Optional<NoteEntity> note = noteRepository.findById(id);

        UserEntity userEntity = userRepository.findByUsername(note.get().getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserDTO userDto = new UserDTO(
                userEntity.getId(),
                userEntity.getUsername()
        );

        return Optional.of(new NoteDTO(
                note.get().getId(),
                note.get().getTitle(),
                note.get().getContent(),
                note.get().getCategory().getId(),
                userDto
        ));
    }

    public List<NoteDTO> findByUserUsername(String username) {
        List<NoteEntity> notes = noteRepository.findByUserUsername(username);

        return notes.stream()
                .map(note -> new NoteDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getContent(),
                        note.getCategory().getId(),
                        new UserDTO(
                                note.getUser().getId(),
                                note.getUser().getUsername()
                        )
                ))
                .collect(Collectors.toList());
    }

    public NoteDTO updateNote(NoteDTO noteDTO, Long id) {
        NoteEntity note = noteRepository.findById(id).orElseThrow();
        CategoryEntity category = categoryRepository.getCategoryEntityById(id);

        UserEntity userEntity = userRepository.findByUsername(note.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        note.setCategory(category);
        note.setUser(userEntity);

        NoteEntity updatedNote = noteRepository.save(note);

        return new NoteDTO(
                updatedNote.getId(),
                updatedNote.getTitle(),
                updatedNote.getContent(),
                updatedNote.getCategory().getId(),
                new UserDTO(
                        userEntity.getId(),
                        userEntity.getUsername()
                )
        );
    }

    public NoteDTO deleteNote(Long id) {
        NoteEntity note = noteRepository.findById(id).orElseThrow();

        UserEntity userEntity = userRepository.findByUsername(note.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        noteRepository.deleteById(id);

        return new NoteDTO(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCategory().getId(),
                new UserDTO(
                        userEntity.getId(),
                        userEntity.getUsername()
                )
        );
    }
}
