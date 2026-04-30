package Quad.Assesment.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//verstuurt vragen naar de frontend zonder correctAnswer
public class QuestionDTO {
    private String question, category, difficulty;
    private List<String> answers;
}
