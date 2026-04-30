package Quad.Assesment.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
//mapped de JSON van de OpenTDB API naar een Java object
public class TriviaQuestion {
	@JsonProperty("type")
    private String type;
    
    @JsonProperty("difficulty")
    private String difficulty;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("question")
    private String question;
    
    @JsonProperty("correct_answer")
    private String correctAnswer; 
    
    @JsonProperty("incorrect_answers")
    private List<String> incorrectAnswers;
}
