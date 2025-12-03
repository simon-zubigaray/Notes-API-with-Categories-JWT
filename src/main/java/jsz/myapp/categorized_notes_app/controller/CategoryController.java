package jsz.myapp.categorized_notes_app.controller;

import jakarta.validation.Valid;
import jsz.myapp.categorized_notes_app.dto.CategoryDTO;
import jsz.myapp.categorized_notes_app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<CategoryDTO>> getAll(){
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryDTOList);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> save(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategory = categoryService.save(categoryDTO);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long id){
        CategoryDTO existingCategory = categoryService.getCategoryById(id);

        if(existingCategory == null) {
            throw new RuntimeException("Categoria no encontrada con id: " + id);
        }

        CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO, id);

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id){
        CategoryDTO existingCategory = categoryService.getCategoryById(id);

        if(existingCategory == null) {
            throw new RuntimeException("Categoria no encontrada con id: " + id);
        }

        CategoryDTO deletedCategory = categoryService.deleteCategory(id);

        return ResponseEntity.ok(deletedCategory);
    }
}
