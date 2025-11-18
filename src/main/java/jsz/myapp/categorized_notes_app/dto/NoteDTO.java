package jsz.myapp.categorized_notes_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private UserDTO userDTO;
}

