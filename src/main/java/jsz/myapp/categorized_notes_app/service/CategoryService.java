package jsz.myapp.categorized_notes_app.service;

import org.springframework.transaction.annotation.Transactional;
import jsz.myapp.categorized_notes_app.dto.CategoryDTO;
import jsz.myapp.categorized_notes_app.dto.NoteDTO;
import jsz.myapp.categorized_notes_app.dto.UserDTO;
import jsz.myapp.categorized_notes_app.entity.CategoryEntity;
import jsz.myapp.categorized_notes_app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::getCategoryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
        return getCategoryDTO(category);
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        CategoryEntity category = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + categoryDTO.getId()));

        category.setName(categoryDTO.getName());
        CategoryEntity updated = categoryRepository.save(category);
        return getCategoryDTO(updated);
    }

    @Transactional
    public CategoryDTO deleteCategory(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));

        CategoryDTO categoryDTO = getCategoryDTO(category);
        categoryRepository.deleteById(id);
        return categoryDTO;
    }

    private CategoryDTO getCategoryDTO(CategoryEntity category) {
        List<NoteDTO> notes = category.getNotes().stream()
                .map(note -> new NoteDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getContent(),
                        note.getCategory().getId(),
                        new UserDTO(
                                note.getUser().getId(),
                                note.getUser().getUsername()
                        )
                )).toList();

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .notes(notes)
                .build();
    }
}