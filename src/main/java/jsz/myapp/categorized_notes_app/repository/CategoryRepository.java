package jsz.myapp.categorized_notes_app.repository;

import jsz.myapp.categorized_notes_app.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {}
