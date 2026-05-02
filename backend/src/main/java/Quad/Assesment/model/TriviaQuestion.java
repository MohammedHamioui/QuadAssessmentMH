package Quad.Assesment.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
//mapped de JSON van de OpenTDB API naar een Java object
public class TriviaQuestion {
    private String type, difficulty, category, question;
    
    @JsonProperty("correct_answer")
    private String correctAnswer; 
    
    @JsonProperty("incorrect_answers")
    private List<String> incorrectAnswers;
}
