package jsz.myapp.categorized_notes_app.repository;

import jsz.myapp.categorized_notes_app.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.notes WHERE c.id = :id")
    Optional<CategoryEntity> findByIdWithNotes(@Param("id") Long id);

    @Query("SELECT DISTINCT c FROM CategoryEntity c LEFT JOIN FETCH c.notes")
    List<CategoryEntity> findAllWithNotes();
}