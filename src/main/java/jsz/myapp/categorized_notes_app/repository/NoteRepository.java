package jsz.myapp.categorized_notes_app.repository;

import jsz.myapp.categorized_notes_app.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    List<NoteEntity> findByTitleContainingOrContentContaining(String t, String c);
}
